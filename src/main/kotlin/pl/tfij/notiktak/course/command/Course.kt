package pl.tfij.notiktak.course.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.tfij.notiktak.course.coreapi.AddStudentToCourseCommand
import pl.tfij.notiktak.course.coreapi.CourseCreatedEvent
import pl.tfij.notiktak.course.coreapi.CreateCourseCommand
import pl.tfij.notiktak.course.coreapi.StudentAddedToCourseEvent
import java.util.*

@Aggregate
class Course {
    @AggregateIdentifier
    private var courseId: String? = null
    private var name: String? = null
    private var participantsLimit: Int? = null
    private var cost: Int? = null
    private val students = mutableListOf<String>()

    constructor() {
        // Required by Axon
    }

    @CommandHandler
    constructor(command: CreateCourseCommand) {
        require(command.participantsLimit > 0) { "`participantsLimit` should be positive number" }
        require(command.cost > 0) { "`cost` should be positive number" }
        AggregateLifecycle.apply(CourseCreatedEvent(UUID.randomUUID().toString(), command.name, command.participantsLimit, command.cost))
    }

    @CommandHandler
    fun handle(command: AddStudentToCourseCommand) {
        //TODO verify limit
        //TODO verify if student already on course
        AggregateLifecycle.apply(StudentAddedToCourseEvent(command.courseId, command.studentId, name!!, cost!!))
    }

    @EventSourcingHandler
    fun on(event: CourseCreatedEvent) {
        courseId = event.courseId
        name = event.name
        participantsLimit = event.participantsLimit
        cost = event.cost
    }

    @EventSourcingHandler
    fun on(event: StudentAddedToCourseEvent) {
        students.add(event.studentId)
    }
}