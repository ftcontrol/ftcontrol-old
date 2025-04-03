import { Types } from "$lib/genericType"

const JAVA_INT_MIN = -2147483648 // -2^31
const JAVA_INT_MAX = 2147483647 // 2^31 - 1
const JAVA_LONG_MIN = BigInt("-9223372036854775808") // -2^63
const JAVA_LONG_MAX = BigInt("9223372036854775807") // 2^63 - 1
const JAVA_FLOAT_MIN = -3.4e38
const JAVA_FLOAT_MAX = 3.4e38
const JAVA_DOUBLE_MIN = -1.7e308
const JAVA_DOUBLE_MAX = 1.7e308

export function intValidator(value: string): boolean {
  const num = parseInt(value, 10)
  if (isNaN(num)) return false
  return num >= JAVA_INT_MIN && num <= JAVA_INT_MAX
}

export function longValidator(value: string): boolean {
  const num = BigInt(value)
  return num >= JAVA_LONG_MIN && num <= JAVA_LONG_MAX
}

export function doubleValidator(value: string): boolean {
  const num = parseFloat(value)
  if (isNaN(num)) return false
  return num >= JAVA_DOUBLE_MIN && num <= JAVA_DOUBLE_MAX
}

export function floatValidator(value: string): boolean {
  const num = parseFloat(value)
  if (isNaN(num)) return false
  return num >= JAVA_FLOAT_MIN && num <= JAVA_FLOAT_MAX
}

export function stringValidator(value: string): boolean {
  return typeof value === "string"
}

export function anyValidator(type: Types) {
  switch (type) {
    case Types.INT:
      return intValidator
    case Types.LONG:
      return longValidator
    case Types.DOUBLE:
      return doubleValidator
    case Types.FLOAT:
      return floatValidator
    case Types.STRING:
      return stringValidator
    default:
      return stringValidator
  }
}
