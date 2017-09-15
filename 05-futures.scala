/**
  * Futures provide a way to reason about performing many operations in
  * parallel, in an efficient and non-blocking way. A future is a placeholder
  * object for a value that may not yet exist.
  *
  * Futures and promises revolve around execution contexts, responsible for
  * actually executing the computations. The scala.concurrent package comes out
  * of the box with an ExecutionContext.

  * Futures are - once received a value or exception - immutable.
  *
  * You can have multiple callbacks on the same Future. They are however
  * non-deterministically executed, so it is best to avoid side-effects.
  * Callbacks do not have a return value, so they cannot be chained in the
  * traditional sense.
  */
import scala.concurrent._
import scala.util.{Success, Failure}
import scala.concurrent.duration._
import scala.concurrent.{ Future, Promise }
import scala.concurrent.ExecutionContext.Implicits.global

println("create future")
def sleep(time: Int) = Thread.sleep(time)
val exampleFuture: Future[String] = Future {
  sleep(1000)
  "Done computing.."
}


// Create callbacks to do something useful with the result.
exampleFuture.onComplete { // onComplete handles both failure and success
  case Success(s) => println(s"Result of future1: $s")
  case Failure(t) => println("An error has occured: " + t.getMessage)
}

// Block on result
Await.result(exampleFuture, 3 second)

// Non-blocking future
val exampleFuture2: Future[String] = Future {
  sleep(500)
  "Done!"
}

exampleFuture2.onSuccess{case s => println(s)}
println("Waiting.."); sleep(150)
println("Waiting.."); sleep(150)
println("Waiting.."); sleep(150)
println("Waiting.."); sleep(150)
println("Waiting.."); sleep(150)
println("Waiting.."); sleep(150)

// To chain futures you can use `map`, `foreach`, or a `for` comprehension
val exampleFuture3: Future[String] =  Future {
  sleep(500)
  "Chaining has started"
}

val exampleFuture4: Future[String] = exampleFuture3.map { s =>
  println(s"Result: $s")
  sleep(500)
  "Chaining is finished"
}

exampleFuture4.map(s => println(s))

/**
  * Another example using for comprehension, the futures will only be created
  * after the first is finished running. Create the futures outside the for
  * comprehension to run them in parallel.
  */
for {
  test1 <- Future { sleep(500); "Test1"}
  test2 <- Future { sleep(100); "Test2"}
} yield println(s"$test1 $test2")

/**
  * Promises
  *
  * Futures are defined as a type of read-only placeholder object created for a
  * result which doesn't exist yet. Promises are a writable, single-assignment
  * container, which completes a future.
  */
sleep(2000)
val p = Promise[String]()
val f = p.future

val producer = Future {
  sleep(500)
  val r = "Some result"
  p.success(r)
  sleep(500)
}

val consumer = Future {
  sleep(500)
  f.onSuccess {
    case r => println(s"Promise result: $r")
  }
}
sleep(1000)

/**
  * You can not complete a Future.
  *
  * A Future is supposed to be a computation and this computation (once started)
  * completes when it does. You have no control over it after creating it. You
  * can assign onComplete callback to it which will be fired when this future
  * completes but you have no control over when it will.
  *
  * If you want to have a Future whose completion you can control, you can
  * Promise that Future like a politician. Now whether you complete that future
  * with success or failure is up to you.
  */
  // lets promise an Int
val promise = Promise[Int]()
val promise2 = Promise[Int]()

// Now... we also have a future associated with this promise
val future = promise.future
val future2 = promise2.future


// assign a callback which will be called when future completes
future.onComplete {
  case Success(i) => println("Future complete: " + i)
  case Failure(ex) => println("Future failed: ")
}

future2.onComplete {
  case Success(i) => println("Future complete: " + i)
  case Failure(ex) => println("Future failed: " + ex.toString)
}

// Future by itself provides you no way to complete it

// if you want to complete this future, you can fulfil your promise
promise.complete(Success(10))
promise2.complete(Failure(new Exception("some failure")))
sleep(1000)
