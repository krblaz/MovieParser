package si.blaz.movieparserspring.controllers

import si.blaz.movieparserspring.data.MetricRepo
import si.blaz.movieparserspring.data.Movie
import si.blaz.movieparserspring.data.MovieRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import si.blaz.movieparserspring.parsing.ParserService

@RestController
@RequestMapping("parse")
class ParserController(private val parserService: ParserService, private val movieRepository: MovieRepository, private val metricRepo: MetricRepo) {

    private val logger = LoggerFactory.getLogger(ParserController::class.java)

    @PostMapping
    fun parseMovie(@RequestBody body: String): MutableList<Movie> {
        val moviesUrls = body.split(System.lineSeparator())
        return parserService.parseURLList(moviesUrls)
    }

    @PostMapping("opening")
    fun parseOpening(): Boolean {
        parserService.parseOpening()
        return true
    }

}