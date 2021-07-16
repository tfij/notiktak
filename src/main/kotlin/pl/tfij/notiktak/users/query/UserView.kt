package pl.tfij.notiktak.users.query

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
class UserView(
    @Id val userId: String,
    val userEmail: String,
    val confirmationToken: String?
)

interface UserViewRepository : MongoRepository<UserView, String>
