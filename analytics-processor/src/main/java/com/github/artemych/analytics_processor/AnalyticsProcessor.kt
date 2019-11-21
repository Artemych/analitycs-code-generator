package com.github.artemych.analytics_processor

import com.github.artemych.analytics_annotation.AnalyticEvent
import com.github.artemych.analytics_annotation.AnalyticsInteractor
import com.github.artemych.analytics_annotation.Param
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(AnalyticsProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnalyticsProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private lateinit var messenger: Messenger
    private lateinit var codeWriter: CodeWriter

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        messenger = Messenger(processingEnvironment)
        codeWriter = CodeWriter(processingEnvironment)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        for (element in roundEnv!!.getElementsAnnotatedWith(AnalyticsInteractor::class.java)) {
            if (element.kind == ElementKind.INTERFACE) {
                processInteractor(element as TypeElement)
            } else {
                messenger.error(element, "Only interfaces can be annotated with " + AnalyticsInteractor::class.java.name)
            }
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AnalyticsInteractor::class.java.canonicalName)
    }

    private fun processInteractor(typeElement: TypeElement) {
        val interactorAnnotation = typeElement.getAnnotation(AnalyticsInteractor::class.java)
        val methodDescriptors = typeElement
                .enclosedElements
                .filter { it.kind == ElementKind.METHOD }
                .map { processMethod(it as ExecutableElement, interactorAnnotation) }

        val interactorDescriptor = AnalyticsInteractorDescriptor(
                delegationInterface = typeElement.asType(),
                packageName = typeElement.getPackageName(),
                className = typeElement.simpleName.toString() + "_Analytics",
                methodDescriptors = methodDescriptors
        )
        codeWriter.write(interactorDescriptor, typeElement)
    }

    private fun processMethod(element: ExecutableElement, interactorAnnotation: AnalyticsInteractor): AnalyticMethodDescriptor {
        val eventAnnotation = element.getAnnotation(AnalyticEvent::class.java)
        val parameters = element.parameters
        val map = LinkedHashMap<String, ParameterSpec>()
        val additionalEvents = mutableListOf<AnalyticEvent>()

        val interactorAdditionalEventsMap = interactorAnnotation
                .additionalEventsMappings
                .associate { mapping ->
                    mapping.key to mapping.event
                }

        additionalEvents.addAll(eventAnnotation.additionalEvents)

        val interactorAdditionalEvents = eventAnnotation.additionalEventKeys.mapNotNull { key ->
            interactorAdditionalEventsMap[key]
        }

        additionalEvents.addAll(interactorAdditionalEvents)

        parameters.forEach { parameter ->
            val spec = ParameterSpec.builder(
                    parameter.simpleName.toString(),
                    parameter.asType().asTypeName().javaToKotlinType()
            ).build()

            val nameAnnotation = parameter.getAnnotation(Param::class.java)
            val paramName = nameAnnotation?.value?.value ?: parameter.simpleName.toString()
            map[paramName] = spec
        }

        return AnalyticMethodDescriptor(
                methodName = element.simpleName.toString(),
                category = eventAnnotation.category,
                action = eventAnnotation.action,
                label = eventAnnotation.label,
                paramNamesSpecMap = map,
                additionalEvents = additionalEvents
        )
    }

    private fun Element.getPackageName(): String {
        var enclosing = this.enclosingElement
        while (enclosing.kind != ElementKind.PACKAGE) {
            enclosing = enclosing.enclosingElement
        }
        return (enclosing as PackageElement).qualifiedName.toString()
    }

    private fun TypeName.javaToKotlinType(): TypeName {
        val className = JavaToKotlinClassMap.INSTANCE
                .mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
        return if (className == null) this
        else ClassName.bestGuess(className)
    }
}