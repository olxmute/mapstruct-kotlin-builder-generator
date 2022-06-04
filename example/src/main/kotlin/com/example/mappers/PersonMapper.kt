package com.example.mappers

import com.example.dto.Person
import com.example.dto.Role
import com.netflix.dgs.codegen.generated.types.PersonDto
import com.netflix.dgs.codegen.generated.types.RoleDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface PersonMapper {

    fun toRoleDto(person: Role): RoleDto

    @Mapping(target = "firstName", source = "person.name")
    @Mapping(target = "lastName", source = "person.surname")
    fun toPersonDto(person: Person): PersonDto

}