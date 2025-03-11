package com.kotlinspring.course_catalogue_service.service

import com.kotlinspring.course_catalogue_service.dto.CourseDto
import com.kotlinspring.course_catalogue_service.entity.CourseEntity
import com.kotlinspring.course_catalogue_service.entity.toCourseDto
import com.kotlinspring.course_catalogue_service.exception.CourseNotFoundException
import com.kotlinspring.course_catalogue_service.exception.InstructorNotValidException
import com.kotlinspring.course_catalogue_service.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val instructorService: InstructorService
) {

    companion object : KLogging()

    fun addCourse(courseDto: CourseDto): CourseDto {

        val instructorOptional = instructorService.findByInstructorId(courseDto.instructorId!!)

        if (!instructorOptional.isPresent) {
            throw InstructorNotValidException("instructor not valid for Id: ${courseDto.instructorId}")
        }

        val courseEntity = courseDto.let {
            CourseEntity(id = null, it.name, it.category, instructorOptional.get())
        }

        courseRepository.save(courseEntity)
        logger.info("Saved course is $courseEntity")

        return courseEntity.let {
            CourseDto(it.id, it.name, it.category, it.instructor?.id)
        }
    }

    fun retrieveCourses(name: String?): List<CourseDto> {
        val courses = name?.let {
            courseRepository.findByNameContaining(name)
        } ?: courseRepository.findAll()

        return courses
            .map { it.toCourseDto() }
    }

    fun updateCourse(id: Int, courseDto: CourseDto): CourseDto {
        val foundCourse = courseRepository.findById(id)
        return if (foundCourse.isPresent) {
            foundCourse.get()
                .let {
                    it.name = courseDto.name
                    it.category = courseDto.category
                    courseRepository.save(it)
                        .toCourseDto()
                }
        } else {
            throw CourseNotFoundException("No course found with id: $id")
        }
    }

    fun deleteCourse(id: Int) {
        val foundCourse = courseRepository.findById(id)
        if (foundCourse.isPresent) {
            foundCourse.get()
                .let {
                    courseRepository.deleteById(id)
                }
        } else {
            throw CourseNotFoundException("No course found with id: $id")
        }
    }
}