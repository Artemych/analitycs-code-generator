package com.github.artemych.analytics

data class TrackEvent(
    val category: AnalyticCategory? = null,
    val action: AnalyticAction? = null,
    val label: AnalyticLabel? = null,
    val params: Map<String, String> = emptyMap()
)
