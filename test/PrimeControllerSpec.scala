import controllers.PrimeController
import domain.PrimeDb
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class PrimeControllerSpec
    extends PlaySpec
    with GuiceOneAppPerTest
    with Injecting {

  "PrimeController#getPrime" should {
    "return 1 as not prime" in {

      val primeController = inject[PrimeController]
      val primeDb = inject[PrimeDb]
      Await.ready(primeDb.delete, Duration.Inf)

      val result: Future[Result] =
        primeController.getPrimes(1).apply(FakeRequest())
      val bodyText: String = contentAsString(result)

      bodyText mustBe """{"isPrime":false,"primes":[]}"""
    }
    "return 5 as prime and include subPrimes in second call" in {

      val primeController = inject[PrimeController]
      val primeDb = inject[PrimeDb]
      Await.ready(primeDb.delete, Duration.Inf)

      val result: Future[Result] =
        primeController.getPrimes(5).apply(FakeRequest())
      val bodyText: String = contentAsString(result)

      bodyText mustBe """{"isPrime":true,"primes":[]}"""

      Thread.sleep(10000) //Wait for Background job to complete

      val result2: Future[Result] =
        primeController.getPrimes(5).apply(FakeRequest())
      val bodyText2: String = contentAsString(result2)

      bodyText2 mustBe """{"isPrime":true,"primes":[2,3]}"""
    }
    "return 8 as not prime but include subPrimes in second call" in {

      val primeController = inject[PrimeController]
      val primeDb = inject[PrimeDb]
      Await.ready(primeDb.delete, Duration.Inf)

      val result: Future[Result] =
        primeController.getPrimes(8).apply(FakeRequest())
      val bodyText: String = contentAsString(result)

      bodyText mustBe """{"isPrime":false,"primes":[]}"""

      Thread.sleep(10000) //Wait for Background job to complete

      val result2: Future[Result] =
        primeController.getPrimes(8).apply(FakeRequest())
      val bodyText2: String = contentAsString(result2)

      bodyText2 mustBe """{"isPrime":false,"primes":[2,3,5,7]}"""
    }
  }

}
