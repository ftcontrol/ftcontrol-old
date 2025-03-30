export function intValidator(value: string): boolean {
  for (const char of value) {
    if (!["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"].includes(char)) {
      return false
    }
  }
  return true
}
