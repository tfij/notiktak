package pl.tfij.notiktak.users.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateUserCommand(val email: String)

class ConfirmUserEmailCommand(@TargetAggregateIdentifier val userId: UUID, val confirmationToken: String)
