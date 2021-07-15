package pl.tfij.notiktak.users.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateUserCommand(val email: String, val role: Role)

data class ConfirmUserEmailCommand(@TargetAggregateIdentifier val userId: String, val confirmationToken: String)
