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
)

fun CourseEntity.toCourseDto() =
    CourseDto(
        id,
        name,
        category
    )