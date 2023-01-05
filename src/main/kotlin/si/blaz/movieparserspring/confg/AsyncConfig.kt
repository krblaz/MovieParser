package si.blaz.movieparserspring.confg

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor




@Configuration
@EnableAsync
class AsyncConfig {

    @Bean
    fun asyncExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 2
        executor.maxPoolSize = 2
        executor.queueCapacity = 500
        executor.setThreadNamePrefix("JDAsync-")
        executor.initialize()
        return executor
    }
}