package com.example.movieparserspring

import com.example.movieparserspring.data.MovieRepository
import com.example.movieparserspring.parsing.Parser
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovieParserSpringApplication(private val movieRepo: MovieRepository, private val parser: Parser) :
    CommandLineRunner {
    override fun run(vararg args: String?) {
        //parseMovie("sample_data/pages/tucker_the_man_and_his_dream.html")
        //parseMovie("sample_data/pages/dont_look_at_the_demon.html")

        val m1 = parser.parseFromPath("sample_data/pages/tucker_the_man_and_his_dream.html")
        movieRepo.save(m1)
    }
}

fun main(args: Array<String>) {
    runApplication<MovieParserSpringApplication>(*args)
}