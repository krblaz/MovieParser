package com.example.movieparserspring.controllers

import com.example.movieparserspring.data.Movie
import com.example.movieparserspring.data.MovieRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("movie")
class MovieController(private val movieRepository: MovieRepository) {

    val logger = LoggerFactory.getLogger(MovieController::class.java)

    @GetMapping
    fun getMovies(): MutableList<Movie> {
        val res = movieRepository.findAll()
        logger.info("Get all movies called, returning ${res.size} movies")
        return res
    }

    @GetMapping("{id}")
    fun getMovie(@PathVariable id: String): Optional<Movie> {
        logger.info("Get movie by id $id called")
        return movieRepository.findById(id)
    }
}