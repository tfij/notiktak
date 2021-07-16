package pl.tfij.notiktak.student.query

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
data class StudentView(
    @Id val userId: String,
    val credit: Int,
    val onCourses: List<StudentCourseView>
)

data class StudentCourseView(
    val courseId: String,
    val name: String,
    val courseCost: Int,
)

interface StudentViewRepository : MongoRepository<StudentView, String>