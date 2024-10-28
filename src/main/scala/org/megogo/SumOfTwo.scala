package org.megogo

import org.megogo.model.SumOfTwoResult

class SumOfTwo {
  def apply(data: Array[Int], target: Int): Array[SumOfTwoResult] = {
    val indexedArr = data.zipWithIndex
    val sortedArr = indexedArr.sortBy(_._1)

    @scala.annotation.tailrec
    def findPairsRec(left: Int, right: Int, acc: List[SumOfTwoResult]): List[SumOfTwoResult] = {
      if (left >= right) acc
      else {
        val sum = sortedArr(left)._1 + sortedArr(right)._1

        if (sum == target) {
          val pair = SumOfTwoResult((sortedArr(left)._2, sortedArr(right)._2), (sortedArr(left)._1, sortedArr(right)._1))
          findPairsRec(left + 1, right - 1, pair :: acc)
        } else if (sum < target) {
          findPairsRec(left + 1, right, acc)
        } else {
          findPairsRec(left, right - 1, acc)
        }
      }
    }

    findPairsRec(0, sortedArr.length - 1, List.empty).toArray
  }
}
