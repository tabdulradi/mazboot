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
 * Module that can define a newtype together with validation logic
 * 
 * All instances of this trait anywhere in the codebase can be retrieved
 * given a Valid type via the `Validation.Of` typeclass
 */
trait Validation[Raw]:
  outer =>

  opaque type Valid <: Raw = Raw
  
  given Validation.Of[Valid] with
    type R = Raw
    def validation = outer

  final case class Error(raw: Raw) extends ValidationError(raw, outer)

  def validateWith[A](raw: Raw, handleSuccess: Valid => A, handleError: this.Error => A): A

  final def validate(raw: Raw): this.Error | Valid = 
    validateWith(raw, identity, identity)

  final def validateEither(raw: Raw): Either[this.Error, Valid] = 
    validateWith(raw, Right.apply, Left.apply)

  def formatErrorMessage(raw: Raw): String  

  final def unapply(raw: Raw): Option[Valid] =
    validateWith(raw, Some.apply, _ => None)

  given (using Raw: CommandLineParser.FromString[Raw]): CommandLineParser.FromString[Valid] with
    def fromString(s: String): Valid = validateWith(Raw.fromString(s), identity, e => throw IllegalArgumentException(formatErrorMessage(e.raw)))

object Validation:
  def fromPredicate[Raw, V <: Raw](predicate: Raw => Boolean, predicateName: String): FromPredicate.Aux[Raw, V] = 
    FromPredicate.aux(predicate, predicateName)

  /**
   * Typeclass that allows lookup of a validation given a Valid type
   */
  sealed trait Of[V]:
    type R
    def validation: Validation[R] { type Valid = V }

// Like it's super class, it know that Valid is actually Raw using the override trick needed to implement validateWith
abstract class FromPredicate0[Raw](val predicate: Raw => Boolean) extends Validation[Raw]:

  override opaque type Valid <: Raw = Raw

  final def validateWith[A](raw: Raw, handleSuccess: Valid => A, handleError: this.Error => A): A = 
    if predicate(raw) then handleSuccess(raw) else handleError(this.Error(raw))

// Unlike it's parent class, we don't have visibiltity through the opaque type. 
open class FromPredicate[Raw](predicate: Raw => Boolean) extends FromPredicate0[Raw](predicate):
  def predicateName = this.toString // We hope that conrete predicate will be an `object`

  def formatErrorMessage(raw: Raw): String =
    s"'$raw' is not a valid $predicateName"  

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

  def negate: FromPredicate[Raw] = 
    new FromPredicate[Raw](raw => ! predicate(raw)) {
      override def formatErrorMessage(raw: Raw): String = 
        s"'$raw' is not a valid non-$predicateName"  
    }

object FromPredicate:
  type Aux[Raw, V] = FromPredicate[Raw] { type Valid = V }

  def aux[Raw, V <: Raw](predicate: Raw => Boolean, pName: String): Aux[Raw, V] = 
    new FromPredicate[Raw](predicate) {
      override type Valid = V
      override val predicateName = pName
    }

/** checks if a value equals to the specified reference */
open class EqualsTo[A](a: A) extends FromPredicate[A](_ == a):
  override def formatErrorMessage(raw: A): String = 
    s"'$raw' doesn't equal $a"