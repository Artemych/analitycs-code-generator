package com.github.artemych.analytics_processor

import javax.lang.model.type.TypeMirror

data class AnalyticsInteractorDescriptor(
    val delegationInterface: TypeMirror,
    val packageName: String,
    val className: String,
    val methodDescriptors: List<AnalyticMethodDescriptor>
)