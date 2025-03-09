package com.kotlinspring.course_catalogue_service.controller

import com.kotlinspring.course_catalogue_service.dto.CourseDto
import com.kotlinspring.course_catalogue_service.dto.toCourseEntity
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient


@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseControllerMock: CourseController

    @Test
    fun retrieveCourses() {
        val course1 = CourseDto(null, "1", "1")
        val course2 = CourseDto(null, "2", "2")

        every { courseControllerMock.retrieveCourses() } returns listOf(course1, course2)

        val results = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, results?.size)
    }

    @Test
    fun addCourse() {
        val course = CourseDto(null, "1", "1")
        val savedCourse = CourseDto(1, "1", "1")

        every { courseControllerMock.addCourse(course) } returns savedCourse

        val result = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(course)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(savedCourse, result)
    }

    @Test
    fun addCourseValidation() {
        val course = CourseDto(null, "", "category")
        every { courseControllerMock.addCourse(course) } returns course

        val result = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(course)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("courseDto name must not be blank", result)
    }

    @Test
    fun addCourseRuntimeExcpetion() {
        val course = CourseDto(null, "name", "category")

        val errorMessage = "unexpected error occurred"

        every { courseControllerMock.addCourse(course) } throws RuntimeException(errorMessage)

        val result = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(course)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, result)
    }

    @Test
    fun updateCourseExists() {
        val course = CourseDto(1, "1", "1")
        val updatedCourse = CourseDto(1, "100", "100")

        every { courseControllerMock.updateCourse(any(), any()) } returns updatedCourse

        val result = webTestClient.put()
            .uri("/v1/courses/{id}", course.id)
            .bodyValue(updatedCourse)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(updatedCourse.name, result?.name)
        Assertions.assertEquals(updatedCourse.category, result?.category)
    }

    @Test
    fun updateCourseDoesNotExist() {
        val nonExistentCourseId = 999
        val updatedCourse = CourseDto(nonExistentCourseId, "Updated Name", "Updated Category")

        every { courseControllerMock.updateCourse(updatedCourse, nonExistentCourseId) } throws Exception()

        assertThrows<Exception> {
            webTestClient.put()
                .uri("/v1/courses/{id}", nonExistentCourseId)
                .bodyValue(updatedCourse)
                .exchange()
        }
    }

    @Test
    fun deleteCourse() {
        every { courseControllerMock.deleteCourse(any()) } just runs

        webTestClient.delete()
            .uri("/v1/courses/1")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun deleteCourseDoesNotExist() {
        every { courseControllerMock.deleteCourse(any()) } throws Exception()

        assertThrows<Exception> {
            webTestClient.delete()
                .uri("/v1/courses/100")
                .exchange()
        }
    }
}