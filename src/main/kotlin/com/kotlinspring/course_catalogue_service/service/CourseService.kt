package com.kotlinspring.course_catalogue_service.service

import com.kotlinspring.course_catalogue_service.dto.CourseDto
import com.kotlinspring.course_catalogue_service.dto.toCourseEntity
import com.kotlinspring.course_catalogue_service.entity.toCourseDto
import com.kotlinspring.course_catalogue_service.exception.CourseNotFoundException
import com.kotlinspring.course_catalogue_service.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository
) {

    companion object : KLogging()

    fun addCourse(courseDto: CourseDto): CourseDto {
        return courseRepository.save(courseDto.toCourseEntity())
            .also { logger.info("Saved course is $it") }
            .toCourseDto()
    }

    fun retrieveCourses() : List<CourseDto> {
        return courseRepository.findAll()
            .map {it.toCourseDto()}
    }

    fun updateCourse(id: Int, courseDto: CourseDto) : CourseDto {
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