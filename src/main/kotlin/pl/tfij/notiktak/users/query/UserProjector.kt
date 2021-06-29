package pl.tfij.notiktak.users.query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pl.tfij.notiktak.users.coreapi.FindUserQuery
import pl.tfij.notiktak.users.coreapi.UserCreatedEvent

@Component
class UserProjector(private val userViewRepository: UserViewRepository) {

    @EventHandler
    fun on(event: UserCreatedEvent) {
        val userView = UserView(event.userId, event.email, event.confirmationToken)
        userViewRepository.save(userView)
    }

    @QueryHandler
    fun on(event: FindUserQuery): UserView? {
        return userViewRepository.findByIdOrNull(event.userId)
    }

}