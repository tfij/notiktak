package pl.tfij.notiktak.student.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterOnCourseCommand(@TargetAggregateIdentifier val studentId: String, val courseId: String)

data class ChargeStudentCourseFeeCommand(@TargetAggregateIdentifier val studentId: String, val courseId: String, val cost: Int)
