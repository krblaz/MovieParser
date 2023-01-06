package si.blaz.MovieParserCircuitBreaker

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration


@SpringBootApplication
@RestController
class MovieParserCircuitBreakerApplication(
    val circuitBreakerFactory: CircuitBreakerFactory<*, *>,
    val restTemplateBuilder: RestTemplateBuilder
) {

    private val logger = LoggerFactory.getLogger(MovieParserCircuitBreakerApplication::class.java)

    @Value("\${service.address}")
    private lateinit var address: String

    @GetMapping("test")
    fun testCircuitBreaker(): ResponseEntity<String>? {
        val restTemplate = restTemplateBuilder.build()

        val circuitBreaker = circuitBreakerFactory.create("breaker")
        return circuitBreaker.run({
            logger.info("Call succeeded")
            ResponseEntity.ok().body(restTemplate.getForObject("http://$address/util/circuit_breaker_request", String::class.java))
        },{
            logger.info(it.message)
            if(it is CallNotPermittedException){
                return@run ResponseEntity.internalServerError().body("Circuit breaker is OPEN")
            }else {
                return@run ResponseEntity.internalServerError().body("MovieParser in unreachable")
            }
        })
    }
}

@Configuration
class Resilience4JConfiguration {
    @Bean
    fun globalCustomConfiguration(): Customizer<Resilience4JCircuitBreakerFactory> {
        val circuitBreakerConfig =
            CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .failureRateThreshold(60f)
                .slowCallRateThreshold(40f)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .minimumNumberOfCalls(3)
                .writableStackTraceEnabled(true)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .build()

        // the circuitBreakerConfig and timeLimiterConfig objects
        return Customizer<Resilience4JCircuitBreakerFactory> { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MovieParserCircuitBreakerApplication>(*args)
}
