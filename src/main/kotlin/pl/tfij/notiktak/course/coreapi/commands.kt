package pl.tfij.notiktak.course.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateCourseCommand(val name: String, val participantsLimit: Int, val cost: Int)

data class AddStudentToCourseCommand(@TargetAggregateIdentifier val courseId: String, val studentId: String)
