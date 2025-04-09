package lol.lazar.lazarkit.panels.configurables.variables

import java.lang.reflect.Field

class ArraysVariable(
     var className: String,
     var reference: Field,
){
    var customValues: List<NestedSimpleVariable>?
}