package com.kotlinspring.course_catalogue_service.repository

import com.kotlinspring.course_catalogue_service.entity.CourseEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<CourseEntity, Int> {

    fun findByNameContaining(name: String) : List<CourseEntity>

    @Query(value = "SELECT * FROM COURSES WHERE NAME LIKE %?1%", nativeQuery = true)
    fun findCoursesByName(name: String) : List<CourseEntity>

}