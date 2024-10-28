package org.megogo

import org.megogo.model.SumOfTwoResult
import org.scalatest.funsuite.AnyFunSuite

class SumOfTwoTest extends AnyFunSuite {

  val sumOfTwo = new SumOfTwo()

  test("should find single pair that sums to target") {
    val data = Array(2, 7, 11, 15)
    val target = 9
    val expected = Array(
      SumOfTwoResult((0, 1), (2, 7))
    )

    val result = sumOfTwo(data, target)

    assert(result.length == 1)
    assert(result.contains(expected(0)))
  }

  test("should find multiple pairs that sum to target") {
    val data = Array(1, 2, 3, 4, 5)
    val target = 6
    val expected = Set(
      SumOfTwoResult((0, 4), (1, 5)),
      SumOfTwoResult((1, 3), (2, 4))
    )

    val result = sumOfTwo(data, target).toSet

    assert(result.size == 2)
    assert(result == expected)
  }

  test("should return empty array when no pairs sum to target") {
    val data = Array(1, 2, 3, 4)
    val target = 10

    val result = sumOfTwo(data, target)

    assert(result.isEmpty)
  }

  test("should handle negative numbers") {
    val data = Array(-2, -1, 0, 1, 2)
    val target = 0
    val expected = Set(
      SumOfTwoResult((0, 4), (-2, 2)),
      SumOfTwoResult((1, 3), (-1, 1))
    )

    val result = sumOfTwo(data, target).toSet

    assert(result.size == 2)
    assert(result == expected)
  }

  test("should handle array with two elements that sum to target") {
    val data = Array(3, 7)
    val target = 10
    val expected = Array(
      SumOfTwoResult((0, 1), (3, 7))
    )

    val result = sumOfTwo(data, target)

    assert(result.length == 1)
    assert(result.contains(expected(0)))
  }

  test("should handle empty array") {
    val data = Array.empty[Int]
    val target = 5

    val result = sumOfTwo(data, target)

    assert(result.isEmpty)
  }

  test("should handle array with single element") {
    val data = Array(5)
    val target = 5

    val result = sumOfTwo(data, target)

    assert(result.isEmpty)
  }
}