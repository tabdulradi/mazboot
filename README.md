# Validated

Companion to the Scala std lib, providing newtypes + validations. It has usefull types out of the box (like `PositiveInt` or `MatchesRegex`), as well as the ability to define your own custom validated types.

Example:
```scala
scala> import com.abdulradi.validated.types.strings.NonEmptyString

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
  |            Required: com.abdulradi.validated.types.strings.NonEmptyString
```

You can also make your own types
```scala
import com.abdulradi.validated.validations.strings.StartsWith
import com.abdulradi.validated.types.net.Ipv4

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

## Integrated with Happypath
Did you notice we have been calling `toEither` and `getOrThrow` on a union type? This functionality comes from [happypath](https://github.com/tabdulradi/happypath). Allowing union types to behave like Either/Try without any implicits imports at the use site.
```scala
import com.abdulradi.validated.types.ints.{GreaterThanOrEqualsOne, Positive}

val res = // ValidationError | Int
    for
      a <- GreaterThanOrEqualsOne.validate(1)
      b <- Positive.validate(1)
    yield a + b

Positive.validate(-1).fold(e => s"Error!! $e", n =>  s"Res = $n")
// val res2: String = Error!! com.abdulradi.validated.validations.Validation$Error: '-1' doesn't pass the predicate: greater than 0
```


## Acknowledgements
This library is inspired by [Refined](https://github.com/fthomas/refined) and tries to provide same functionality, but using Scala 3 constructs instead of relying on macros.