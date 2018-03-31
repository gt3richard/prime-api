import domain.PrimeService
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class PrimeServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "PrimeService#isPrime" should {
    "return 0 is not prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(0)

      result mustBe false

    }
    "return 1 is not prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(1)

      result mustBe false

    }
    "return 2 is prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(2)

      result mustBe true

    }
    "return 3 is prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(3)

      result mustBe true

    }
    "return 4 is not prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(4)

      result mustBe false

    }
    "return 7 is prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(7)

      result mustBe true

    }
    "return 10 is not prime" in {

      val primeService = inject[PrimeService]
      val result = primeService.determineIfPrime(10)

      result mustBe false

    }
  }

  "PrimeService#calculateSubPrimes" should {
    "return no subprimes to 1" in {

      val primeService = inject[PrimeService]
      val result = primeService.calculateSubPrimes(1)

      result.toString mustBe "Vector()"
    }
    "return no subprime to 2" in {

      val primeService = inject[PrimeService]
      val result = primeService.calculateSubPrimes(2)

      result.toString mustBe "Vector()"
    }
    "return 2 as subprimes to 3" in {

      val primeService = inject[PrimeService]
      val result = primeService.calculateSubPrimes(3)

      result.toString mustBe "Vector(2)"
    }
    "return 2, 3 as subprimes to 4" in {

      val primeService = inject[PrimeService]
      val result = primeService.calculateSubPrimes(4)

      result.toString mustBe "Vector(2, 3)"
    }
    "return 2, 3 as subprimes to 5" in {

      val primeService = inject[PrimeService]
      val result = primeService.calculateSubPrimes(5)

      result.toString mustBe "Vector(2, 3)"
    }
    "return 2, 3, 5, 7 as subprimes to 8" in {

      val primeService = inject[PrimeService]
      val result = primeService.calculateSubPrimes(8)

      result.toString mustBe "Vector(2, 3, 5, 7)"
    }
  }

}
