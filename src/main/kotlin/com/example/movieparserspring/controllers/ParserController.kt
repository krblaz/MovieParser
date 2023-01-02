package com.example.movieparserspring.controllers

import com.example.movieparserspring.data.Movie
import com.example.movieparserspring.data.MovieRepository
import com.example.movieparserspring.parsing.Parser
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("parse")
class ParserController(private val parser: Parser, private val movieRepository: MovieRepository) {

    val logger = LoggerFactory.getLogger(ParserController::class.java)

    @PostMapping
    fun parseMovie(@RequestBody body: String): MutableList<Movie> {
        val moviesUrls = body.split(System.lineSeparator())

        logger.info("Parse movie urls request called with ${moviesUrls.size} urls")

        val parsedMovies = mutableListOf<Movie>()
        for(movieUrl in moviesUrls){
            val movie = parser.parseFromWeb(movieUrl)
            movieRepository.save(movie)
            parsedMovies.add(movie)
        }
        return parsedMovies
    }

    @PostMapping("opening")
    fun parseOpening(){
        logger.info("Parse opening request")
    }

}