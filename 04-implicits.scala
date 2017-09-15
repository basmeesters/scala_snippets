/**
  * Implicit parameters: where if parameters are not defined they are searched
  * in appropriate scope. You implicitly ask the compiler to fill in the missing
  * pieces. You can use `val`, `vars` or `defs` for implicit parameters as long
  * as the types are correct. If you have multiple values in scope which could
  * be used the compiler will give an error.

  * Example use is a logger where it will write to std_out by default. This way
  * you can have dependency injection without the need to provide constructor
  * parameters. It is similar to default value, but the default value can be
  * different in different contexts throughout the code, instead of standardly
  * set by the callee. Note that it is quite dangerous to use String or Int as
  * they can accidentally match.
  */

// Standard
def example1(implicit x: String) = println(x)

// Both are implicit
def example2(implicit x: String, y: Int) = println(s"$x and $y")

// Won't compile, implicit has to be first
// def example3(x: String, implicit y: String) = ...

// Multiple parameter list
def example4(x: String)(implicit y: Int) = println(s"$x and $y")

// Won't compile, you can only have one implicit parameter list
// def example5(implicit x: String)(implicit y: Int) = println(s"$x and $y")

implicit val implX: String = "Implicit X"
implicit val implY: Int = 10

// Putting parentheses behind it will not work..
// example() would fail to compile

example1 // Implicit X
example1("Explicit X") // Explicit X

// Will also fail to compile
// example2("Explicit X")

example2 // Implicit X and 10
example2("Explicit X", 5) // Explicit X and 5

example4("Has to be explicit")
example4("Has to be explicit")(5)

/**
  * Implicit conversion: functions that are automatically called if the code
  * otherwise would not compile. They are typically used to create implicit
  * conversions from one type to another.
  *
  * Here an example with numbers, rationals, and complex numbers which need to
  * be added together.
  */
sealed trait Num
case class Simple(x: Int) extends Num {
  def +(other: Simple) = Simple(x + other.x)
}
case class Complex(x: Int, i: Int) extends Num {
  def +(other: Complex) = Complex(x + x, i + i)
}
case class Rational(x: Int, y: Int) extends Num {
  val g  = gcd(x, y)
  val numer = x / g
  val denom = y / g

  def +(that: Rational) =
    new Rational(that.numer * denom + numer * that.denom, denom * that.denom)

  override def toString() = s"Rational($numer, $denom)"

  private def gcd(a: Int, b: Int):Int=if (b==0) a else gcd(b, a%b)
}

println(Rational(1, 2) + Rational(1, 4))

// Won't compile
// println(Rational(1, 2) + Simple(1))
import scala.language.implicitConversions // avoid compiler warning
implicit def toRat(s: Simple): Rational = Rational(s.x, 1)

// Now it compiles because of automatic conversion
println(Rational(1, 2) + Simple(1))

/**
  * Implicit classes: can be used to extend existing classes with new
  * functionality.
  */
implicit class RichInt(x: Int) {
  def times[A](f: => A): Unit = {
    def loop(current: Int): Unit =
      if(current > 0) {
        f
        loop(current - 1)
      }
    loop(x)
  }
}
3.times(println("It works!"))

/**
  * Type classes: create ad-hoc polymorphism in Scala. It consists of three
  * parts:
  * - The type class itself, a trait defining the functionality that needs to
  *   to be adhered to.
  * - Instantiations of the type class for data types you want to support.
  * - An interface to the outside world.
  */

// The type class
trait Number[T] {
  def plus(x: T, y: T): T
}

// Instantiations for double and int
object Number {
  implicit val double = new Number[Double] {
    def plus(x: Double, y: Double) = x + y
  }

  implicit val int = new Number[Int] {
    def plus(x: Int, y: Int) = x + y
  }
}

//
object Statistics {
  import Number._
  def sum[T](xs: List[T])(implicit ev: Number[T]): T =
    xs.reduce(ev.plus(_, _))
}
println(Statistics.sum(List[Double](1, 2, 3)))
println(Statistics.sum(List[Int](1, 2, 3)))
