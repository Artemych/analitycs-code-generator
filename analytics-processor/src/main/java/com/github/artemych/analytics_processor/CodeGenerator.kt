package com.github.artemych.analytics_processor

import com.github.artemych.analytics.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

private const val TRACKER = "tracker"
private const val INDENT = "    "

class CodeGenerator {

    fun generate(descriptor: AnalyticsInteractorDescriptor): FileSpec {
        val typeSpecBuilder = TypeSpec.classBuilder(descriptor.className)
                .addModifiers(KModifier.FINAL)
                .addSuperinterface(descriptor.delegationInterface.asTypeName())
                .addProperty(
                        PropertySpec.builder(TRACKER, AnalyticsTracker::class)
                                .initializer(TRACKER)
                                .addModifiers(KModifier.PRIVATE)
                        .build())
                .primaryConstructor(
                        FunSpec.constructorBuilder()
                        .addParameter(TRACKER, AnalyticsTracker::class)
                        .build()
                )

        descriptor.methodDescriptors.forEach { methodDescriptor ->
            typeSpecBuilder.addFunction(buildTrackMethod(methodDescriptor))
        }

        val funcSpecBuilder = FunSpec.builder("trackEvent")
                .addParameter("category", AnalyticCategory::class.asClassName())
                .addParameter("action", AnalyticAction::class.asClassName())
                .addParameter("label", AnalyticLabel::class.asClassName())
                .addParameter("paramMap", MAP.parameterizedBy(String::class.asTypeName(), String::class.asTypeName()))
                .addModifiers(KModifier.PRIVATE)
                .addStatement("val event = TrackEvent(" +
                        "category = category, " +
                        "action = action, " +
                        "label = label, " +
                        "params = paramMap" +
                        ")")
                .addStatement("$TRACKER.trackEvent(event)")

        typeSpecBuilder.addFunction(funcSpecBuilder.build())

        return FileSpec.builder(descriptor.packageName, descriptor.className)
                .indent(INDENT)
                .addImport("com.github.artemych.analytics.TrackEvent", "")
                .addType(typeSpecBuilder.build())
                .build()
    }

    private fun buildTrackMethod(methodDescriptor: AnalyticMethodDescriptor): FunSpec {
        val funcSpecBuilder = FunSpec.builder(methodDescriptor.methodName)
                .addModifiers(KModifier.OVERRIDE)
                .addParameters(methodDescriptor.paramNamesSpecMap.values)

        funcSpecBuilder.addTrackEventSection(
                category = methodDescriptor.category,
                action = methodDescriptor.action,
                label = methodDescriptor.label,
                paramsStringMap = methodDescriptor.paramNamesSpecMap.mapValues { it.value.name }.toCodeMap()
        )

        methodDescriptor.additionalEvents.forEach { event ->
            funcSpecBuilder.addTrackEventSection(
                    category = event.category,
                    action = event.action,
                    label = event.label,
                    paramsStringMap = mapOf(
                        AnalyticParameter.EVENT.value to "${methodDescriptor.category} - ${methodDescriptor.label} ${methodDescriptor.action}"
                    ).toCodeMap(true)
            )
        }

        return funcSpecBuilder.build()
    }

    private fun FunSpec.Builder.addTrackEventSection(
            category: AnalyticCategory,
            action: AnalyticAction,
            label: AnalyticLabel,
        paramsStringMap: String) {
        addStatement("trackEvent(" +
                "category = com.github.artemych.analytics.AnalyticCategory.%L, " +
                "action = com.github.artemych.analytics.AnalyticAction.%L, " +
                "label = com.github.artemych.analytics.AnalyticLabel.%L, " +
                "paramMap = %L)",
                category,
                action,
                label,
                paramsStringMap
        )
    }

    private fun Map<String, String>.toCodeMap(asLiteralValue: Boolean = false): String {
        val sb = StringBuilder("mapOf<String, String>(")
        entries.forEachIndexed { index, entry ->
            sb.append("\"")
                .append(entry.key)
                .append("\"")
                .append(" to ")
            if (asLiteralValue) {
                sb.append("\"").append(entry.value).append("\"")
            } else {
                sb.append(entry.value).append(".toString()")
            }
            if (index < size - 1) {
                sb.append(",\n")
            }
        }
        sb.append(")")
        return sb.toString()
    }
}