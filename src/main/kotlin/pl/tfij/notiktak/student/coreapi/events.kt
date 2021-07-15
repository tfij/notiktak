package pl.tfij.notiktak.student.coreapi

data class StudentCreatedEvent(val studentId: String, val credit: Int)

data class StudentOnCourseRegistrationStartedEvent(val studentOnCourseRegistrationId: String, val studentId: String, val courseId: String)

data class StudentCharged(val studentId: String, val cost: Int)
