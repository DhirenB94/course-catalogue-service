package com.kotlinspring.course_catalogue_service.controller

import com.kotlinspring.course_catalogue_service.dto.CourseDto
import com.kotlinspring.course_catalogue_service.dto.toCourseEntity
import com.kotlinspring.course_catalogue_service.entity.CourseEntity
import com.kotlinspring.course_catalogue_service.repository.CourseRepository
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
class CourseControllerIntTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        courseRepository.saveAll(courseEntityList())
    }

    @Test
    fun addCourse() {
        val courseDto = CourseDto(id = null, name = "hello", category = "action")

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
        val course = CourseDto(id = null, name = "someCourse", category = "someCategory")
        val savedCourse = courseRepository.save(course.toCourseEntity())

        val updatedCourse = CourseDto(id = null, name = "updatedCourseName", category = "updatedCategory")
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
        val course = CourseDto(id = null, name = "someCourse", category = "someCategory")
        val savedCourse = courseRepository.save(course.toCourseEntity())

        webTestClient.delete()
            .uri("/v1/courses/{id}", savedCourse.id)
            .exchange()
            .expectStatus().isNoContent
    }
}

fun courseEntityList() = listOf(
    CourseEntity(
        null,
        "nameOne",
        "categoryOne"
    ),
    CourseEntity(
        null,
        "nameTwo",
        "categoryTwo"
    ),
    CourseEntity(
        null,
        "nameThree",
        "categoryThree"
    ),
)