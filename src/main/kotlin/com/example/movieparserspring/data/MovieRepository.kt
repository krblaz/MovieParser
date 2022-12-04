package com.example.movieparserspring.data

import org.springframework.data.mongodb.repository.MongoRepository

interface MovieRepository: MongoRepository<Movie, String>{
}