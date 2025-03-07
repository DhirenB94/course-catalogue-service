package com.kotlinspring.course_catalogue_service.repository

import com.kotlinspring.course_catalogue_service.entity.CourseEntity
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<CourseEntity, Int> {
}