package integration

import integration.fixture.BaseIntegrationSpec
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import pl.tfij.notiktak.course.coreapi.CreateCourseCommand
import pl.tfij.notiktak.course.coreapi.FindCourseQuery
import pl.tfij.notiktak.course.query.CourseView
import pl.tfij.notiktak.course.query.StudentOnCourseView
import pl.tfij.notiktak.student.coreapi.FindStudentQuery
import pl.tfij.notiktak.student.coreapi.RegisterOnCourseCommand
import pl.tfij.notiktak.student.query.StudentCourseView
import pl.tfij.notiktak.student.query.StudentView
import pl.tfij.notiktak.users.coreapi.CreateUserCommand
import pl.tfij.notiktak.users.coreapi.Role

class RegisterUserOnCourseIntegrationSpec extends BaseIntegrationSpec {

    @Autowired
    CommandGateway commandGateway

    @Autowired
    QueryGateway queryGateway

    def "Should register user on course when student has enough credit and the course participant limit has not been reached"() {
        given: "the sample student wit 100 credit granted"
        int initStudentCredit = 100
        String studentId = commandGateway.send(new CreateUserCommand("karl.anderson@example.com", new Role.Student(initStudentCredit))).get()

        and: "course for 5 students and cost of 20 for each"
        int courseCost = 20
        String courseId = commandGateway.send(new CreateCourseCommand("TDD with Axon", 5, courseCost)).get()

        when: "I register student on course"
        sleep(2000)
        commandGateway.send(new RegisterOnCourseCommand(studentId, courseId)).get()

        then: "the student is registered"
        sleep(2000)
        StudentView studentView = queryGateway.query(new FindStudentQuery(studentId), ResponseTypes.instanceOf(StudentView)).get()
        studentView.onCourses == [
                new StudentCourseView(courseId, "TDD with Axon", courseCost)
        ]

        and: "student credit is reduced by cost of course"
        studentView.credit == initStudentCredit - courseCost

        and: "4 vacancies left on the course"
        CourseView courseView = queryGateway.query(new FindCourseQuery(courseId), ResponseTypes.instanceOf(CourseView)).get()
        courseView.vacanciesLeft == 4

        and: "one student is registered on the course"
        courseView.students == [
                new StudentOnCourseView(studentId)
        ]
    }

    def "Should not register user on course when student hasn't enough credit and the course participant limit has not been reached"() {

    }

    def "Should not register user on course when student has enough credit and the course participant limit has been reached"() {

    }

}
