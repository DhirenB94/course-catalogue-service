package com.kotlinspring.course_catalogue_service.repository

import com.kotlinspring.course_catalogue_service.controller.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        courseRepository.saveAll(courseEntityList())
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("nameT")

        Assertions.assertEquals(2, courses.size)
    }

    @Test
    fun findCoursesByName() {
        val courses = courseRepository.findCoursesByName("nameT")

        Assertions.assertEquals(2, courses.size)
    }

    @ParameterizedTest
    @MethodSource("courseNameAndSize")
    fun findByNameContainingParametrised(name: String, expectedSize: Int) {

        val courses = courseRepository.findByNameContaining(name)
        Assertions.assertEquals(expectedSize, courses.size)

    }

    companion object {

        @JvmStatic
        fun courseNameAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("nameT", 2),
                Arguments.arguments("name", 3)
            )
        }
    }
}