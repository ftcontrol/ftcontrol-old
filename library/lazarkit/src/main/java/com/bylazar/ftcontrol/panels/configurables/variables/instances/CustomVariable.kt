package com.bylazar.ftcontrol.panels.configurables.variables.instances

import com.bylazar.ftcontrol.panels.configurables.variables.BaseTypes
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericVariable
import com.bylazar.ftcontrol.panels.json.GenericTypeJson

class CustomVariable(
    val fieldName: String,
    override val className: String,
    val values: List<GenericVariable>,
    val type: BaseTypes = BaseTypes.CUSTOM
) : GenericVariable(className) {

    override val toJsonType: GenericTypeJson
        get() {
            val valuesList: MutableList<GenericTypeJson> = mutableListOf()
            values.forEach {
                try{
                    valuesList.add(it.toJsonType)
                }catch (t: Throwable){
                    //skip
                }
            }
            return GenericTypeJson(
                id = "",
                className = className,
                fieldName = fieldName,
                type = type,
                valueString = "",
                newValueString = "",
                customValues = valuesList
            )
        }

}