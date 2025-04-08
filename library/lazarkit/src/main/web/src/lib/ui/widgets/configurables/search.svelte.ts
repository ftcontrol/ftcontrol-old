import { info } from "$lib"
import { Types, type GenericTypeJson } from "$lib/genericType"

function search(p: string, fields: GenericTypeJson[]) {
  if (!fields) return

  p = p.toLowerCase()
  const newOpenedStates: { [key: string]: boolean } = {}

  function recursiveSearch(fields: GenericTypeJson[]): boolean {
    let foundInGroup = false

    for (const field of fields) {
      const matchesField =
        field.fieldName?.toLowerCase().includes(p) ||
        field.className?.toLowerCase().includes(p)

      let foundInNested = false
      if (
        (field.type === Types.CUSTOM ||
          field.type === Types.ARRAY ||
          field.type === Types.MAP ||
          field.type === Types.LIST) &&
        Array.isArray(field.customValues)
      ) {
        field.isOpened = false

        if (matchesField) {
          field.isOpened = true
        } else {
          foundInNested = recursiveSearch(field.customValues)
          if (foundInNested) field.isOpened = true
        }
      }

      field.isShown = matchesField || foundInNested

      if (field.isShown) {
        foundInGroup = true
        newOpenedStates[field.className] = true
      } else if (!(field.className in newOpenedStates)) {
        newOpenedStates[field.className] = false
      }
    }

    return foundInGroup
  }

  recursiveSearch(fields)
  info.openedStates = newOpenedStates
}

let savedStates: {
  openedStates: { [key: string]: boolean }
  isOpenedMap: Map<string, boolean>
} = {
  openedStates: {},
  isOpenedMap: new Map(),
}

function saveState() {
  savedStates.openedStates = { ...info.openedStates }
  console.log("Saved opened states", savedStates.openedStates)
  savedStates.isOpenedMap = new Map()
  function saveStates(fields: GenericTypeJson[]) {
    for (const field of fields) {
      if (
        (field.type === Types.CUSTOM ||
          field.type === Types.ARRAY ||
          field.type === Types.MAP ||
          field.type === Types.LIST) &&
        Array.isArray(field.customValues)
      ) {
        savedStates.isOpenedMap.set(field.id, field.isOpened ?? false)
        saveStates(field.customValues)
      }
    }
  }
  saveStates(info.jvmFields)
}

function restoreState() {
  info.openedStates = { ...savedStates.openedStates }

  function restoreStates(fields: GenericTypeJson[]) {
    for (const field of fields) {
      field.isShown = true
      if (
        (field.type === Types.CUSTOM ||
          field.type === Types.ARRAY ||
          field.type === Types.MAP ||
          field.type === Types.LIST) &&
        Array.isArray(field.customValues)
      ) {
        field.isOpened = savedStates.isOpenedMap.get(field.id) ?? false
        restoreStates(field.customValues)
      }
    }
  }
  restoreStates(info.jvmFields)
}

let wasSaved = false

export function handleSearch(value: string) {
  if (value != "") {
    if (!wasSaved) saveState()
    wasSaved = true
    search(value, info.jvmFields)
  } else {
    restoreState()
    wasSaved = false
  }
}
