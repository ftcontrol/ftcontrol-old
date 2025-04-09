package lol.lazar.lazarkit.panels.oldConfs.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class GenericValue(
    val tParam: KClass<*>,
    val vParam: KClass<*> = Any::class
)
