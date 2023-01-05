package si.blaz.movieparserspring.controllers

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import si.blaz.movieparserspring.data.Movie
import si.blaz.movieparserspring.data.MovieRepository

@Controller
class MovieGraphQLController(private val movieRepository: MovieRepository) {

    @QueryMapping
    fun allMovies(): MutableList<Movie> {
        return movieRepository.findAll()
    }

    @QueryMapping
    fun movieById(@Argument id: String): Movie {
        return movieRepository.findById(id).get()
    }
}