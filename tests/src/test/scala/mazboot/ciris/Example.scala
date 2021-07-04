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
package mazboot.tests.integrations.ciris

import mazboot.net.*
import mazboot.integrations.ciris.given
import cats.effect.*
import cats.implicits.*
import ciris.*

case class Config(host: Ipv4, port: PortNumber)
  
// HOST=127.0.0.1 PORT=80 sbt ciris/test:run
// HOST=lol PORT=80 sbt ciris/test:run
// HOST=127.0.0.1 PORT=999999 sbt ciris/test:run
object ConfigTest extends IOApp.Simple:
  val run = 
    (
      env("HOST").as[Ipv4],
      env("PORT").as[PortNumber]
    ).parMapN(Config.apply).load[IO].flatMap(IO.println)