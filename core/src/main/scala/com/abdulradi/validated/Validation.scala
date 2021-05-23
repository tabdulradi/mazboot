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
package com.abdulradi.validated

object Validation:
  trait Error:
    def message: String

  type Aux[Raw, V] = Validation[Raw] { type Valid = V }

trait Validation[_Raw]:
  type Raw = _Raw
  opaque type Valid <: Raw = Raw
  type Error <: Validation.Error

  def validate(raw: Raw): Either[Error, Valid]

class And[_Raw, _Valid1 <: _Raw, _Valid2 <: _Raw](val first: Validation.Aux[_Raw, _Valid1], val second: Validation.Aux[_Raw, _Valid2]) extends Validation[_Raw]:
  override type Raw = _Raw
  override type Valid = _Valid1 & _Valid2
  override type Error = first.Error | second.Error

  override def validate(raw: Raw): Either[Error, Valid] =
    first.validate(raw).flatMap(x => second.validate(x).asInstanceOf[Either[second.Error, Valid]])

case class BothErrors[E1 <: Validation.Error, E2 <: Validation.Error](e1: E1, e2: E2) extends Validation.Error:
  def message = s"${e1.message} & ${e2.message}"

class Or[_Raw, _Valid1 <: _Raw, _Valid2 <: _Raw](val first: Validation.Aux[_Raw, _Valid1], val second: Validation.Aux[_Raw, _Valid2]) extends Validation[_Raw]:
  override type Raw = _Raw
  override type Valid = _Valid1 | _Valid2
  override type Error = BothErrors[first.Error, second.Error]

  override def validate(raw: Raw): Either[Error, Valid] =
    first.validate(raw).left.flatMap { error1 =>
      second.validate(raw).left.map(error2 => BothErrors(error1, error2))
    }

// class Not[_Raw, _Valid <: _Raw](val validation: ValidationAux[_Raw, _Valid]) extends Validation[_Raw]:
//   override opaque type Valid <: Raw = Raw
//   override type Error = BothErrors[first.Error, second.Error]
//   override def validate(raw: Raw): Either[Error, Valid] =
//     validation.validate(raw).swap.map(_ => raw).left.map(raw => ???)