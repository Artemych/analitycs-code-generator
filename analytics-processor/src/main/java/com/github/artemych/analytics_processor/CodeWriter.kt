package com.github.artemych.analytics_processor

import java.io.IOException
import java.io.UncheckedIOException
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class CodeWriter internal constructor(private val processingEnv: ProcessingEnvironment) {

    internal fun write(descriptor: AnalyticsInteractorDescriptor, element: TypeElement) {
        try {
            writeInternal(descriptor, element)
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }

    }

    @Throws(IOException::class)
    private fun writeInternal(descriptor: AnalyticsInteractorDescriptor, element: TypeElement) {
        val generator = CodeGenerator()
        generator.generate(descriptor).writeTo(processingEnv.filer)
        print(element)
    }
}
