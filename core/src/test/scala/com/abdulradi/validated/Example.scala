import com.abdulradi.validated.validations.*
import com.abdulradi.validated.validations.strings.*
import com.abdulradi.validated.types.ints.*
import com.abdulradi.validated.types.net.*
import com.abdulradi.validated.types.strings.*
import com.abdulradi.validated.ValidationError

@main def netTest(): Unit = 
  val a: String = Ipv4.validate("127.0.0.1").getOrThrow
  val b: Ipv4 = Ipv4.validate("127.0.0.1").getOrThrow

@main def andTest(): Unit = 
  val StartsWith127 = StartsWith("127")
  type StartsWith127 = StartsWith127.Valid
  val LocalHost = Ipv4 and StartsWith127
  type LocalHost = LocalHost.Valid

  val x: LocalHost = LocalHost.validate("127.0.0.1").getOrThrow
  val mustBeIpV4: Ipv4 = x
  val mustBeStartsWith: StartsWith127 = x
  
  println(LocalHost.validate("loll").toEither.left.map(_.getMessage))

@main def orTest(): Unit = 
  val Ipv4Or6 = Ipv4 or Ipv6 // FromPredicate.Or(Ipv4, Ipv6)
  type Ipv4Or6 = Ipv4Or6.Valid
  
  println(Ipv4Or6.validate("128").mapError(_.getMessage))
  // val g: Ipv4Or6 = Ipv4Or6.validate("128").getOrThrow // FIXME: getOrThrow exposes the Raw type in Or types (but not And). Probably due to the use of inline

  val ipv4Or6: Ipv4Or6 = Ipv4Or6.validateEither("127.0.0.1").right.get // OrThrow
  // val ipv4Or6MightNotBeIpv4: Ipv4 = ipv4Or6 // Shouldn't compile
  val ipv4Or6MightNotBeIpv4: Ipv4 = Ipv4.validate(ipv4Or6).getOrThrow

  val ipv4 = Ipv4.validate("127.0.0.1").getOrThrow
  val ipv4MustBeIpv4Or6: Ipv4Or6 = ipv4

@main def negationTest(): Unit = 
  val x = NonEmptyString.validate("foo").getOrThrow
  val y: String = x
  // val z: EmptyString = x // Must not compile
  NonEmptyString.validate("").toEither.left.get

@main def doubleNegationTest(): Unit =
  val NotNonEmptyString = NonEmptyString.negate
  type NotNonEmptyString = NotNonEmptyString.Valid
  val a = NotNonEmptyString.validate("").getOrThrow
  // val b: EmptyString = a // Would be nice if this work
  NotNonEmptyString.validate("lol").toEither.left.get
  
@main def numericTest(): Unit = 
  val x: GreaterThanOrEqualsOne = GreaterThanOrEqualsOne.validateEither(1).fold(throw _, identity)
  val xMustBePositive: Positive = x
  val xMustBeGraterOrEqualsZero: GreaterThanOrEqualsZero = x
  val xMustBeInt: Int = x

  val y: GreaterThanOrEqualsZero = GreaterThanOrEqualsZero.validateEither(1).fold(throw _, identity)
  // val yIsGreaterOne: GreaterThanOrEqualsOne = y // Shouldn't compile

  val z: Positive = Positive.validate(1).getOrThrow
  val zMustBeGreaterOrEqualsZero: GreaterThanOrEqualsZero = z
  val zMustBeInt: Int = z
  
  println(z)

@main def monadicSyntaxTest(): Unit = 
  val res = // : ValidationError | Int
    for
      a <- GreaterThanOrEqualsOne.validate(1)
      b <- Positive.validate(-1)
    yield a + b

  res match // TODO: Would be nice to get rid of Exhaustivity warnings
    case GreaterThanOrEqualsOne.Error(raw) => println(s"$raw caused GreaterThanOrEqualsOne.Error")
    case Positive.Error(raw) => println(s"$raw caused caused Positive.Error")
    case n: Int => println(s"Successs: $n")

  res.mapError(println)

  // val e = GreaterThanOrEqualsOne.validate(-1).
  
  println("Res = " + res)