package pl.tfij.notiktak.student.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterOnCourseCommand(@TargetAggregateIdentifier val studentId: String, val courseId: String)

data class ChargeStudentCommand(@TargetAggregateIdentifier val studentId: String, val cost: Int)
