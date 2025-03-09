package com.kotlinspring.course_catalogue_service.dto

import com.kotlinspring.course_catalogue_service.entity.CourseEntity
import jakarta.validation.constraints.NotBlank

data class CourseDto(
    val id: Int?,
    @get:NotBlank(message = "courseDto name must not be blank")
    val name: String,
    @get:NotBlank(message = "courseDto category must not be blank")
    val category: String
)

fun CourseDto.toCourseEntity() =
    CourseEntity(
        id = null,
        name,
        category
    )