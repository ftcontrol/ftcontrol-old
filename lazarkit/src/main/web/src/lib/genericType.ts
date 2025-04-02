export enum Types {
  INT = "INT",
  DOUBLE = "DOUBLE",
  STRING = "STRING",
  BOOLEAN = "BOOLEAN",
  FLOAT = "FLOAT",
  LONG = "LONG",
  ENUM = "ENUM",
  ARRAY = "ARRAY",
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
}

export interface FixedValuesTypeJson extends BaseGenericTypeJson {
  type: Types.ENUM | Types.BOOLEAN
  possibleValues: string[]
}

export interface CustomTypeJson extends BaseGenericTypeJson {
  type: Types.CUSTOM
  customValues: GenericTypeJson[]
}

export interface ArrayTypeJson extends BaseGenericTypeJson {
  type: Types.ARRAY
  arrayValues: GenericTypeJson[]
}

export interface MapTypeJson extends BaseGenericTypeJson {
  type: Types.MAP
  mapValues: GenericTypeJson[]
}

export interface DefaultTypeJson extends BaseGenericTypeJson {
  type: Exclude<
    Types,
    Types.ENUM | Types.CUSTOM | Types.ARRAY | Types.MAP | Types.BOOLEAN
  >
}

export type GenericTypeJson =
  | FixedValuesTypeJson
  | CustomTypeJson
  | ArrayTypeJson
  | MapTypeJson
  | DefaultTypeJson

export type ChangeJson = {
  id: string
  newValueString: string
}
