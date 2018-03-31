package controllers

import javax.inject.{Inject, Singleton}

import domain.PrimeService
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class PrimeController @Inject()(cc: ControllerComponents, primeService: PrimeService)
    extends AbstractController(cc) {
  import PrimeController._

  def getPrimes(value: Int) = Action.async { request =>
    for {
      (isPrime, subPrimes) <- primeService.checkIfPrime(value)
    } yield {
      val json = Json.toJson(Response(isPrime, subPrimes))
      Ok(json)
    }
  }

}
object PrimeController {

  case class Response(isPrime: Boolean, primes: Seq[Int])

  implicit val responseWrites: Writes[Response] = Json.writes[Response]

}
