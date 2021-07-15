package pl.tfij.notiktak.student.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.tfij.notiktak.logger
import pl.tfij.notiktak.student.coreapi.ChargeStudentCommand
import pl.tfij.notiktak.student.coreapi.RegisterOnCourseCommand
import pl.tfij.notiktak.student.coreapi.StudentCharged
import pl.tfij.notiktak.student.coreapi.StudentCreatedEvent
import pl.tfij.notiktak.student.coreapi.StudentOnCourseRegistrationStartedEvent
import java.util.*

@Aggregate
class Student {
    @AggregateIdentifier
    var studentId: String? = null
    var credit: Int? = null

    constructor() {
        // Required by Axon
    }

    constructor(studentId: String, credit: Int) {
        logger.info("StudentConstructor: studentId = $studentId")
        AggregateLifecycle.apply(StudentCreatedEvent(studentId, credit))
    }

    @CommandHandler
    fun handle(command: RegisterOnCourseCommand) {
        AggregateLifecycle.apply(StudentOnCourseRegistrationStartedEvent(UUID.randomUUID().toString(), command.studentId, command.courseId))
    }

    @CommandHandler
    fun handle(command: ChargeStudentCommand) {
        //TODO verify credit
        AggregateLifecycle.apply(StudentCharged(command.studentId, command.cost))
    }

    @EventSourcingHandler
    fun on(event: StudentCreatedEvent) {
        logger.info("studentId = $studentId")
        studentId = event.studentId
        credit = event.credit
    }

    @EventSourcingHandler
    fun on(event: StudentCharged) {
        credit = credit!! - event.cost
    }

    companion object {
        private val logger by logger()
    }
}