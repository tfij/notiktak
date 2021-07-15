package pl.tfij.notiktak.course.query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pl.tfij.notiktak.course.coreapi.CourseCreatedEvent
import pl.tfij.notiktak.course.coreapi.FindCourseQuery
import pl.tfij.notiktak.course.coreapi.StudentAddedToCourseEvent

@Component
class CourseProjector(private val courseViewRepository: CourseViewRepository) {

    @EventHandler
    fun on(event: CourseCreatedEvent) {
        val courseView = CourseView(event.courseId, event.participantsLimit, event.cost, emptyList())
        courseViewRepository.save(courseView)
    }

    @EventHandler
    fun on(event: StudentAddedToCourseEvent) {
        courseViewRepository.findByIdOrNull(event.courseId)?.let {
            it.copy(vacanciesLeft = it.vacanciesLeft - 1, students = it.students.plus(StudentOnCourseView(event.studentId)))
        }?.let {
            courseViewRepository.save(it)
        }
    }

    @QueryHandler
    fun on(event: FindCourseQuery): CourseView? {
        return courseViewRepository.findByIdOrNull(event.courseId)
    }

}