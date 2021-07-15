package pl.tfij.notiktak.course.coreapi

data class CourseCreatedEvent(
    val courseId: String,
    val name: String,
    val participantsLimit: Int,
    val cost: Int,
    )

data class StudentAddedToCourseEvent(val courseId: String, val studentId: String, val courseName: String, val courseCost: Int)
