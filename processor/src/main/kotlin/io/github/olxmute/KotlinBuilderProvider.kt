package io.github.olxmute

import com.google.auto.service.AutoService
import io.github.olxmute.processor.isDataClass
import org.mapstruct.ap.spi.BuilderInfo
import org.mapstruct.ap.spi.BuilderProvider
import org.mapstruct.ap.spi.DefaultBuilderProvider
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter

@AutoService(BuilderProvider::class)
class KotlinBuilderProvider : DefaultBuilderProvider(), BuilderProvider {
    private val BUILDER_CLASS_POSTFIX = "Builder"
    private val CREATE_METHOD_NAME = "build"

    override fun findBuilderInfo(type: TypeMirror?): BuilderInfo? {
        val typeElement = getTypeElement(type) ?: return null

        val builder = if (typeElement.isDataClass()) findBuilder(typeElement) else null

        return builder ?: super.findBuilderInfo(typeElement)
    }

    private fun findBuilder(typeElement: TypeElement): BuilderInfo? {
        val builderTypeElement = asBuilderElement(typeElement) ?: return null

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

}