package pl.tfij.notiktak.student.saga

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired
import pl.tfij.notiktak.course.coreapi.AddStudentToCourseCommand
import pl.tfij.notiktak.course.coreapi.FindCourseQuery
import pl.tfij.notiktak.course.coreapi.StudentAddedToCourseEvent
import pl.tfij.notiktak.course.query.CourseView
import pl.tfij.notiktak.student.coreapi.ChargeStudentCourseFeeCommand
import pl.tfij.notiktak.student.coreapi.FindStudentQuery
import pl.tfij.notiktak.student.coreapi.StudentChargedCourseFeeEvent
import pl.tfij.notiktak.student.coreapi.StudentOnCourseRegistrationStartedEvent
import pl.tfij.notiktak.student.query.StudentView

@Saga
class StudentOnCourseRegistrationSaga {
    @Autowired
    @Transient
    private var commandGateway: CommandGateway? = null

    @Autowired
    @Transient
    private var queryGateway: QueryGateway? = null

    var courseId: String? = null
    var studentId: String? = null

    constructor() {
        // Required by Axon
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "studentOnCourseRegistrationId")
    fun handle(event: StudentOnCourseRegistrationStartedEvent) {
        SagaLifecycle.associateWith("courseId", event.courseId)
        SagaLifecycle.associateWith("studentId", event.studentId)
        courseId = event.courseId
        studentId = event.studentId
        val courseView = queryGateway!!.query(FindCourseQuery(event.courseId), ResponseTypes.instanceOf(CourseView::class.java)).get() //todo get()
        val studentView = queryGateway!!.query(FindStudentQuery(event.studentId), ResponseTypes.instanceOf(StudentView::class.java)).get() //todo get()
        if (courseView.vacanciesLeft == 0) {
            throw RuntimeException()
        }
        if (studentView.credit < courseView.cost) {
            throw RuntimeException()
        }
        commandGateway!!.send<Any>(AddStudentToCourseCommand(event.courseId, event.studentId))
            .whenComplete { _, throwable -> if (throwable != null) {
                SagaLifecycle.end()
            } }
    }

    @SagaEventHandler(associationProperty = "courseId")
    fun handle(event: StudentAddedToCourseEvent) {
        if (event.studentId != studentId) {
            return
        }
        commandGateway!!.send<Any>(ChargeStudentCourseFeeCommand(event.studentId, event.courseId, event.courseCost))
            .whenComplete { _, throwable -> if (throwable != null) {
                SagaLifecycle.end()
            } }
    }

    @SagaEventHandler(associationProperty = "studentId")
    fun handle(event: StudentChargedCourseFeeEvent) {
        if (event.courseId != courseId) {
            return
        }
        SagaLifecycle.end()
    }

}