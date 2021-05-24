package com.gcsreddy.weather

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    WeatherServer.stream[IO].compile.drain.as(ExitCode.Success)
}
