package com.github.artemych.analytics_processor

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

internal class Messenger(private val processingEnv: ProcessingEnvironment) {

    fun error(element: Element, message: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message, element)
    }
}
