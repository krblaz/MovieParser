package si.blaz.movieparserspring.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Document("movies")
class Movie(
    @Id var url: String,
    var title: String? = null
) {

    var rt_score: RottenTomatoesScore? = null
    var rt_metadata: RottenTomatoesMetadata? = null

    var date_added = LocalDateTime.now()

    override fun toString(): String {
        return "MovieRT(url='$url', title='$title', added='$date_added')"
    }
}

class RottenTomatoesScore(
    var tomatometer_score: Int?,
    var tomatometer_state: String?,
    var audience_score: Int?,
    var audience_state: String?
) {

    override fun toString(): String {
        return "Score(Tomatometer: $tomatometer_score - $tomatometer_state, Audience: $audience_score - $audience_state)"
    }

}

class RottenTomatoesMetadata(
    var rating: String? = null,
    var original_language: String? = null,
    var genre: List<String> = mutableListOf(),
    var director: List<String> = mutableListOf(),
    var producer: List<String> = mutableListOf(),
    var writer: List<String> = mutableListOf(),
    var distributor: List<String> = mutableListOf(),
    var releaseDateTheater: LocalDate? = null,
    var releaseDateTheaterType: String? = null,
    var releaseDateStreaming: LocalDate? = null,

    ) {

    val releaseDate: LocalDate?
        get() {
            val first = releaseDateTheater ?: LocalDate.MAX
            val second = releaseDateStreaming ?: LocalDate.MAX
            return if (releaseDateStreaming == null && releaseDateTheater == null) null else if(first.isBefore(second)) releaseDateTheater else releaseDateStreaming
        }

    override fun toString(): String {
        return "RottenTomatoesMetadata(rating=$rating, genre=$genre, original_language=$original_language, director=$director, producer=$producer, writer=$writer, distributor=$distributor)"
    }
}