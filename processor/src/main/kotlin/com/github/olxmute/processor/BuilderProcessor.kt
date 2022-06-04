package com.github.olxmute.processor

import com.google.auto.service.AutoService
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.ElementFilter

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.mapstruct.Mapper")
class BuilderProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val types = roundEnv.getElementsAnnotatedWith(Class.forName("org.mapstruct.Mapper") as Class<Annotation>)
            .asSequence()
            .flatMap { ElementFilter.methodsIn(it.enclosedElements) }
            .map { it.returnType as DeclaredType }
            .map { it.asElement() as TypeElement }
            .filter { it.isDataClass() }
            .distinct()

        types.forEach { generateBuilder(it) }

        return false
    }

    private fun generateBuilder(typeElement: TypeElement) {
        val packageName = processingEnv.elementUtils.getPackageOf(typeElement).toString()
        val builderClassName = "${typeElement.simpleName}Builder"

        val constructor = ElementFilter.constructorsIn(typeElement.enclosedElements)
            .filter { it.modifiers.contains(Modifier.PUBLIC) }
            .single { it.parameters.isNotEmpty() }

        val constructorParameters = constructor.parameters
        val className = ClassName.bestGuess(typeElement.qualifiedName.toString())

        val typeSpec = TypeSpec.classBuilder(builderClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addTypeVariables(typeElement.typeParameters.map { TypeVariableName.get(it) })
            .addFields(constructorParameters.map { variableElement ->
                FieldSpec.builder(
                    variableElement.type,
                    variableElement.name,
                    Modifier.PRIVATE
                ).build()
            })
            .addMethods(constructorParameters.map { variableElement ->
                val variableElementName = variableElement.name
                val variableElementType = variableElement.type

                MethodSpec.methodBuilder("set${variableElementName.capitalize()}")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.get(packageName, builderClassName))
                    .addParameter(variableElementType, variableElementName)
                    .addStatement("this.\$N = \$N", variableElementName, variableElementName)
                    .addStatement("return this")
                    .build()
            })
            .addMethod(
                MethodSpec.methodBuilder("build")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(className)
                    .addStatement("return new \$T(\$L)", className, constructorParameters.joinToString { it.name })
                    .build()
            )
            .build()

        JavaFile.builder(packageName, typeSpec)
            .build()
            .writeTo(processingEnv.filer)

    }
}
