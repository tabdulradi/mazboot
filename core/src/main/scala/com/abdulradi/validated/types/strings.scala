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
package com.abdulradi.validated.types.strings

import com.abdulradi.validated.validations.*, strings.*

object EmptyString extends EqualsTo("")
type EmptyString = EmptyString.Valid

object NonEmptyString extends FromPredicate.Not(EmptyString)
type NonEmptyString = NonEmptyString.Valid

// TODO: Feature parity with Refined https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala-3.0-/eu/timepit/refined/types/string.scala
