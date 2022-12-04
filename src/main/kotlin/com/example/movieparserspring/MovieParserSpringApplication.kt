package com.example.movieparserspring

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@SpringBootApplication
@ComponentScan(excludeFilters = arrayOf(ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = arrayOf(
    LoadMoviesToDB::class
))))
class MovieParserSpringApplication() :
    CommandLineRunner {
    override fun run(vararg args: String?) {

    }
}

fun main(args: Array<String>) {
    runApplication<MovieParserSpringApplication>(*args)
}