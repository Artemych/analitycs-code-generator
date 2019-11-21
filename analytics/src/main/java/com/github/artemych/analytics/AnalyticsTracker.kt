package com.github.artemych.analytics

interface AnalyticsTracker {
    fun trackEvent(event: TrackEvent)
}