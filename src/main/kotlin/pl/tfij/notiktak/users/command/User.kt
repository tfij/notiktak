package pl.tfij.notiktak.users.command
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.tfij.notiktak.logger
import pl.tfij.notiktak.users.coreapi.ConfirmUserEmailCommand
import pl.tfij.notiktak.users.coreapi.CreateUserCommand
import pl.tfij.notiktak.users.coreapi.UserCreatedEvent
import pl.tfij.notiktak.users.coreapi.UserEmailConfirmedEvent
import java.util.*

@Aggregate
class User {
    @AggregateIdentifier
    private var userId: UUID? = null
    private var email: String? = null
    private var confirmed: Boolean? = null
    private var confirmationToken: String? = null
    private var dummyCounter = 0

    constructor() {
        // Required by Axon
    }

    @CommandHandler
    constructor(command: CreateUserCommand) {
        logger.info("command handler: " + command.toString())
        val confirmationToken = UUID.randomUUID().toString().replace("-", "")
        AggregateLifecycle.apply(UserCreatedEvent(UUID.randomUUID(), command.email, confirmationToken))
    }

    @CommandHandler
    fun handle(command: ConfirmUserEmailCommand): String {
        logger.info("command handler: " + command.toString())
        if (confirmed == true) {
            throw RuntimeException("User mail already confirmed")
        }
        if (command.confirmationToken != confirmationToken) {
            throw RuntimeException("Confirmation `${command.confirmationToken}` token not valid.")
        }
        AggregateLifecycle.apply(UserEmailConfirmedEvent(command.userId))
        return "abc"
    }

    @EventSourcingHandler
    fun on(event: UserCreatedEvent) {
        logger.info("event handler: " + event.toString())
        userId = event.userId
        email = event.email
        confirmed = false
        confirmationToken = event.confirmationToken
    }

    @EventSourcingHandler
    fun on(event: UserEmailConfirmedEvent) {
        logger.info("event handler: " + event.toString())
        confirmed = true
    }

    companion object {
        private val logger by logger()
    }
}