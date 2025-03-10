package com.kotlinspring.course_catalogue_service.controller

import com.kotlinspring.course_catalogue_service.dto.InstructorDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
class InstructorControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorControllerMock: InstructorController

    @Test
    fun createInstructor() {
        val instructor = InstructorDto(null, "hello")
        val savedInstructor = InstructorDto(1, "hello")

        every { instructorControllerMock.createInstructor(instructor) } returns savedInstructor

        val result = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructor)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(InstructorDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(savedInstructor, result)
    }

    @Test
    fun addInstructor_Validation() {
        val instructor = InstructorDto(null, "")

        every { instructorControllerMock.createInstructor(instructor) } returns instructor

        val result = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructor)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("instructorDto name must not be blank", result)
    }

}