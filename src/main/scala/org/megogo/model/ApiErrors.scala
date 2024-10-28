package org.megogo.model

import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, HCursor}

object ApiErrors {
  sealed trait ApiError
  case class TooManyRequests(message: String) extends ApiError
  object TooManyRequests {
    implicit val encoder: Encoder[TooManyRequests] = (response: TooManyRequests) => response.message.asJson
    implicit val decoder: Decoder[TooManyRequests] = (c: HCursor) => ???
  }
}
