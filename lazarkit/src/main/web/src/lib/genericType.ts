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
}

export interface BaseGenericTypeJson {
  className: string
  fieldName: string
  type: Types
  valueString: string
  newValueString: string
  isValid: boolean
  value: any
}

export interface EnumTypeJson extends BaseGenericTypeJson {
  type: Types.ENUM
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

export interface DefaultTypeJson extends BaseGenericTypeJson {
  type: Exclude<Types, Types.ENUM | Types.CUSTOM | Types.ARRAY>
}

export type GenericTypeJson =
  | EnumTypeJson
  | CustomTypeJson
  | ArrayTypeJson
  | DefaultTypeJson
