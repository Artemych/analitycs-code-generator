# App analytics code generator

The idea was to automate writing code for out analytic interactors. 

## Lib has 3 modules
1. analytics - module with classes which could be used in client app
2. analytics-annotation - all lib annotations
3. analytics-processor - processor and code generator for interactors implementation

## Libs
Main library for code generation is [Kotlin poet](https://square.github.io/kotlinpoet/)

## Sample usage

```Kotlin
@AnalyticsInteractor
interface StatInteractor {

    @AnalyticEvent(
        label = LABEL,
        category = GENERAL,
        action = CLICK
    )
    fun simpleLogMethod(@Param(TEST_PARAM) param: String, count: Long)
}

class MyClass {

    fun doSomeAction() {
        val interactor: StatInteractor = StatInteractor_Analytics(object :AnalyticsTracker {
                    override fun trackEvent(event: TrackEvent) {
                        Log.d("ANALYTICS", event.toString())
                    }
                })
        
        interactor.simpleLogMethod("test", 1)
    }

}

// D/ANALYTICS: TrackEvent(category=GENERAL, action=CLICK, label=LABEL, params={test_param=test, count=1})

```





