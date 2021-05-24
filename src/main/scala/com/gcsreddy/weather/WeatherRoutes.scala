package com.gcsreddy.weather

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scala.util.Try

object WeatherRoutes {

  object LatitudeVar {
    def unapply(str: String): Option[Latitude] = {
      Try(Latitude(str.toDouble)).toOption.filter {
        case Latitude(value) => value >= -90.0 && value <= 90.0
      }
    }
  }

  object LongitudeVar {
    def unapply(str: String): Option[Longitude] = {
      Try(Longitude(str.toDouble)).toOption.filter {
        case Longitude(value) => value >= -180.0 && value <= 180.0
      }
    }
  }

  def weatherRoutes[F[_]: Sync](W: Weather[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    //import Weather.Data._
    HttpRoutes.of[F] {
      case GET -> Root / "weather" / "lat" / LatitudeVar(lat) / "lon" / LongitudeVar(lon) =>
        for {
          wd <- W.get(lat, lon)
          resp <- Ok(wd.info)
        } yield resp
      case _ =>  //REDO: more specific error messages
        BadRequest("bad request, check your latitude(-90.0 to 90.0) and longitude(-180.0 to 180.0)"+
        "example - /weather/lat/28.81/lon/-97.01")
    }
  }
}