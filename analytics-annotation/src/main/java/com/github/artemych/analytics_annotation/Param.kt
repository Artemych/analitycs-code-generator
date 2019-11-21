package com.github.artemych.analytics_annotation

import com.github.artemych.analytics.AnalyticParameter

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param(val value: AnalyticParameter)
