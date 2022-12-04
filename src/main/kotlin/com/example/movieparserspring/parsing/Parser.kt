package com.example.movieparserspring.parsing

import com.example.movieparserspring.data.Movie
import com.example.movieparserspring.data.RottenTomatoesMetadata
import com.example.movieparserspring.data.RottenTomatoesScore
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.stereotype.Component
import java.io.File
import kotlin.io.path.Path

@Component
class Parser() {

    fun parseFromPath(path: String): Movie {
        val pageFile = File(path)
        val moviePage = Jsoup.parse(pageFile)

        val movie = Movie(Path(path).fileName.toString().split(".")[0])
        parse(movie, moviePage)
        return movie
    }

    fun parse(movie: Movie, moviePage: Document){
        val scoreBoard = moviePage.select("score-board")
        val detailsPane = moviePage.select("ul.content-meta")

        movie.title = scoreBoard.select("h1[slot=title]").text()
        movie.rt_score = parseScoreBoard(scoreBoard)
        movie.rt_metadata = parseDetails(detailsPane)
    }

    fun parseScoreBoard(scoreBoard: Elements): RottenTomatoesScore {
        val tmScore = scoreBoard.attr("tomatometerscore").let { if (it == "") null else it.toInt() }
        val tmState = scoreBoard.attr("tomatometerstate").let { if (it == "") null else it }
        val audScore = scoreBoard.attr("audiencescore").let { if (it == "") null else it.toInt() }
        val audState = scoreBoard.attr("audiencestate").let { if (it == "") null else it }
        return RottenTomatoesScore(tmScore, tmState, audScore, audState)
    }

    fun parseDetails(detailsPane: Elements): RottenTomatoesMetadata {
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