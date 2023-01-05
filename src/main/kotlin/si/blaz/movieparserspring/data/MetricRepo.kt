package si.blaz.movieparserspring.data

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class MetricRepo(private val registry: MeterRegistry) {
    val moviesAddedCounter = registry.counter("movies.added")
    val moviesRemovedCounter = registry.counter("movies.removed")
}