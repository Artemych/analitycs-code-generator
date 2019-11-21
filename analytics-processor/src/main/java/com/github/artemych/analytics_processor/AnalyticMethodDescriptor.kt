package com.github.artemych.analytics_processor

import com.github.artemych.analytics.AnalyticAction
import com.github.artemych.analytics.AnalyticCategory
import com.github.artemych.analytics.AnalyticLabel
import com.github.artemych.analytics_annotation.AnalyticEvent
import com.squareup.kotlinpoet.ParameterSpec

data class AnalyticMethodDescriptor(
    val methodName: String,
    val category: AnalyticCategory,
    val action: AnalyticAction,
    val label: AnalyticLabel,
    val paramNamesSpecMap: Map<String, ParameterSpec>,
    val additionalEvents: List<AnalyticEvent>
)