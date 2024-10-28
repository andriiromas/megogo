package org.megogo

import cats.effect.{IO, Ref}

import scala.concurrent.duration.FiniteDuration

class RateLimiter(windowSize: FiniteDuration, requestTimestamps: Ref[IO, Vector[Long]], rateLimit: Int) {

  def isRateLimitExceeded: IO[Boolean] = {
    val now = System.currentTimeMillis()
    val windowStart = now - windowSize.toMillis

    for {
      _ <- requestTimestamps.update(_.filterNot(_ < windowStart))
      _ <- requestTimestamps.update(_ :+ now)
      times <- requestTimestamps.get
    } yield times.size > rateLimit
  }

}
