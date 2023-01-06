package si.blaz.movieparserspring.controllers

import org.slf4j.LoggerFactory
import org.springframework.boot.availability.AvailabilityChangeEvent
import org.springframework.boot.availability.LivenessState
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import si.blaz.movieparserspring.confg.MovieAppProperties

@RestController
@RequestMapping("util")
class HealthController(val properties: MovieAppProperties, val ctx: ApplicationContext) {

    private val logger = LoggerFactory.getLogger(HealthController::class.java)

    @GetMapping("simulate_error")
    fun configTest(): Boolean {
        return properties.simulateError
    }

    @PostMapping("simulate_error")
    fun setConfigTest(): Boolean {
        logger.info("Starting error simulation")
        properties.simulateError = true

        AvailabilityChangeEvent.publish(ctx, LivenessState.BROKEN)

        return properties.simulateError
    }

    @PostMapping("break_request")
    fun setCircuitBreakerDelay(): String {
        properties.circuitBreakerBroken = !properties.circuitBreakerBroken
        logger.info("Setting circuit broken to ${properties.circuitBreakerBroken} s")
        val status = if(properties.circuitBreakerBroken) "" else " not"
        return  "Request is$status broken"
    }
    @GetMapping("circuit_breaker_broken")
    fun getCircuitBreakerDelay(): Boolean {
        return properties.circuitBreakerBroken
    }

    @GetMapping("circuit_breaker_request")
    fun circuitBreakerRequest(): String {
        if(properties.circuitBreakerBroken) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Not working")
        }
        return "Working"
    }

}