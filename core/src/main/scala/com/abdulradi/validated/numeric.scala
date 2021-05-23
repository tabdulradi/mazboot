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
package numeric

/** checks if a numeric value is greater than N */
class Greater[N](n: N)(using N: Numeric[N]) extends Validation[N]:
  override opaque type Valid <: N = N
  override type Error = ValidationError
  case class ValidationError(raw: N) extends Validation.Error:
    def message: String = s"$raw isn't greater than $n"
  
  final def validate(raw: N): Either[Error, Valid] = 
    if N.gt(raw, n) then Right(raw) else Left(ValidationError(raw))

class Positive[N](using N: Numeric[N]) extends Greater[N](N.zero)

object PositiveInt extends Positive[Int]
type PositiveInt = PositiveInt.Valid


// TODO:
//    Less[N]: checks if a numeric value is less than N
//    LessEqual[N]: checks if a numeric value is less than or equal to N
//    GreaterEqual[N]: checks if a numeric value is greater than or equal to N
//    Positive: checks if a numeric value is greater than zero
//    NonPositive: checks if a numeric value is zero or negative
//    Negative: checks if a numeric value is less than zero
//    NonNegative: checks if a numeric value is zero or positive
//    Interval.Open[L, H]: checks if a numeric value is in the interval (L, H)
//    Interval.OpenClosed[L, H]: checks if a numeric value is in the interval (L, H]
//    Interval.ClosedOpen[L, H]: checks if a numeric value is in the interval [L, H)
//    Interval.Closed[L, H]: checks if a numeric value is in the interval [L, H]
//    Modulo[N, O]: checks if an integral value modulo N is O
//    Divisible[N]: checks if an integral value is evenly divisible by N
//    NonDivisible[N]: checks if an integral value is not evenly divisible by N
//    Even: checks if an integral value is evenly divisible by 2
//    Odd: checks if an integral value is not evenly divisible by 2
//    NonNaN: checks if a floating-point number is not NaN