export function intValidator(value: string): boolean {
  for (const char of value) {
    if (!"0123456789".includes(char)) {
      return false
    }
  }
  return true
}

export function longValidator(value: string): boolean {
  return intValidator(value)
}

export function doubleValidator(value: string): boolean {
  return !isNaN(parseFloat(value))
}

export function floatValidator(value: string): boolean {
  return doubleValidator(value)
}

export function stringValidator(value: string): boolean {
  return true
}
