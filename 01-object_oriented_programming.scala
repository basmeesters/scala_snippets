/**
  * Creating a class with a method (optionally)
  */
class Point(x: Int, y: Int) {
 override def toString(): String = s"Some point($x, $y)"
}
val point = new Point(1,1)

// Will automatically call the `toString` function
println(point)

/**
  * Objects are singletons. Often they are used as companion objects to
  * non-singleton objects. Companion object can also see private values and
  * variables of the corresponding classes.
  */
object Greeting {
  def hello(x: String): String= s"Hello $x!"
}
println(Greeting.hello("Bas"))

/**
  * Are like regular classes which export their constructor parameters and
  * provide recursive decomposition via pattern matching. equals, toString, and
  * hashCode are automatically generated. You can have default and named
  * parameters
  */
sealed abstract class Term
case class Var(name: String) extends Term
case class Fun(arg: String, body: Term) extends Term
case class App(f: Term, v: Term) extends Term

// Example
val t = Fun("x", Fun("y", App(Var("a"), Var("b"))))

// Pattern matching with case classes help destructure them
def printTerm(term: Term): Unit = term match {
  case Fun(x, b) => print(s"$x."); printTerm(b)
  case Var(n) => print(n)
  case App(f, v) => printTerm(f); print(" + "); printTerm(v)
}

printTerm(t)

// Access properties
val v = Var("hello")
println(v.name)

// Create mutable properties
case class Dog(var name: String)

// You can also copy an immutable case clss
val d = Dog("Hond")
val d2 = d.copy(name = "Hond2")

/**
  * Similar to interfaces in other languages, traits are used to define object
  * types by specifying the signature of the supported methods. traits can be
  * partially implemented. Traits may not have constructor parameters
  */
trait Similarity {
  def isSimilar(x: Any): Boolean
  def isNotSimilar(x: Any): Boolean = !isSimilar(x)
}

class MyNumber(y: Int) extends Similarity {
  def isSimilar(x: Any): Boolean = x == y
}

val num = new MyNumber(10)
println(num.isSimilar(11))
