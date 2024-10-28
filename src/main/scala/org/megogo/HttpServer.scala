package org.megogo

import cats.effect.{IO, Ref}
import cats.implicits.toSemigroupKOps
import org.http4s._
import org.http4s.implicits._
import org.megogo.model.ApiErrors.{ApiError, TooManyRequests}
import org.megogo.model.FindResponse
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import scala.concurrent.duration.DurationInt
import scala.util.Try

class HttpServer(rateLimiter: RateLimiter) {
  private val sumOfTwo = new SumOfTwo

  private val findEndpoint = endpoint
    .get
    .in("find")
    .in(query[String]("data").description("Comma-separated list of integers"))
    .in(query[Int]("target").description("Target sum to find"))
    .out(jsonBody[FindResponse])
    .errorOut(
      oneOf[ApiError](
        oneOfVariant(StatusCode.TooManyRequests, jsonBody[TooManyRequests])
      )
    )
    .description("Find pairs of numbers in array that sum up to target")
    .tag("Sum Operations")
    .name("findPairs")

  private val swaggerEndpoints = SwaggerInterpreter()
    .fromEndpoints[IO](List(findEndpoint), "Sum finder API", "1.0.0")



  private val findRoute = findEndpoint.serverLogic { case (data, target) =>
    rateLimiter.isRateLimitExceeded.flatMap {
      case true =>
        IO.pure(Left(TooManyRequests("Rate limit exceeded")): Either[ApiError, FindResponse])
      case false =>
        val numbers = data.split(",").map(_.trim.toInt)
        val result = sumOfTwo(numbers, target)
        IO.pure(Right(FindResponse(result)))
    }
  }

  val routes: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(findRoute) <+>
      Http4sServerInterpreter[IO]().toRoutes(swaggerEndpoints)

  val httpApp = routes.orNotFound

}

object HttpServer {
  private val DefaultRateLimit = 5

  def make: IO[HttpServer] = {
    val rateLimit = sys.env.get("RATE_LIMIT")
      .flatMap(limit => Try(limit.toInt).toOption)
      .getOrElse(DefaultRateLimit)

    Ref.of[IO, Vector[Long]](Vector.empty).map(new RateLimiter(1.minute, _, rateLimit)).map(new HttpServer(_))
  }
}
