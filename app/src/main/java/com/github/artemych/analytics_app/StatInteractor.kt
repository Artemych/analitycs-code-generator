package com.github.artemych.analytics_app

import com.github.artemych.analytics.AnalyticAction.CLICK
import com.github.artemych.analytics.AnalyticCategory.GENERAL
import com.github.artemych.analytics.AnalyticLabel.LABEL
import com.github.artemych.analytics.AnalyticParameter.TEST_PARAM
import com.github.artemych.analytics_annotation.AdditionalEventsMapping
import com.github.artemych.analytics_annotation.AnalyticEvent
import com.github.artemych.analytics_annotation.AnalyticsInteractor
import com.github.artemych.analytics_annotation.Param

private const val NEW_EVENT_KEY = "new_event"

@AnalyticsInteractor(
    additionalEventsMappings = [
        AdditionalEventsMapping(
            key = NEW_EVENT_KEY,
            event = AnalyticEvent(
                label = LABEL,
                category = GENERAL,
                action = CLICK
            )
        )
    ]
)
interface StatInteractor {

    @AnalyticEvent(
        label = LABEL,
        category = GENERAL,
        action = CLICK
    )
    fun simpleLogMethod(@Param(TEST_PARAM) param: String, count: Long)

    @AnalyticEvent(
        label = LABEL,
        category = GENERAL,
        action = CLICK,
        additionalEventKeys = [NEW_EVENT_KEY]
    )
    fun midDifficultLogMethod(@Param(TEST_PARAM) param: String, count: Long)

    @AnalyticEvent(
        label = LABEL,
        category = GENERAL,
        action = CLICK,
        additionalEvents = [
            AnalyticEvent(
                    label = LABEL,
                    category = GENERAL,
                    action = CLICK
            )
        ]
    )
    fun midDifficultWithAdditionalEventLogMethod(@Param(TEST_PARAM) param: String, count: Long)

    @AnalyticEvent(
        label = LABEL,
        category = GENERAL,
        action = CLICK,
        additionalEvents = [
            AnalyticEvent(
                label = LABEL,
                category = GENERAL,
                action = CLICK
            )
        ],
        additionalEventKeys = [NEW_EVENT_KEY]
    )
    fun complicatedLogMethod(@Param(TEST_PARAM) param: String, count: Long)
}
