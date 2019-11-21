package com.github.artemych.analytics_annotation

import com.github.artemych.analytics.AnalyticAction
import com.github.artemych.analytics.AnalyticCategory
import com.github.artemych.analytics.AnalyticLabel

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class AnalyticEvent(
        val category: AnalyticCategory,
        val label: AnalyticLabel,
        val action: AnalyticAction,
        val additionalEvents: Array<AnalyticEvent> = [],
        val additionalEventKeys: Array<String> = []
)
