package com.github.olxmute

import com.google.auto.service.AutoService
import org.mapstruct.ap.spi.BuilderInfo
import org.mapstruct.ap.spi.BuilderProvider
import org.mapstruct.ap.spi.DefaultBuilderProvider
import org.mapstruct.ap.spi.TypeHierarchyErroneousException
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter

@AutoService(BuilderProvider::class)
class KotlinBuilderProvider : DefaultBuilderProvider(), BuilderProvider {
    private val KOTLIN_METADATA_CLASS_NAME = "kotlin.Metadata"
    private val BUILDER_CLASS_POSTFIX = "Builder"
    private val CREATE_METHOD_NAME = "build"

    override fun findBuilderInfo(type: TypeMirror?): BuilderInfo? {
        val typeElement = getTypeElement(type) ?: return null
        return if (isAnnotatedByKotlin(typeElement)) {
            findBuilder(typeElement)
        } else {
            super.findBuilderInfo(typeElement)
        }
    }

    private fun findBuilder(typeElement: TypeElement): BuilderInfo? {
        val builderTypeElement = asBuilderElement(typeElement) ?: throw TypeHierarchyErroneousException(typeElement)

        val builderMethods = ElementFilter.methodsIn(builderTypeElement.enclosedElements)
            .filter { CREATE_METHOD_NAME == it?.simpleName?.toString() }

        val builderConstructor = ElementFilter.constructorsIn(builderTypeElement.enclosedElements)[0]
        return BuilderInfo.Builder()
            .builderCreationMethod(builderConstructor)
            .buildMethod(builderMethods)
            .build()
    }

    private fun asBuilderElement(typeElement: TypeElement): TypeElement? {
        val enclosingElement = typeElement.enclosingElement
        val builderQualifiedName = StringBuilder()
        if (enclosingElement.kind == ElementKind.PACKAGE) {
            builderQualifiedName.append((enclosingElement as PackageElement).qualifiedName.toString())
        } else {
            builderQualifiedName.append((enclosingElement as TypeElement).qualifiedName.toString())
        }
        if (builderQualifiedName.isNotEmpty()) {
            builderQualifiedName.append(".")
        }
        builderQualifiedName.append(typeElement.simpleName).append(BUILDER_CLASS_POSTFIX)
        return elementUtils.getTypeElement(builderQualifiedName)
    }

    private fun isAnnotatedByKotlin(typeElement: TypeElement): Boolean {
        return typeElement.annotationMirrors
            .any { KOTLIN_METADATA_CLASS_NAME == it?.annotationType?.toString() }
    }
}