package integration

import integration.fixture.BaseIntegrationSpec
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import pl.tfij.notiktak.course.coreapi.CreateCourseCommand
import pl.tfij.notiktak.course.coreapi.FindCourseQuery
import pl.tfij.notiktak.course.query.CourseView
import spock.util.concurrent.PollingConditions

class CourseIntegrationSpec extends BaseIntegrationSpec {

    @Autowired
    CommandGateway commandGateway

    @Autowired
    QueryGateway queryGateway

    def "Should return created course data"() {
        given: "Sample created course"
        String courseId = commandGateway.send(new CreateCourseCommand("TDD with Axon", 5, 25)).get()

        and: "wait until CourseView is ready"
        new PollingConditions().within(1) { queryGateway.query(new FindCourseQuery(courseId), ResponseTypes.instanceOf(CourseView)).get() != null }

        when: "I send query to fetch course data"
        CourseView courseView = queryGateway.query(new FindCourseQuery(courseId), ResponseTypes.instanceOf(CourseView)).get()

        then: "course data is valid"
        courseView.courseId == courseId
        courseView.vacanciesLeft == 5
        courseView.cost == 25
        courseView.students == []
    }
}
