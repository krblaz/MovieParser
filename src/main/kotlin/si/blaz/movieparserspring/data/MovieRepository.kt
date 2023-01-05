package si.blaz.movieparserspring.data

import org.springframework.data.mongodb.repository.MongoRepository

interface MovieRepository: MongoRepository<Movie, String>{

}