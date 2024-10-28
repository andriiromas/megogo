package org.megogo

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.traverse._
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.duration._

class TargetExtractorIntegrationTest extends AnyFunSuite {

  private val runTest = (test: Client[IO] => IO[Unit]) => {
    EmberClientBuilder
      .default[IO]
      .withTimeout(5.seconds)
      .withIdleTimeInPool(30.seconds)
      .build
      .use { client =>
        test(client)
      }
      .unsafeRunSync()
  }

  test("successfully call httpbin and extract target parameter") {
    runTest { client =>
      val extractor = new TargetExtractor(client, "default-value")
      extractor(Some(42)).map(result => assert(result == "42"))
    }
  }

  test("successfully call httpbin without target parameter and return default value") {
    runTest { client =>
      val extractor = new TargetExtractor(client, "default-value")
      extractor(None).map(result => assert(result == "default-value"))
    }
  }

  test("handle multiple sequential requests") {
    runTest { client =>
      val extractor = new TargetExtractor(client, "default-value")
      for {
        results <- (1 to 3).toList.traverse(i => extractor(Some(i)))
      } yield {
        assert(results.size == 3)
        assert(results == List("1", "2", "3"))
      }
    }
  }
}