# Mazboot (previously called Validated)

Companion to the Scala std lib, providing newtypes + validations. It has usefull types out of the box (like `PositiveInt` or `MatchesRegex`), as well as the ability to define your own custom validated types.  
  
"mazboot" (مظبوط) is an Egyptian Arabic word that could mean "correct", "proper", or "valid".

Example:
```scala
scala> import mazboot.strings.NonEmptyString

scala> NonEmptyString.validate("") // Error messages out of the box                   
val res0: NonEmptyString.Error | NonEmptyString.Valid = NonEmptyString.Error: '' doesn't pass the predicate: not equals

scala> val input: NonEmptyString = NonEmptyString.validate("input-from-user").getOrThrow
val input: NonEmptyString.Valid = input-from-user

scala> val itIsAlsoString: String = input // isA relationship (subtyping)
val itIsAlsoString: String = input-from-user

scala> val butNotTheOtherWayAround: NonEmptyString = "lol" // Doesn't compile, must validate to get instance of NonEmptyString
1 |val butNotTheOtherWayAround: NonEmptyString = "lol"
  |                                              ^^^^^
  |            Found:    ("lol" : String)
  |            Required: mazboot.strings.NonEmptyString
```

You can also make your own types
```scala
import mazboot.validations.strings.StartsWith
import mazboot.net.Ipv4

val StartsWith127 = StartsWith("127")
type StartsWith127 = StartsWith127.Valid

val LocalHost = Ipv4 and StartsWith127 // Combine newtypes to make new ones
type LocalHost = LocalHost.Valid

val x: LocalHost = LocalHost.validate("127.0.0.1").getOrThrow

// isA relationships work as you expect
val mustBeIpV4: Ipv4 = x
val mustBeStartsWith: StartsWith127 = x

// Composes Error messages out of the box
println(LocalHost.validate("loll").toEither.left.map(_.getMessage))
// Left('loll' doesn't pass the predicate: match pattern '^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$' and start with '127')
```

Also, it works out of the box with Scala 3' Main methods syntax
```scala
import mazboot.net.{Ipv4, PortNumber}

@main def myCmdLineApp(host: Ipv4, port: PortNumber): Unit = 
  println(s"host = $host, port = $port")
```
```
$ sbt run 127.0.0.1 99999
Illegal command line after first argument: java.lang.IllegalArgumentException: '99999' doesn't pass the predicate: greater than 0 or equals 0 and less than 65535 or equals 65535
```




## Getting started

Add the following to your build.sbt

```scala
libraryDependencies += "com.abdulradi" %% "mazboot-types" % "0.4.0"
```
## Integrations 

### Happypath

Did you notice we have been calling `toEither` and `getOrThrow` on a union type? This functionality comes from [happypath](https://github.com/tabdulradi/happypath). Allowing union types to behave like Either/Try without any implicits imports at the use site.
```scala
import mazboot.ints.{GreaterThanOrEqualsOne, Positive}

val res = // ValidationError | Int
    for
      a <- GreaterThanOrEqualsOne.validate(1)
      b <- Positive.validate(1)
    yield a + b

Positive.validate(-1).fold(e => s"Error!! $e", n =>  s"Res = $n")
// val res2: String = Error!! mazboot.validations.Validation$Error: '-1' doesn't pass the predicate: greater than 0
```

Note: this integration is part of the core module, so nothing needs to be added to build.sbt

###  Cats Parse

Add this your build.sbt

```scala
libraryDependencies += "com.abdulradi" %% "mazboot-cats-parse" % "0.4.0"
```

This module will allow you to easily extend cats parsers with a validation step

```scala
import cats.parse.Parser
import mazboot.cats.parse.syntax.*
import mazboot.net.*

val parser = Parser.anyChar.rep.string.validateAs(Ipv4)
val a: Ipv4 = parser.parse("127.0.0.1").fold(_ => ???, _._2)
parser.parse("lol").fold(e => println(e.expected), _ => ???) 
// Error(3,NonEmptyList(FailWith(3,'lol' doesn't pass the predicate: match pattern '^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$')))
```

###  Ciris

Add this your build.sbt

```scala
libraryDependencies += "com.abdulradi" %% "mazboot-ciris" % "0.4.0"
```

This module provides `ConfigDecoder` instance for all Validated types

```scala
import mazboot.net.*
import mazboot.ciris.given
import cats.effect.*
import cats.implicits.*
import ciris.*

case class Config(host: Ipv4, port: PortNumber)

object App extends IOApp.Simple:
  val run = 
    (
      env("HOST").as[Ipv4],
      env("PORT").as[PortNumber]
    ).parMapN(Config.apply).load[IO].flatMap(IO.println)
```

## Acknowledgements

This library is inspired by [Refined](https://github.com/fthomas/refined) and tries to provide similar functionality using Scala 3 constructs.