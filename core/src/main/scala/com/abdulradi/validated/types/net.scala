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
package com.abdulradi.validated.types
package net

import com.abdulradi.validated.validations.strings.*

object Ipv4 extends MatchesRegex("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")
type Ipv4 = Ipv4.Valid

object Ipv6 extends MatchesRegex("^([a-f0-9:]+:+)+[a-f0-9]+$")
type Ipv6 = Ipv6.Valid
