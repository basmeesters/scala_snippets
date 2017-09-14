// Helper function
def shouldEqual(given: Any, expected: Any) {
  println(s"${given} should be ${expected}")
}

/**
  * Lists are immutable and always contain items of the same type `eq` tests
  * identity (same object) and `==` tests equlity (same content).
  */
val l = List(1,2,3)
shouldEqual(l.headOption, Some(1))
shouldEqual(l(0), 1)
shouldEqual(l.map {v => v * 2}, List(2,4,6))

/**
  * Sets are like lists but 'unordered', do not contain duplicates, and can have
  * different types.
  */
val s1 = Set(1,2,3)
val s2 = Set(2,3,4)

shouldEqual(s1 intersect s2, Set(2,3))
shouldEqual(s1 union s2, Set(1,2,3,4))
shouldEqual(s1 diff s2, Set(1))

/**
  * Maps are keys to values pairs, you can use both `(key, value)` as `key ->
  * value`. Maps are also immutable by default.
  */
val m = Map("k1" -> 1, "k2" -> 2)

// Common functions
shouldEqual(m.contains("k1"), true)
shouldEqual(m.size, 2)
shouldEqual(m.keys, Set("k1", "k2"))
shouldEqual(m.values, Set(1, 2)) // MapLike?
shouldEqual(m("k1"), 1)
shouldEqual(m.getOrElse("k1", 0), 1)
shouldEqual(m - "k1", Map("k2" -> 2))

/**
  * Ranges, e.g. `Range(0, 10)`. You can access by position again `r(0)`. Ranges
  * do not include there last number by default You can create them in different
  * ways. r1 == r2 == r3
  */
val r = 1 to 10
val r2 = Range(1, 11)
val r3 = 1 until 11
val r4 = Range(0, 10, 2)
println(r)
println(r2)
println(r3)
println(r4)
println(r.toList)

// TODO: Scala collection hierarchy
