package com.example.movieparserspring

import com.example.movieparserspring.data.MovieRepository
import com.example.movieparserspring.parsing.Parser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.exists

@SpringBootApplication
class LoadMoviesToDB(private val parser: Parser, private val movieRepository: MovieRepository) : CommandLineRunner{

    val logger: Logger = LoggerFactory.getLogger(LoadMoviesToDB::class.java)

    override fun run(vararg args: String?) {
        val base_path = Path("sample_data/pages")
        if(!base_path.exists()){
            logger.error("Path $base_path doesn't exists")
            return
        }

        Files.list(base_path).forEach { movie_file_path ->
            val m = parser.parseFromPath(movie_file_path.toString())
            movieRepository.save(m)
            logger.info("Saved movie ${m.title}")
        }
    }

}

fun main(args: Array<String>) {
    val app = SpringApplication(LoadMoviesToDB::class.java)
    app.webApplicationType = WebApplicationType.NONE
    app.run(*args)
}