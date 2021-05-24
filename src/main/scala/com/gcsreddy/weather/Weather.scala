package com.gcsreddy.weather

import cats.effect.Sync
import cats.implicits._
import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.circe._
import org.http4s.Method._
import org.log4s.getLogger


trait Weather[F[_]]{
  def get(lat: Latitude, lon: Longitude): F[Weather.WeatherInfo]
}

case class Latitude(value: Double) extends AnyVal
case class Longitude(value: Double) extends AnyVal

object Weather {

  private val logger = getLogger

  def apply[F[_]](implicit ev: Weather[F]): Weather[F] = ev

  final case class WeatherCondition(
    main: String,
    description: String
  )

  final case class WeatherData(
    temp: Double,
    feels_like: Double,
    weather: List[WeatherCondition]
  )

  final case class Alert(
    event: String,
    description: String
  )

  final case class Data(
    lat: Double,
    lon: Double,
    current: WeatherData,
    alerts: Option[List[Alert]]
  )

  final case class WeatherInfo(
    info: String
  )

  object Data {
    implicit val dataWeatherConditionDecoder: Decoder[WeatherCondition] = deriveDecoder[WeatherCondition]
    implicit def dataWeatherConditionEntityDecoder[F[_]: Sync]: EntityDecoder[F, WeatherCondition] = jsonOf
    implicit val dataWeatherConditionEncoder: Encoder[WeatherCondition] = deriveEncoder[WeatherCondition]
    implicit def dataWeatherConditionEntityEncoder[F[_]]: EntityEncoder[F, WeatherCondition] = jsonEncoderOf

    implicit val dataWeatherDecoder: Decoder[WeatherData] = deriveDecoder[WeatherData]
    implicit def dataWeatherEntityDecoder[F[_]: Sync]: EntityDecoder[F, WeatherData] = jsonOf
    implicit val dataWeatherEncoder: Encoder[WeatherData] = deriveEncoder[WeatherData]
    implicit def dataWeatherEntityEncoder[F[_]]: EntityEncoder[F, WeatherData] = jsonEncoderOf

    implicit val dataAlertDecoder: Decoder[Alert] = deriveDecoder[Alert]
    implicit def dataAlertEntityDecoder[F[_]: Sync]: EntityDecoder[F, Alert] = jsonOf
    implicit val dataAlertEncoder: Encoder[Alert] = deriveEncoder[Alert]
    implicit def dataAlertEntityEncoder[F[_]]: EntityEncoder[F, Alert] = jsonEncoderOf

    implicit val dataDecoder: Decoder[Data] = deriveDecoder[Data]
    implicit def dataEntityDecoder[F[_]: Sync]: EntityDecoder[F, Data] = jsonOf
    implicit val dataEncoder: Encoder[Data] = deriveEncoder[Data]
    implicit def dataEntityEncoder[F[_]]: EntityEncoder[F, Data] = jsonEncoderOf

    implicit val dataWeatherInfoDecoder: Decoder[WeatherInfo] = deriveDecoder[WeatherInfo]
    implicit def dataWeatherInfoEntityDecoder[F[_]: Sync]: EntityDecoder[F, WeatherInfo] = jsonOf
    implicit val dataWeatherInfoEncoder: Encoder[WeatherInfo] = deriveEncoder[WeatherInfo]
    implicit def dataWeatherInfoEntityEncoder[F[_]]: EntityEncoder[F, WeatherInfo] = jsonEncoderOf
  }

  final case class WeatherError(e: Throwable) extends RuntimeException

  def impl[F[_]: Sync](c: Client[F]): Weather[F] = new Weather[F]{
    val dsl = new Http4sClientDsl[F]{}
    import dsl._
    def get(lat: Latitude, lon: Longitude): F[Weather.WeatherInfo] = {

      logger.info(lat.toString())
      logger.info(lon.toString())

      val params = Map(
        "lat" -> lat.value.toString(),
        "lon" -> lon.value.toString(),
        "exclude" -> "minutely,hourly,daily",
        "appid" -> "7d183c7035f7f4e9ae0021cea5a07e82"
      )

      c.expect[Data](GET(uri"https://api.openweathermap.org/data/2.5/onecall".withQueryParams(params)))
        .adaptError{ case t => WeatherError(t)}
        .map {
          data => logger.info(data.toString()); 
          val currentTempInC = 
            BigDecimal(data.current.temp - 273.15).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
          val feelLikeTempInC = 
            BigDecimal(data.current.feels_like - 273.15).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
          val tempCondition = currentTempInC match{
            case t if t < 273.15 => "below freezing"
            case t if t < 282.15 => "chilly"
            case t if t > 313.15 => "hot"
            case _ => "balmy"
          }
          val wCond = data.current.weather.map(_.description).mkString(",")
          val alerts = data.alerts match {
            case None => "no alerts"
            case Some(s) => s.map(_.description).mkString(",")
          }
          WeatherInfo(
            info = s"""Weather for latitude ${data.lat} and longitude ${data.lon}
            |
            |current temperature : ${currentTempInC} Celsius
            |
            |feel like temperature : ${feelLikeTempInC} Celsius
            |
            |outside temp is : ${tempCondition}
            |
            |weather conditions: ${wCond}
            |
            |alerts: ${alerts}
            """.stripMargin
          )
        }

    }
  }

}