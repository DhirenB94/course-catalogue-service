package com.kotlinspring.course_catalogue_service.controller

import com.kotlinspring.course_catalogue_service.dto.InstructorDto
import com.kotlinspring.course_catalogue_service.repository.InstructorRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class InstructorControllerIntTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        instructorRepository.deleteAll()
    }

    @Test
    fun createInstructor() {
        val instructorDto = InstructorDto(id = null, "dhiren")
        val result =
            webTestClient.post()
                .uri("v1/instructors")
                .bodyValue(instructorDto)
                .exchange()
                .expectStatus().isCreated
                .expectBody(InstructorDto::class.java)
                .returnResult()
                .responseBody

        Assertions.assertNotNull(result?.id)
    }
}