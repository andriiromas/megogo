package org.megogo.model

import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, HCursor}

case class FindResponse(pairs: Array[SumOfTwoResult])

object FindResponse {
  implicit val encoder: Encoder[FindResponse] = (response: FindResponse) => response.pairs.asJson
  implicit val decoder: Decoder[FindResponse] = (c: HCursor) => ???
}
