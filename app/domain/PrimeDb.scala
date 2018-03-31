package domain

import domain.PrimeService.PrimeData
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json._
import slick.jdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PrimeDb(dbConfigProvider: DatabaseConfigProvider) {
  import PrimeDb._

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.profile.api._

  class PrimeTable(tag: Tag) extends Table[PrimeEntity](tag, "prime_entity") {
    def value = column[Int]("value")
    def isPrime = column[Boolean]("is_prime")
    def subPrimes = column[String]("sub_primes")
    def * =
      (value, isPrime, subPrimes) <> (PrimeEntity.tupled, PrimeEntity.unapply)
  }

  def primeQuery = TableQuery[PrimeTable]

  def getPrimes(value: Int): Future[Option[PrimeData]] =
    dbConfig.db run primeQuery
      .filter(f => f.value === value)
      .result
      .headOption
      .map(_ map (_.toPrimeData))

  def insertPrimes(
      value: Int,
      isPrime: Boolean,
      primes: Seq[Int]
  ): Future[Unit] =
    dbConfig.db run DBIO.seq(
      primeQuery insertOrUpdate PrimeEntity(value, isPrime, primes))

  def delete: Future[Int] = dbConfig.db run primeQuery.delete

}
object PrimeDb {
  case class PrimeEntity(value: Int, isPrime: Boolean, subPrimes: String)

  implicit val formatPrimeEntity: Format[PrimeEntity] = Json.format[PrimeEntity]

  object PrimeEntity extends ((Int, Boolean, String) => PrimeEntity) {
    def apply(value: Int, isPrime: Boolean, subPrimes: Seq[Int]): PrimeEntity =
      PrimeEntity(value, isPrime, Json.toJson(subPrimes).toString)
  }

  implicit class PrimeEntityOps(val primeEntity: PrimeEntity) extends AnyVal {
    def toPrimeData: PrimeData =
      PrimeData(
        primeEntity.value,
        primeEntity.isPrime,
        Json.parse(primeEntity.subPrimes).as[Seq[Int]]
      )
  }
}
