package com.bylazar.bytebox.panels.configurables.variables

fun convertValue(value: String, type: BaseTypes, possibleValues: List<String>?): Any? {
    return when (type) {
        BaseTypes.INT -> {
            when {
                value.toIntOrNull() != null -> value.toInt()
                value.toFloatOrNull() != null -> value.toFloat()
                    .toInt()

                value.toDoubleOrNull() != null -> value.toDouble()
                    .toInt()

                else -> value.toInt()
            }
        }

        BaseTypes.DOUBLE -> {
            when {
                value.toDoubleOrNull() != null -> value.toDouble()
                value.toFloatOrNull() != null -> value.toFloat()
                    .toDouble()

                else -> value.toDouble()
            }
        }

        BaseTypes.STRING -> {
            value
        }

        BaseTypes.BOOLEAN -> {
            value.toBoolean()
        }

        BaseTypes.FLOAT -> {
            when {
                value.toFloatOrNull() != null -> value.toFloat()
                value.toDoubleOrNull() != null -> value.toDouble()
                    .toFloat()

                else -> value.toFloat()
            }
        }

        BaseTypes.LONG -> {
            when {
                value.toLongOrNull() != null -> value.toLong()
                value.toDoubleOrNull() != null -> value.toDouble()
                    .toLong()

                else -> value.toLong()
            }
        }

        BaseTypes.ENUM -> {
            if (possibleValues == null) return null
            for (enumValue in possibleValues) {
                if (enumValue == value) return enumValue
            }
            return null
        }

        else -> null
    }
}