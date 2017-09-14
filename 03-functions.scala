/**
  * There are different ways to construct high order functions
  */
def f1 = {x: Int => x + 1}
def f2 = (x: Int) => x + 1
val f3 = (x: Int) => x + 1
val f4 = new Function1[Int, Int] {
 def apply(v1: Int): Int = v1 + 1
}
def f5(x: Int) = x + 1
def f6(x: Int): (Int => Int) = (y: Int) => x + y

// You can partially apply functions
def sum(a: Int, b: Int, c: Int) = a + b + c
def sum3 = sum _

def sumC = (sum _).curried
sumC(1)(2)

// You can create partial functions as well
val doubleEvents: PartialFunction[Int, Int] = {
 case x if(x % 2) == 0 => x * 2
}

val tripleOdds: PartialFunction[Int, Int] = {
 case x if (x%2) != 0 => x * 3
}

// You can also use andThen to chain
val doubleOrTriple = doubleEvents orElse tripleOdds
doubleOrTriple(3) // 9
doubleOrTriple(4) // 8

/**
  * Named and default parameters. You can call a method using the variable names.
  * This works well especially with default parameters
  */
def printName(first: String = "Default", last: String) =
  println(s"$first & $last")

printName("Bas", "Meesters")
printName(first = "Bas", last = "Meesters")
printName(last = "Meesters")

/**
  * - Infix, Prefix and Postfix operators Any method with a single parameter can
  *   be used as infix operator: `a.m(b)` == `a m b`
  * - Any method which does not require a parameter can be used as postfix
  *   operator: `a.m` == `a m`
  * - Prefix operators work when a method name starts with `unary_`
  */
class Stereo {
  def unary_+ = "on"
  def unary_- = "off"
}
val stereo = new Stereo
println(+stereo) // "on"

/**
  * Repeated parameters. Allows you to give as many parameters as needed. It is
  * always the last parameter
  */
def someMethod(z: Any*) = z.foreach(println(_))
someMethod(1, 2, 3)
