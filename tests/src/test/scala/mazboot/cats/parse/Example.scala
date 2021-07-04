/*
 * Copyright 2021 Tamer Abdulradi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
package mazboot.tests.integrations.cats.parse

import cats.parse.Parser
import mazboot.integrations.cats.parse.*
import mazboot.net.*

@main def parserTest(): Unit = 
  val parser = Parser.anyChar.rep.string.validateAs(Ipv4)
  val a: Ipv4 = parser.parse("127.0.0.1").fold(_ => ???, _._2)
  parser.parse("lol").fold(e => println(e.expected), _ => ???)
