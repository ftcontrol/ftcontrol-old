package lol.lazar.lazarkit.panels.configurables

object GlobalFields {
    var fieldsMap = mutableMapOf<String, GenericType>()
    var configurableClasses: List<ClassFinder.ClassEntry> = listOf()
    var customTypeClasses: List<ClassFinder.ClassEntry> = listOf()

    var jvmFields = listOf<GenericType>()
}