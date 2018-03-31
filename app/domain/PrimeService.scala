package domain

import akka.actor.{ActorSystem, Cancellable, Scheduler}
import akka.stream.ActorMaterializer
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class PrimeService(primeDb: PrimeDb) {

  val log: Logger = LoggerFactory.getLogger(getClass)

  implicit val system: ActorSystem = ActorSystem("prime-api")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val scheduler: Scheduler = system.scheduler

  def checkIfPrime(value: Int): Future[(Boolean, Seq[Int])] =
    primeDb.getPrimes(value).map {
      case None =>
        val isPrime = determineIfPrime(value)
        scheduleSubPrimeCalculation(isPrime, value)
        (isPrime, Seq())
      case Some(s) => (s.isPrime, s.subPrimes)
    }

  def scheduleSubPrimeCalculation(isPrime: Boolean, value: Int): Cancellable =
    scheduler.scheduleOnce(0.seconds)(
      {
        log.debug(s"Staring sub prime calculations for $value")
        val primes = calculateSubPrimes(value)
        val saveToDb = primeDb.insertPrimes(value, isPrime, primes)
        Await.ready(saveToDb, Duration.Inf)
        log.debug(s"Finished sub prime calculations for $value")
      }
    )

  def calculateSubPrimes(value: Int): Seq[Int] =
    (1 until value).map(m => (m, determineIfPrime(m))).filter(f => f._2).map(_._1)

  def determineIfPrime(value: Int): Boolean =
    !(value < 2 || (2 until value)
      .map(m => (m, value % m))
      .exists(_._2 == 0))

}
object PrimeService {
  case class PrimeData(value: Int, isPrime: Boolean, subPrimes: Seq[Int])
}
