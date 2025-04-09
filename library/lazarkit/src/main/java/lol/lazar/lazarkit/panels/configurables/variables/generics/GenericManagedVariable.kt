package lol.lazar.lazarkit.panels.configurables.variables.generics

import lol.lazar.lazarkit.panels.configurables.variables.MyField

abstract class GenericManagedVariable(
    override val reference: MyField?,
    override val className: String,
) : GenericVariable(reference, className) {
    abstract val manager: GenericManager
}