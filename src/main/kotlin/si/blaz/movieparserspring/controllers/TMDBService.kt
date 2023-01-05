package si.blaz.movieparserspring.controllers

import kong.unirest.Unirest
import kong.unirest.json.JSONArray
import kong.unirest.json.JSONObject
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import si.blaz.movieparserspring.confg.MovieAppProperties
import si.blaz.movieparserspring.data.Movie


@Service
class TMDBService(restTemplateBuilder: RestTemplateBuilder, private val movieAppProperties: MovieAppProperties) {
    fun searchMovie(movie: Movie): String? {
        val response = Unirest.get("${movieAppProperties.tmdbBaseUrl}/search/movie").queryString("api_key",movieAppProperties.tmdbApiKey).queryString("query",movie.title).asJson()
        val results = response.body.`object`["results"] as JSONArray
        val foundMovie = results.map { it as JSONObject}.find { it["title"] == movie.title }
        return foundMovie?.toString()
    }
}