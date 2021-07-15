package pl.tfij.notiktak.course.query

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

@Document
data class CourseView(
    @Id
    val courseId: String,
    val vacanciesLeft: Int,
    val cost: Int,
    val students: List<StudentOnCourseView>
)

data class StudentOnCourseView(
    val studentId: String
)

interface CourseViewRepository : MongoRepository<CourseView, String>
