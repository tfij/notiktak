package pl.tfij.notiktak.users.query

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

@Document
class UserView(
    @Id val userId: UUID,
    val userEmail: String,
    val confirmationToken: String?
    ) {


}

interface UserViewRepository: MongoRepository<UserView, UUID>
