package com.kotlinspring.course_catalogue_service.controller

import com.kotlinspring.course_catalogue_service.dto.CourseDto
import com.kotlinspring.course_catalogue_service.entity.CourseEntity
import com.kotlinspring.course_catalogue_service.entity.Instructor
import com.kotlinspring.course_catalogue_service.repository.CourseRepository
import com.kotlinspring.course_catalogue_service.repository.InstructorRepository
import com.kotlinspring.course_catalogue_service.util.PostgreSQLContainerInitialiser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient

class CourseControllerIntTest : PostgreSQLContainerInitialiser() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()
        val instructor = instructorEntity()
        instructorRepository.save(instructor)
        courseRepository.saveAll(courseEntityList(instructor))
    }

    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()
        val courseDto = CourseDto(id = null, name = "hello", category = "action", instructor.id)

        val result =
            webTestClient.post()
                .uri("v1/courses")
                .bodyValue(courseDto)
                .exchange()
                .expectStatus().isCreated
                .expectBody(CourseDto::class.java)
                .returnResult()
                .responseBody

        Assertions.assertNotNull(result?.id)
    }

    @Test
    fun retrieveCourses() {
        val results =
            webTestClient.get()
                .uri("v1/courses")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(CourseDto::class.java)
                .returnResult()
                .responseBody

        Assertions.assertEquals(3, results?.size)
    }

    @Test
    fun retrieveCoursesByName() {

        val uri = UriComponentsBuilder.fromUriString("v1/courses")
            .queryParam("name", "nameT")
            .toUriString()
        val results =
            webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(CourseDto::class.java)
                .returnResult()
                .responseBody

        Assertions.assertEquals(2, results?.size)
    }

    @Test
    fun updateCourse() {
        val instructor = instructorRepository.findAll().first()
        val course = CourseEntity(id = null, name = "someCourse", category = "someCategory", instructor)
        val savedCourse = courseRepository.save(course)

        val updatedCourse =
            CourseDto(id = null, name = "updatedCourseName", category = "updatedCategory", instructor.id)
        val result =
            webTestClient.put()
                .uri("/v1/courses/{id}", savedCourse.id)
                .bodyValue(updatedCourse)
                .exchange()
                .expectStatus().isOk
                .expectBody(CourseDto::class.java)
                .returnResult()
                .responseBody

        Assertions.assertEquals(4, result?.id)
        Assertions.assertEquals(updatedCourse.name, result?.name)
        Assertions.assertEquals(updatedCourse.category, result?.category)
    }

    @Test
    fun deleteCourse() {
        val instructor = instructorRepository.findAll().first()
        val course = CourseEntity(id = null, name = "someCourse", category = "someCategory", instructor)
        val savedCourse = courseRepository.save(course)

        webTestClient.delete()
            .uri("/v1/courses/{id}", savedCourse.id)
            .exchange()
            .expectStatus().isNoContent
    }
}

fun courseEntityList(instructor: Instructor? = null) = listOf(
    CourseEntity(
        null,
        "nameOne",
        "categoryOne",
        instructor
    ),
    CourseEntity(
        null,
        "nameTwo",
        "categoryTwo",
        instructor
    ),
    CourseEntity(
        null,
        "nameThree",
        "categoryThree",
        instructor
    ),
)

fun instructorEntity(name: String = "hsshhs") = Instructor(null, name)