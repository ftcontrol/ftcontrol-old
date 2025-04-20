import { info } from "$lib"
import {
  Types,
  type CustomTypeJson,
  type GenericTypeJson,
} from "$lib/genericType"
import { ConfigurablesStates } from "$lib/socket.svelte"
import { forAll, forAllRecursive } from "./utils"

function genericSearch(
  fields: GenericTypeJson[],
  condition: (field: GenericTypeJson) => boolean
) {
  if (!fields) return

  const newOpenedStates: { [key: string]: boolean } = {}

  forAllRecursive(
    fields,
    (field) => {
      const matches = condition(field)
      field.isShown = matches
      return matches
    },
    (field, childResults) => {
      const nameMatches = condition(field)
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

function search(p: string, fields: GenericTypeJson[]) {
  p = p.toLowerCase()

  genericSearch(fields, (field) => {
    return (
      field.fieldName?.toLowerCase().includes(p) ||
      field.className?.toLowerCase().includes(p)
    )
  })
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
  genericSearch(fields, (field) => {
    return field.valueString != info.initialJvmFields.get(field.id)
  })
}
