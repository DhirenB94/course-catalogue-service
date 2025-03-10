package com.kotlinspring.course_catalogue_service.dto

import com.kotlinspring.course_catalogue_service.entity.Instructor
import jakarta.validation.constraints.NotBlank

data class InstructorDto(
    val id: Int?,
    @get:NotBlank(message = "instructorDto name must not be blank")
    val name: String
)

fun InstructorDto.toEntity() =
    Instructor(
        id,
        name
    )