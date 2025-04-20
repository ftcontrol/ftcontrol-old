import { info } from "$lib"
import { Types, type GenericTypeJson } from "$lib/genericType"
import { ConfigurablesStates } from "$lib/socket.svelte"
import { forAll, forAllRecursive } from "./utils"

function search(p: string, fields: GenericTypeJson[]) {
  if (!fields) return

  p = p.toLowerCase()
  const newOpenedStates: { [key: string]: boolean } = {}

  forAllRecursive(
    fields,
    (field) => {
      const matches =
        field.fieldName?.toLowerCase().includes(p) ||
        field.className?.toLowerCase().includes(p)
      field.isShown = matches
      return matches
    },
    (field, childResults) => {
      const nameMatches =
        field.fieldName?.toLowerCase().includes(p) ||
        field.className?.toLowerCase().includes(p)
      const childrenMatch = childResults.some(Boolean)

      const isMatch = nameMatches || childrenMatch

      if (nameMatches) {
        field.isOpened = true
        field.isShown = true
        forAll(
          field.customValues ?? [],
          (field) => {
            field.isShown = true
          },
          (field) => {
            field.isShown = true
            field.isOpened = true
          }
        )
      } else {
        field.isOpened = childrenMatch
        field.isShown = isMatch
      }
      return isMatch
    }
  )

  const openStuff = (field: GenericTypeJson) => {
    if (field.isShown) newOpenedStates[field.className] = true
  }

  forAll(info.jvmFields, openStuff, openStuff)

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

  savedStates.isOpenedMap = new Map()
  forAll(
    info.jvmFields,
    (field) => {},
    (field) => {
      savedStates.isOpenedMap.set(field.id, field.isOpened ?? false)
    }
  )
}

function restoreState() {
  info.openedStates = { ...savedStates.openedStates }

  forAll(
    info.jvmFields,
    (field) => {
      field.isShown = true
    },
    (field) => {
      field.isShown = true
      field.isOpened = savedStates.isOpenedMap.get(field.id) ?? false
    }
  )
}

export function handleSearch(value: string) {
  info.searchParam = value
  if (value != "") {
    if (info.configurablesState == ConfigurablesStates.NORMAL) saveState()
    info.configurablesState = ConfigurablesStates.SEARCH
    search(value, info.jvmFields)
  } else {
    if (info.configurablesState == ConfigurablesStates.SEARCH) {
      info.configurablesState = ConfigurablesStates.NORMAL
      restoreState()
      return
    }
  }
}

export function handleDiff() {
  if (info.configurablesState == ConfigurablesStates.DIFF) {
    info.configurablesState = ConfigurablesStates.NORMAL
    restoreState()
    return
  }
  if (info.configurablesState == ConfigurablesStates.NORMAL) {
    saveState()
  }
  info.configurablesState = ConfigurablesStates.DIFF
  computeDiff(info.jvmFields)
}

function computeDiff(fields: GenericTypeJson[]) {
  if (!fields) return

  const newOpenedStates: { [key: string]: boolean } = {}

  forAllRecursive(
    fields,
    (field) => {
      const matches = field.valueString != info.initialJvmFields.get(field.id)
      field.isShown = matches
      return matches
    },
    (field, childResults) => {
      const nameMatches =
        field.valueString != info.initialJvmFields.get(field.id)
      const childrenMatch = childResults.some(Boolean)

      const isMatch = nameMatches || childrenMatch

      if (nameMatches) {
        field.isOpened = true
        field.isShown = true
        forAll(
          field.customValues ?? [],
          (field) => {
            field.isShown = true
          },
          (field) => {
            field.isShown = true
            field.isOpened = true
          }
        )
      } else {
        field.isOpened = childrenMatch
        field.isShown = isMatch
      }
      return isMatch
    }
  )

  const openStuff = (field: GenericTypeJson) => {
    if (field.isShown) newOpenedStates[field.className] = true
  }

  forAll(info.jvmFields, openStuff, openStuff)

  info.openedStates = newOpenedStates
}
