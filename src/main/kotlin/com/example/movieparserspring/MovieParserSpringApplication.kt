package com.example.movieparserspring

import com.example.movieparserspring.parsing.Parser
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovieParserSpringApplication(val parser: Parser) :
    CommandLineRunner {
    override fun run(vararg args: String?) {

    }
}

fun main(args: Array<String>) {
    runApplication<MovieParserSpringApplication>(*args)
}