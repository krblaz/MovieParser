package com.example.movieparserspring.parsing

import com.example.movieparserspring.confg.MovieAppProperties
import com.example.movieparserspring.data.Movie
import com.example.movieparserspring.data.RottenTomatoesMetadata
import com.example.movieparserspring.data.RottenTomatoesScore
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import kotlin.io.path.Path

@Component
class Parser(val properties: MovieAppProperties) {

    val logger = LoggerFactory.getLogger(Parser::class.java)

    fun parseOpening(): MutableList<Movie> {
        val url = "${properties.baseURL}${properties.openingURL}"
        val openingPage = Jsoup.connect(url).get()

        logger.info("Starting to parse opening page")
        val moviesElements = openingPage.select("div.discovery-tiles__wrap[data-qa=discovery-media-list] > a")
        val moviesUrls = moviesElements.map { it.attr("href") }

        val parsedMovies = mutableListOf<Movie>()
        var successful = 0
        var unsuccessful = 0
        for(movieUrl in moviesUrls){
            try {
                val movie = parseFromWeb(movieUrl)
                parsedMovies.add(movie)
                successful++;
            }catch (e: Exception) {
                logger.error(e.message)
                unsuccessful++
            }
        }

        logger.info("Done parsing opening page. $successful movies parsed successfully, $unsuccessful successfully")
        return parsedMovies
    }

    fun parseFromFile(path: String): Movie {
        logger.info("Parsing movie from $path")
        val pageFile = File(path)
        val moviePage = Jsoup.parse(pageFile)

        val movieUrl = Path(path).fileName.toString().split(".")[0]
        val movie = Movie("/m/$movieUrl")
        parse(movie, moviePage)

        logger.info("Done parsing movie ${movie.title}")
        return movie
    }

    fun parseFromWeb(url: String): Movie {
        val movieUrl = "/m/${url.split("/").last()}"
        logger.info("Parsing movie from $movieUrl")

        val moviePage = Jsoup.connect("${properties.baseURL}$movieUrl").get()
        val movie = Movie(movieUrl)
        parse(movie, moviePage)

        logger.info("Done parsing movie ${movie.title}")
        return movie
    }

    private fun parse(movie: Movie, moviePage: Document){
        val scoreBoard = moviePage.select("score-board")
        val detailsPane = moviePage.select("ul.content-meta")

        movie.title = scoreBoard.select("h1[slot=title]").text()
        movie.rt_score = parseScoreBoard(scoreBoard)
        movie.rt_metadata = parseDetails(detailsPane)
    }

    private fun parseScoreBoard(scoreBoard: Elements): RottenTomatoesScore {
        val tmScore = scoreBoard.attr("tomatometerscore").let { if (it == "") null else it.toInt() }
        val tmState = scoreBoard.attr("tomatometerstate").let { if (it == "") null else it }
        val audScore = scoreBoard.attr("audiencescore").let { if (it == "") null else it.toInt() }
        val audState = scoreBoard.attr("audiencestate").let { if (it == "") null else it }
        return RottenTomatoesScore(tmScore, tmState, audScore, audState)
    }

    private fun parseDetails(detailsPane: Elements): RottenTomatoesMetadata {
        val movieDetails = RottenTomatoesMetadata()
        for (detailEntry in detailsPane.first()!!.children()) {
            val key = detailEntry.children()[0].text().let {
                it.substring(0, it.length - 1)
            }
            val value = detailEntry.children()[1].text()

            when(key){
                "Rating" -> movieDetails.rating = value.split(" ")[0]
                "Original Language" -> movieDetails.original_language = value
                "Genre" -> movieDetails.genre = value.split(", ")
                "Director" -> movieDetails.director = value.split(", ")
                "Producer" -> movieDetails.producer = value.split(", ")
                "Writer" -> movieDetails.writer = value.split(", ")
                "Distributor" -> movieDetails.distributor = value.split(", ")
            }
        }
        return movieDetails
    }
}