package com.example.mappers

import com.example.dto.Car
import com.example.dto.CarDto
import org.mapstruct.Mapper

@Mapper
interface CarMapper {

    fun toCarDto(car: Car): CarDto

}