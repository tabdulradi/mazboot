/*
 * Copyright 2021 Tamer Abdulradi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use a file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mazboot
package validations

import scala.util.CommandLineParser

import com.abdulradi.happypath.ErrorCase

/**
 * Typeclass that allows lookup of a validation given a Valid type
 */
sealed trait ValidationOf[V]:
  type R
  def validation: Validation[R] { type Valid = V }

object ValidationOf:
  type Aux[Valid <: Raw, Raw] = ValidationOf[Valid] { type R = Raw}

/**
 * Module that can define a newtype together with validation logic
 * All instances of this trait anywhere in the codebase can be retrieved
 * from the Valid type via the `ValidationOf` typeclass
 */
trait Validation[Raw]:
  outer =>

  opaque type Valid <: Raw = Raw
  
  given ValidationOf[Valid] with
    type R = Raw
    def validation = outer

  final case class Error(raw: Raw) extends ValidationError(outer.formatErrorMessage(raw))

  def validateWith[A](raw: Raw, handleSuccess: Valid => A, handleError: this.Error => A): A

  final def validate(raw: Raw): this.Error | Valid = 
    validateWith(raw, identity, identity)

  final def validateEither(raw: Raw): Either[this.Error, Valid] = 
    validateWith(raw, Right.apply, Left.apply)

  protected def formatErrorMessage(raw: Raw): String    

  given (using Raw: CommandLineParser.FromString[Raw]): CommandLineParser.FromString[Valid] with
    def fromString(s: String): Valid = validateWith(Raw.fromString(s), identity, e => throw IllegalArgumentException(e.message))

object Validation:
  def fromPredicate[Raw, V <: Raw](predicate: Raw => Boolean, predicateName: String): FromPredicate.Aux[Raw, V] = 
    FromPredicate.aux(predicate, predicateName)

abstract class FromPredicate0[Raw](
  val predicate: Raw => Boolean, 
  val predicateName: String) extends Validation[Raw]:

  override opaque type Valid <: Raw = Raw

  final def validateWith[A](raw: Raw, handleSuccess: Valid => A, handleError: this.Error => A): A = 
    if predicate(raw) then handleSuccess(raw) else handleError(this.Error(raw))

  protected def formatErrorMessage(raw: Raw): String = 
    s"'$raw' doesn't pass the predicate: $predicateName"

open class FromPredicate[Raw]( // Doesn't know that Valid is actually Raw
  predicate: Raw => Boolean, 
  predicateName: String) extends FromPredicate0[Raw](predicate, predicateName):

  infix def or[Valid2 <: Raw](that: FromPredicate.Aux[Raw, Valid2]): FromPredicate.Aux[Raw, Valid | Valid2]  =
    FromPredicate.aux(
      raw => this.predicate(raw) || that.predicate(raw),
      s"${this.predicateName} or ${that.predicateName}"
    )

  infix def and[Valid2 <: Raw](that: FromPredicate.Aux[Raw, Valid2]): FromPredicate.Aux[Raw, Valid & Valid2]  = 
    FromPredicate.aux(
      raw => this.predicate(raw) && that.predicate(raw),
      s"${this.predicateName} and ${that.predicateName}"
    )

  def negate: FromPredicate[Raw]  = 
    new FromPredicate[Raw](raw => ! predicate(raw), s"not $predicateName")

object FromPredicate:
  type Aux[Raw, V] = FromPredicate[Raw] { type Valid = V }

  def aux[Raw, V <: Raw](predicate: Raw => Boolean, predicateName: String): Aux[Raw, V] = 
    new FromPredicate[Raw](predicate, predicateName) {
      override type Valid = V
    }

/** checks if a value equals to the specified reference */
open class EqualsTo[A](a: A) extends FromPredicate[A](_ == a, s"equals $a")