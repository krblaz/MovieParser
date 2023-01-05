package si.blaz.movieparserspring

import si.blaz.movieparserspring.controllers.MovieController
import si.blaz.movieparserspring.parsing.Parser
import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import si.blaz.movieparserspring.controllers.TMDBService

@SpringBootApplication
class MovieParserSpringApplication(val parser: Parser, val tmdbService: TMDBService) : CommandLineRunner {

    override fun run(vararg args: String?) {

    }
}

fun main(args: Array<String>) {
    runApplication<MovieParserSpringApplication>(*args)
}