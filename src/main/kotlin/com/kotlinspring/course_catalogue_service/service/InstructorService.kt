package com.kotlinspring.course_catalogue_service.service

import com.kotlinspring.course_catalogue_service.dto.InstructorDto
import com.kotlinspring.course_catalogue_service.dto.toEntity
import com.kotlinspring.course_catalogue_service.entity.Instructor
import com.kotlinspring.course_catalogue_service.entity.toInstructorDto
import com.kotlinspring.course_catalogue_service.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(
    private val instructorRepository: InstructorRepository
) {
    fun createInstructor(instructorDto: InstructorDto): InstructorDto {
        return instructorRepository.save(instructorDto.toEntity())
            .toInstructorDto()
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }
}