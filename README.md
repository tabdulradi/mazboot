# Validated

Companion to the Scala std lib, providing useful subtypes like `PositiveInt` or `MatchesRegex` as well as the ability to define custom validations.
Example:
```scala
val a: Ipv4 = Ipv4.validate("127.0.0.1").right.get // Ipv4 is convienent type that ensures a valid value
val b: String = Ipv4.validate("127.0.0.1").right.get // Ipv4 isA String
// val c: Ipv4 = "127.0.0.1" // Won't compile. Ipv4 is seperate type different from String. Values must be validated, no way to cheat.
```

## Acknowledgements
This library is inspired by [Refined](https://github.com/fthomas/refined) and tries to provide same functionality, but without the compile-time validations.