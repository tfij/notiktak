package pl.tfij.notiktak.infrastructure

import org.axonframework.modelling.saga.repository.SagaStore
import org.axonframework.modelling.saga.repository.inmemory.InMemorySagaStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {
    @Bean
    fun mySagaStore(): SagaStore<Any> {
        return InMemorySagaStore() //TODO change to MongoSagaStore
    }
}