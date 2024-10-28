package org.megogo

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    HttpServer.make.flatMap { httpServer =>
      BlazeServerBuilder[IO]
        .bindHttp(port = 8080, host = "0.0.0.0")
        .withHttpApp(httpServer.httpApp)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    }

}