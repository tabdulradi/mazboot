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
package com.abdulradi.validated.validations

trait Validation[_Raw]:
  outer =>

  type Raw = _Raw
  opaque type Valid <: Raw = Raw

  class ValidationError(raw: Raw) extends Validation.BaseValidationError(outer.formatErrorMessage(raw)):
    override def toString: String = s"${outer.getClass.getSimpleName}.ValidationError($getMessage)"

  def validateEither(raw: Raw): Either[ValidationError, Valid]
  def validate(raw: Raw): ValidationError | Valid
  protected def formatErrorMessage(raw: Raw): String    

object Validation:
  abstract class BaseValidationError(val message: String) extends Exception(message)

  // type Aux[Raw, V] = Validation[Raw] { type Valid = V }

abstract class FromPredicate[_Raw](
  val predicate: _Raw => Boolean, 
  val predicateName: String) extends Validation[_Raw]:

  override opaque type Valid <: Raw = Raw

  final def validate(raw: Raw): ValidationError | Valid = 
    if predicate(raw) then raw else ValidationError(raw)

  final def validateEither(raw: Raw): Either[ValidationError, Valid] = 
    if predicate(raw) then Right(raw) else Left(ValidationError(raw))

  protected def formatErrorMessage(raw: Raw): String = 
    s"'$raw' doesn't pass the predicate: $predicateName"

object FromPredicate:
  type Aux[Raw, V] = FromPredicate[Raw] { type Valid = V }
  def aux[Raw, V <: Raw](predicate: Raw => Boolean, predicateName: String): Aux[Raw, V] = 
    new FromPredicate[Raw](predicate, predicateName) {
      override type Valid = V
    }

  open class And[_Raw, _Valid1 <: _Raw, _Valid2 <: _Raw](val first: Aux[_Raw, _Valid1], val second: Aux[_Raw, _Valid2]) extends FromPredicate[_Raw](
    raw => first.predicate(raw) && second.predicate(raw),
    s"${first.predicateName} and ${second.predicateName}"
  ):
    override type Valid = _Valid1 & _Valid2
    override protected def formatErrorMessage(raw: Raw): String = 
      if first.predicate(raw) then second.formatErrorMessage(raw) else first.formatErrorMessage(raw)

  open class Or[_Raw, _Valid1 <: _Raw, _Valid2 <: _Raw](val first: Aux[_Raw, _Valid1], val second: Aux[_Raw, _Valid2]) extends FromPredicate[_Raw](
    raw => first.predicate(raw) || second.predicate(raw),
    s"${first.predicateName} or ${second.predicateName}"
  ):
    override type Valid = _Valid1 | _Valid2
     override protected def formatErrorMessage(raw: Raw): String = 
      s"'$raw' doesn't pass any of the predicates: $predicateName"

  open class Not[_Raw, _Valid <: _Raw](val validation: Aux[_Raw, _Valid]) extends FromPredicate[_Raw](
    raw => ! validation.predicate(raw),
    s"not ${validation.predicateName}"
  ):
    override protected def formatErrorMessage(raw: Raw): String = 
      s"'$raw' unexpectedly passes the predicate: ${validation.predicateName}"

/** checks if a value equals to the specified reference */
open class EqualsTo[A](a: A) extends FromPredicate[A](_ == a, s"equals $a")