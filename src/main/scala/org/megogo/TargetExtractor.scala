package org.megogo

import cats.effect.IO
import io.circe.Json
import org.http4s.Uri
import org.http4s.circe._
import org.http4s.client.Client

class TargetExtractor(client: Client[IO], defaultTarget: String) {
  def apply(targetOpt: Option[Int]): IO[String] = {
    val baseUri = Uri.unsafeFromString("https://httpbin.org/get")

    val uri = targetOpt
      .map(target => baseUri.withQueryParam("target", target.toString))
      .getOrElse(baseUri)

    client
      .expect[Json](uri)
      .map { json =>
        json
          .hcursor
          .downField("args")
          .downField("target")
          .as[String]
          .getOrElse(defaultTarget)
      }
  }
}