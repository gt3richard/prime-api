package controllers

import com.google.inject.{AbstractModule, Provides}
import domain.{PrimeDb, PrimeService}
import play.api.db.slick.DatabaseConfigProvider

class PrimeModule extends AbstractModule {

  @Override
  def configure() = Unit

  @Provides
  def providesPrimeDb(dbConfigProvider: DatabaseConfigProvider): PrimeDb =
    new PrimeDb(dbConfigProvider)

  @Provides
  def providesPrimeService(primeDb: PrimeDb): PrimeService =
    new PrimeService(primeDb)

}
