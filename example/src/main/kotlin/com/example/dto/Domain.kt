package com.example.dto

data class Car(
    val model: String,
    val series: String,
    val year: Int
)

data class CarDto(
    val model: String? = null,
    val series: String? = null,
    val year: Int? = null
)

data class Role(
    val id: Int,
    val name: String,
    val abbreviation: String,
)

data class Person(
    val name: String,
    val surname: String,
    val phone: String?,
    val age: Int,
    val role: Role?
)

data class Department(
    val name: String,
    val code: String
)

data class Course(
    val id: String,
    val name: String,
    val description: String,
    val teacher: Person,
    val students: List<Person>,
    val department: Department
)
