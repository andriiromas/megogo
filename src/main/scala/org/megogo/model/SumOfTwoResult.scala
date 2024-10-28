package org.megogo.model

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}

case class SumOfTwoResult(indexes: (Int, Int), values: (Int, Int))

object SumOfTwoResult {
  implicit val encoder: Encoder[SumOfTwoResult] = (a: SumOfTwoResult) => Json.obj(
    "indexes" -> a.indexes.asJson,
    "values" -> a.values.asJson
  )
}