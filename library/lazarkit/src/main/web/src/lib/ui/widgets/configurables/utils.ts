import {
  Types,
  type CustomTypeJson,
  type GenericTypeJson,
} from "$lib/genericType"

export function forAll(
  fields: GenericTypeJson[],
  transformDefault: (item: GenericTypeJson) => void,
  transformCustom: (item: CustomTypeJson) => void
) {
  for (const field of fields) {
    if (
      (field.type == Types.CUSTOM ||
        field.type == Types.ARRAY ||
        field.type == Types.MAP ||
        field.type == Types.LIST) &&
      Array.isArray(field.customValues)
    ) {
      transformCustom(field)
      forAll(field.customValues, transformDefault, transformCustom)
    } else {
      transformDefault(field)
    }
  }
}

export function forAllRecursive<T>(
  fields: GenericTypeJson[],
  onDefault: (item: GenericTypeJson) => T,
  onCustom: (item: CustomTypeJson, childrenResults: T[]) => T
): T[] {
  return fields.map((field) => {
    if (
      (field.type === Types.CUSTOM ||
        field.type === Types.ARRAY ||
        field.type === Types.MAP ||
        field.type === Types.LIST) &&
      Array.isArray(field.customValues)
    ) {
      const childResults = forAllRecursive(
        field.customValues,
        onDefault,
        onCustom
      )
      return onCustom(field, childResults)
    } else {
      return onDefault(field)
    }
  })
}
