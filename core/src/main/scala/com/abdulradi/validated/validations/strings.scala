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
package strings

/** checks if a String matches the specified regular expression */
open class MatchesRegex(regex: String) extends FromPredicate[String](_.matches(regex), s"match pattern '$regex'")

/** checks if a String starts with the specified prefix */
open class StartsWith(prefix: String) extends FromPredicate[String](_.startsWith(prefix), s"start with '$prefix'")

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