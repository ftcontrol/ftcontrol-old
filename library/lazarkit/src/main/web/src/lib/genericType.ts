export enum Types {
  INT = "INT",
  DOUBLE = "DOUBLE",
  STRING = "STRING",
  BOOLEAN = "BOOLEAN",
  FLOAT = "FLOAT",
  LONG = "LONG",
  ENUM = "ENUM",
  ARRAY = "ARRAY",
  LIST = "LIST",
  UNKNOWN = "UNKNOWN",
  CUSTOM = "CUSTOM",
  MAP = "MAP",
  GENERIC = "GENERIC",
  GENERIC_NO_ANNOTATION = "GENERIC_NO_ANNOTATION",
}

export interface BaseGenericTypeJson {
  className: string
  fieldName: string
  type: Types
  valueString: string
  id: string
  newValueString: string
  isValid: boolean
  value: any
  isShown: boolean
}

export interface FixedValuesTypeJson extends BaseGenericTypeJson {
  type: Types.ENUM | Types.BOOLEAN
  possibleValues: string[]
}

export interface CustomTypeJson extends BaseGenericTypeJson {
  type: Types.CUSTOM | Types.ARRAY | Types.MAP | Types.LIST
  customValues: GenericTypeJson[]
  isOpened: boolean
}

export interface DefaultTypeJson extends BaseGenericTypeJson {
  type: Exclude<
    Types,
    | Types.ENUM
    | Types.BOOLEAN
    | Types.CUSTOM
    | Types.ARRAY
    | Types.MAP
    | Types.LIST
  >
}

export type GenericTypeJson =
  | FixedValuesTypeJson
  | CustomTypeJson
  | DefaultTypeJson

export type ChangeJson = {
  id: string
  newValueString: string
}
