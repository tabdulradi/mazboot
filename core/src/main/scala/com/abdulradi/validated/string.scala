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
package strings

/** checks if a String matches the specified regular expression */
class MatchesRegex(regex: String) extends Validation[String]:
  override opaque type Valid <: String = String
  override type Error = ValidationError
  case class ValidationError(raw: String) extends Validation.Error {
    def message: String = s"'$raw' doesn't match pattern '$regex'"
  }
  
  final def validate(raw: String): Either[Error, Valid] = 
    if raw.matches(regex) then Right(raw) else Left(ValidationError(raw))

/** checks if a String starts with the specified prefix */
class StartsWith(prefix: String) extends Validation[String]:
  override opaque type Valid <: String = String
  override type Error = ValidationError
  case class ValidationError(raw: Raw) extends Validation.Error:
    def message: String = s"'$raw' doesn't start with '$prefix'"
  
  final def validate(raw: String): Either[Error, Valid] = 
    if raw.startsWith(prefix) then Right(raw) else Left(ValidationError(raw))

object Ipv4 extends MatchesRegex("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")
type Ipv4 = Ipv4.Valid

object Ipv6 extends MatchesRegex("^([a-f0-9:]+:+)+[a-f0-9]+$")
type Ipv6 = Ipv6.Valid

// TODO:
//    EndsWith[S]: checks if a String ends with the suffix S
//    Regex: checks if a String is a valid regular expression
//    Uri: checks if a String is a valid URI
//    Url: checks if a String is a valid URL
//    Uuid: checks if a String is a valid UUID
//    ValidByte: checks if a String is a parsable Byte
//    ValidShort: checks if a String is a parsable Short
//    ValidInt: checks if a String is a parsable Int
//    ValidLong: checks if a String is a parsable Long
//    ValidFloat: checks if a String is a parsable Float
//    ValidDouble: checks if a String is a parsable Double
//    ValidBigInt: checks if a String is a parsable BigInt
//    ValidBigDecimal: checks if a String is a parsable BigDecimal
//    Xml: checks if a String is well-formed XML
//    XPath: checks if a String is a valid XPath expression
//    Trimmed: checks if a String has no leading or trailing whitespace
//    HexStringSpec: checks if a String represents a hexadecimal number
