package com.github.olxmute.processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmClassVisitor
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.Element
import javax.lang.model.element.VariableElement


fun Element.readHeader() = getAnnotation(Metadata::class.java)?.run {
    KotlinClassHeader(kind, metadataVersion, data1, data2, extraString, packageName, extraInt)
}

fun Element.isDataClass(): Boolean {
    val kotlinClassHeader = readHeader() ?: return false
    val kotlinClassMetadata = KotlinClassMetadata.read(kotlinClassHeader)

    var isDataClass = false
    if (kotlinClassMetadata is KotlinClassMetadata.Class) {
        kotlinClassMetadata.accept(object : KmClassVisitor() {
            override fun visit(flags: Flags, name: kotlinx.metadata.ClassName) {
                isDataClass = Flag.Class.IS_DATA(flags)
            }
        })
    }
    return isDataClass
}

val VariableElement.name: String
    get() = simpleName.toString()

val VariableElement.type: TypeName
    get() = ClassName.get(asType())