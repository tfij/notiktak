package pl.tfij.notiktak.users.coreapi

import java.util.*

data class UserCreatedEvent(val userId: UUID, val email: String, val confirmationToken: String)

data class UserEmailConfirmedEvent(val userId: UUID)
