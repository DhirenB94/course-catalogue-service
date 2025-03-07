package com.kotlinspring.course_catalogue_service.dto

import com.kotlinspring.course_catalogue_service.entity.CourseEntity

data class CourseDto (
    val id: Int?,
    val name: String,
    val category: String
)

fun CourseDto.toCourseEntity() =
    CourseEntity(
        id = null,
        name,
        category
    )