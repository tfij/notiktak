package pl.tfij.notiktak.student.query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pl.tfij.notiktak.course.coreapi.StudentAddedToCourseEvent
import pl.tfij.notiktak.logger
import pl.tfij.notiktak.student.coreapi.FindStudentQuery
import pl.tfij.notiktak.student.coreapi.StudentChargedCourseFeeEvent
import pl.tfij.notiktak.users.coreapi.Role
import pl.tfij.notiktak.users.coreapi.UserCreatedEvent

@Component
class StudentViewProvider(private val studentViewRepository: StudentViewRepository) {

    @EventHandler
    fun on(event: UserCreatedEvent) {//TODO change to StudentCreatedEvent
        try {
            if (event.role is Role.Student) {
                val insert = studentViewRepository.insert(StudentView(event.userId, event.role.credit, emptyList()))
                logger.info("StudentViewProvider.onUserCreatedEvent : " + insert.toString())
            }
        } catch (ex: Exception) {
            logger.error("error creating student view", ex)
            throw ex
        }
    }

    @EventHandler
    fun on(event: StudentAddedToCourseEvent) {
        studentViewRepository.findByIdOrNull(event.studentId)?.let {
            it.copy(onCourses = it.onCourses.plus(StudentCourseView(event.courseId, event.courseName, event.courseCost)))
        }?.let {
            studentViewRepository.save(it)
        }
    }

    @EventHandler
    fun on(event: StudentChargedCourseFeeEvent) {
        studentViewRepository.findByIdOrNull(event.studentId)?.let {
            it.copy(credit = it.credit - event.cost)
        }?.let {
            studentViewRepository.save(it)
        }
    }

    @QueryHandler
    fun on(query: FindStudentQuery): StudentView? {
        return studentViewRepository.findByIdOrNull(query.studentId)
    }

    companion object {
        private val logger by logger()
    }
}