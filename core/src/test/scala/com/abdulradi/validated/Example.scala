import com.abdulradi.validated.validations.*
import com.abdulradi.validated.validations.strings.*
import com.abdulradi.validated.types.ints.*
import com.abdulradi.validated.types.net.*

object Ipv4Or6 extends FromPredicate.Or(Ipv4, Ipv6)
type Ipv4Or6 = Ipv4Or6.Valid

// object NotIpv6 extends Not(Ipv6)
// type NotIpv6 = NotIpv6.Valid

object StartsWith127 extends StartsWith("127")
type StartsWith127 = StartsWith127.Valid

object LocalHost extends FromPredicate.And(Ipv4, StartsWith127)
type LocalHost = LocalHost.Valid

object NotLocalHost extends FromPredicate.Not(LocalHost)
type NotLocalHost = NotLocalHost.Valid

@main def strings(): Unit = 
  val a: String = Ipv4.validateEither("127.0.0.1").fold(_ => ???, identity)
  val b: Ipv4 = Ipv4.validateEither("127.0.0.1").fold(_ => ???, identity)
  val c: LocalHost = LocalHost.validateEither("127.0.0.1").fold(_ => ???, identity)
  val d: Ipv4 = c
  val e: StartsWith127 = c
  val f: Ipv4Or6 = c
  
  println(LocalHost.validateEither("loll").left.map(_.getMessage))
  println(NotLocalHost.validateEither("127.0.0.1").left.map(_.getMessage))
  println(Ipv4Or6.validateEither("128").left.map(_.getMessage))
  println(f)


@main def numeric(): Unit = 
  val x: GreaterThanOrEqualsOne = GreaterThanOrEqualsOne.validateEither(1).fold(throw _, identity)
  val itMustBePositive: Positive = x
  val itMustBeGraterOrEqualsZero: GreaterThanOrEqualsZero = x
  val itMustBeInt: Int = x

  // val y: GreaterThanOrEqualsZero = GreaterThanOrEqualsZero.validateEither(1).fold(throw _, identity)
  // val yIsGreaterOne: GreaterThanOrEqualsOne = y // Won't compile
  
  println(x)