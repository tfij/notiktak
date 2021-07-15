package pl.tfij.notiktak.users.restapi

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.tfij.notiktak.users.coreapi.ConfirmUserEmailCommand
import pl.tfij.notiktak.users.coreapi.CreateUserCommand
import pl.tfij.notiktak.users.coreapi.FindUserQuery
import pl.tfij.notiktak.users.coreapi.Role
import pl.tfij.notiktak.users.query.UserView
import java.util.*
import java.util.concurrent.CompletableFuture

@RequestMapping("/users")
@RestController
class UsersController(private val commandGateway: CommandGateway, private val queryGateway: QueryGateway) {

    @PostMapping("/create")
    fun createUser(): CompletableFuture<CreatedUserRestResponse> {
        return commandGateway.send<String>(CreateUserCommand("jon@example.com", Role.Student(100)))
            .thenApply { CreatedUserRestResponse(it.toString()) }
    }

    @PostMapping("/confirm-email/{userId}/{confirmationToken}")
    fun confirmEmail(
        @PathVariable("userId") userId: String,
        @PathVariable("confirmationToken") confirmationToken: String
    ): CompletableFuture<String> {
        return commandGateway.send(ConfirmUserEmailCommand(UUID.fromString(userId).toString(), confirmationToken))
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable("userId") userId: String): CompletableFuture<UserRestResponse> {
        val query = queryGateway.query(
            FindUserQuery(UUID.fromString(userId).toString()),
            ResponseTypes.instanceOf(UserView::class.java)
        )
        return query.thenApply { UserRestResponse(it.userId.toString(), it.userEmail, it.confirmationToken) }
    }
}

data class CreatedUserRestResponse(val userId: String)

data class UserRestResponse(
    val userId: String,
    val userEmail: String,
    val confirmationToken: String?
)
