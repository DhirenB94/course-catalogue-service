package com.kotlinspring.course_catalogue_service.entity

import com.kotlinspring.course_catalogue_service.dto.InstructorDto
import jakarta.persistence.*

@Entity
@Table(name = "INSTRUCTORS")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    val name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var courses: MutableList<CourseEntity> = mutableListOf()
)

fun Instructor.toInstructorDto() =
    InstructorDto(
        id,
        name
    )