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
package mazboot.integrations.ciris

import mazboot.validations.*
import ciris.*


given [Raw, Valid <: Raw](using decoder: ConfigDecoder[String, Raw], v: ValidationOf.Aux[Valid, Raw]): ConfigDecoder[String, Valid] =
  decoder.mapEither { (maybeKey, raw) => 
    v.validation.validateWith(
      raw, 
      valid => 
        Right(valid),
      e => 
        val prefix = maybeKey.map(key => s"At $key: ").getOrElse("")
        Left(ConfigError(prefix + e.message))
    )
  }
