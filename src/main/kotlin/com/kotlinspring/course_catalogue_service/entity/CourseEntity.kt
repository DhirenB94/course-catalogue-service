package com.kotlinspring.course_catalogue_service.entity

import com.kotlinspring.course_catalogue_service.dto.CourseDto
import jakarta.persistence.*

@Entity
@Table(name = "Courses")
data class CourseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var name: String,
    var category: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTRUCTOR_ID", nullable = false)
    val instructor: Instructor? = null
) {
    override fun toString(): String {
        return "Course(id=$id, name='$name', category='$category', instructor=${instructor!!.id})"
    }
}

fun CourseEntity.toCourseDto() =
    CourseDto(
        id,
        name,
        category,
        instructor?.id
    )