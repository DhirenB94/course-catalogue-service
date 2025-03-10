package com.kotlinspring.course_catalogue_service.controller

import com.kotlinspring.course_catalogue_service.dto.InstructorDto
import com.kotlinspring.course_catalogue_service.service.InstructorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(
    private val instructorService: InstructorService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@RequestBody @Valid instructorDto: InstructorDto): InstructorDto {
        return instructorService.createInstructor(instructorDto)
    }
}