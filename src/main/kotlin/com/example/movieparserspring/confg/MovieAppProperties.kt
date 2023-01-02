package com.example.movieparserspring.confg

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "movies-app")
class MovieAppProperties {
    var baseURL = "https://www.rottentomatoes.com"
    var openingURL = "/browse/movies_in_theaters/sort:newest?page=100"

    var initDBPath: String? = null

    var simulateError = false
}