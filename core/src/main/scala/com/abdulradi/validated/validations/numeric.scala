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
package numeric
import math.Numeric.Implicits.*
import math.Ordering.Implicits.*

/** checks if a numeric value is greater than N */
open class GreaterThan[N: Numeric](n: N) extends FromPredicate[N](_ > n, s"greater than $n")

/** checks if a numeric value is positive */
open class Positive[N](using N: Numeric[N]) extends GreaterThan[N](N.zero)

/** checks if a numeric value is greater than zero */
open class GreaterThanZero[N](using N: Numeric[N]) extends GreaterThan[N](N.zero)

/** checks if a numeric value is greater than one */
open class GreaterThanOne[N](using N: Numeric[N]) extends GreaterThan[N](N.one)

/** checks if a numeric value equals to 0 */
open class EqualsZero[N](using N: Numeric[N]) extends EqualsTo(N.zero)

/** checks if a numeric value equals to 1 */
open class EqualsOne[N](using N: Numeric[N]) extends EqualsTo(N.one)

/** checks if an number is even (divisible by 2) */
open class Even[N](n: N)(using N: Numeric[N]) extends FromPredicate[N](_.toDouble % 2 == 0, "even")

/** checks if an number is odd (non divisible by 2) */
open class Odd[N](n: N)(using N: Numeric[N]) extends FromPredicate[N](N.eq(_, n), "odd")

// TODO: Feature parity with Refined
//    Less[N]: checks if a numeric value is less than N
//    LessOrEqual[N]: checks if a numeric value is less than or equal to N

//    Interval.Open[L, H]: checks if a numeric value is in the interval (L, H)
//    Interval.OpenClosed[L, H]: checks if a numeric value is in the interval (L, H]
//    Interval.ClosedOpen[L, H]: checks if a numeric value is in the interval [L, H)
//    Interval.Closed[L, H]: checks if a numeric value is in the interval [L, H]
//    Modulo[N, O]: checks if an integral value modulo N is O
//    Divisible[N]: checks if an integral value is evenly divisible by N
//    NonDivisible[N]: checks if an integral value is not evenly divisible by N
//    NonNaN: checks if a floating-point number is not NaN

class NumericTypes[N: Numeric]:
  val Positive = numeric.Positive[N]
  type Positive = Positive.Valid

  val GreaterThanOne = numeric.GreaterThanOne[N]
  type GreaterThanOne = GreaterThanOne.Valid

  val EqualsZero = numeric.EqualsZero[N]
  type EqualsZero = EqualsZero.Valid

  val EqualsOne = numeric.EqualsOne[N]
  type EqualsOne = EqualsOne.Valid

  val GreaterThanOrEqualsZero = Positive or EqualsZero
  type GreaterThanOrEqualsZero = GreaterThanOrEqualsZero.Valid

  val GreaterThanOrEqualsOne = {
    val _0 = GreaterThanOne or EqualsOne
    _0 and Positive
  }
  type GreaterThanOrEqualsOne = GreaterThanOrEqualsOne.Valid

// TODO: Feature parity with Refined
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



