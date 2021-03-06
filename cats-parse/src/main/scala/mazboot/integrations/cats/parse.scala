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
package mazboot.integrations.cats

import mazboot.validations.*
import cats.parse.{Parser0, Parser}

object parse:
  extension [Raw](p: Parser[Raw]) def validateAs(v: Validation[Raw]): Parser[v.Valid] =  
    p.flatMap(raw => v.validate(raw).fold(e => Parser.failWith(e.message), Parser.pure))
