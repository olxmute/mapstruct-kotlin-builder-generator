package com.example.mappers

import com.example.dto.Course
import com.example.dto.Department
import com.netflix.dgs.codegen.generated.types.CourseDto
import com.netflix.dgs.codegen.generated.types.DepartmentDto
import org.mapstruct.Mapper

@Mapper(uses = [PersonMapper::class])
interface CourseMapper {

    fun toDepartmentDto(department: Department): DepartmentDto

    fun toCourseDto(course: Course): CourseDto

}
