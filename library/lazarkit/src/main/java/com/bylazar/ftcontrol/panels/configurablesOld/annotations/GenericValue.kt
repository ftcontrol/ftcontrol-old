package com.bylazar.ftcontrol.panels.configurablesOld.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class GenericValue(
    val tParam: KClass<*> = Any::class,
    val vParam: KClass<*> = Any::class
)
