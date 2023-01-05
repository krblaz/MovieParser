package si.blaz.movieparserspring.parsing

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import si.blaz.movieparserspring.data.MetricRepo
import si.blaz.movieparserspring.data.Movie
import si.blaz.movieparserspring.data.MovieRepository

@Service
class ParserService(
    private val parser: Parser,
    private val movieRepository: MovieRepository,
    private val metricRepo: MetricRepo
) {

    private final val logger = LoggerFactory.getLogger(ParserService::class.java)
    fun parseURLList(moviesUrls: List<String>): MutableList<Movie> {
        logger.info("Parse movie urls request called with ${moviesUrls.size} urls")
        val parsedMovies = mutableListOf<Movie>()
        for(movieUrl in moviesUrls){
            val movie = parser.parseFromWeb(movieUrl)
            movieRepository.save(movie)
            parsedMovies.add(movie)
            metricRepo.moviesAddedCounter.increment()
        }
        return parsedMovies
    }

    @Async
    fun parseOpening() {
        val movieList = parser.parseOpening()
        movieRepository.saveAll(movieList)
        metricRepo.moviesAddedCounter.increment(movieList.size.toDouble())
    }
}