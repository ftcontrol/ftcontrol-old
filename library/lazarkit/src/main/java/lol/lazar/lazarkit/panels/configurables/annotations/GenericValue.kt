package lol.lazar.lazarkit.panels.configurables.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class GenericValue(
    val tParam: KClass<*> = Any::class,
    val vParam: KClass<*> = Any::class
)
