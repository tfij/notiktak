package pl.tfij.notiktak.student.coreapi

data class StudentCreatedEvent(val studentId: String, val credit: Int)

data class StudentOnCourseRegistrationStartedEvent(val studentOnCourseRegistrationId: String, val studentId: String, val courseId: String)

data class StudentChargedCourseFeeEvent(val studentId: String, val courseId: String, val cost: Int)
