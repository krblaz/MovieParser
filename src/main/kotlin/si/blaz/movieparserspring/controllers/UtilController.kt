package si.blaz.movieparserspring.controllers

import si.blaz.movieparserspring.confg.MovieAppProperties
import si.blaz.movieparserspring.data.Movie
import si.blaz.movieparserspring.data.MovieRepository
import si.blaz.movieparserspring.parsing.Parser
import org.slf4j.LoggerFactory
import org.springframework.boot.availability.AvailabilityChangeEvent
import org.springframework.boot.availability.LivenessState
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.exists

@RestController
@RequestMapping("util")
class UtilController(val movieRepository: MovieRepository, val properties: MovieAppProperties, val parser: Parser, val ctx: ApplicationContext) {

    private final val logger = LoggerFactory.getLogger(UtilController::class.java)

    @DeleteMapping("delete_all")
    fun deleteAllMovies(): String {
        logger.info("Delete all request DB called")
        movieRepository.deleteAll()

        val message = "Successfully deleted all DB entries"
        logger.info(message)
        return message
    }

    @PostMapping("init_db")
    fun initDB(): ResponseEntity<String> {
        logger.info("InitDB request called")

        if (properties.initDBPath == null) {
            val message = "No DB path set"
            logger.error(message)
            return ResponseEntity.internalServerError().body(message)
        }

        val basePath = Path(properties.initDBPath!!)
        if (!basePath.exists()) {
            val message = "Path $basePath doesn't exists"
            logger.error(message)
            return ResponseEntity.internalServerError().body(message)
        }

        val parsedMovies = mutableListOf<Movie>()
        Files.list(basePath).forEach { movie_file_path ->
            val movie = parser.parseFromFile(movie_file_path.toString())
            parsedMovies.add(movie)
            movieRepository.save(movie)
            logger.info("Saved movie ${movie.title}")
        }
        return ResponseEntity.ok().body("Successfully loaded ${parsedMovies.size} movies to DB")
    }

    @GetMapping("simulate_error")
    fun configTest(): Boolean {
        return properties.simulateError
    }

    @PostMapping("simulate_error")
    fun setConfigTest(): Boolean {
        logger.info("Starting error simulation")
        properties.simulateError = true

        AvailabilityChangeEvent.publish(ctx, LivenessState.BROKEN)

        return properties.simulateError
    }
}