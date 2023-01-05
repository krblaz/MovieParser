package si.blaz.movieparserspring.controllers

import si.blaz.movieparserspring.data.Movie
import si.blaz.movieparserspring.data.MovieRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("movie")
class MovieController(private val movieRepository: MovieRepository, private val tmdbService: TMDBService) {

    private final val logger = LoggerFactory.getLogger(MovieController::class.java)

    @GetMapping
    fun getMovies(): MutableList<Movie> {
        val res = movieRepository.findAll()
        return res
    }

    @GetMapping("{id}")
    fun getMovie(@PathVariable id: String): Optional<Movie> {
        return movieRepository.findById(id)
    }

    //TODO FIX
    @GetMapping("{id}/tmdb")
    fun getTMDBInfo(@PathVariable id:String): ResponseEntity<String> {
        val movie = movieRepository.findById(id)
        if(movie.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with $id not found")
        val foundMovie = tmdbService.searchMovie(movie.get())
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with $id not found on TMDB")
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(foundMovie)
    }
}