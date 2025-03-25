package lol.lazar.lazarkit.panels.configurables

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Configurable(
    val value: String = ""
)
