package pl.tfij.notiktak.users.coreapi

import java.util.*

data class UserCreatedEvent(val userId: String, val email: String, val confirmationToken: String, val role: Role)

data class UserEmailConfirmedEvent(val userId: String)

sealed class Role {

    data class Student(val credit: Int) : Role()

    object Teacher : Role()
}
