package com.github.artemych.analytics_app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.artemych.analytics.AnalyticsTracker
import com.github.artemych.analytics.TrackEvent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val interactor: StatInteractor = StatInteractor_Analytics(object : AnalyticsTracker {
            override fun trackEvent(event: TrackEvent) {
                Log.d("ANALYTICS", event.toString())
            }
        })

        interactor.simpleLogMethod("test", 1)
    }
}
