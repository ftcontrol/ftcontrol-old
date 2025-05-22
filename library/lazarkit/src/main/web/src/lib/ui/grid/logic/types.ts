import { v4 as uuidv4 } from "uuid"

export type Preset = {
  id: string
  name: string
  xSize: number
  ySize: number
  groups: WidgetGroup[]
}

export type WidgetGroup = {
  id: string
  activeWidgetID: number
  widgets: Widget[]
  start: {
    x: number
    y: number
  }
  sizes: {
    x: number
    y: number
  }
}

type CustomWidget = {
  type: WidgetTypes.CUSTOM
  pluginID: string
  pageID: string
}

type OtherWidget = {
  type: Exclude<WidgetTypes, WidgetTypes.CUSTOM>
}

export type Widget = CustomWidget | OtherWidget

export const allWidgetTypes = [
  "Custom",
  "OpMode Control",
  "Gamepad",
  "Field",
  "Telemetry",
  "Configurables",
  "Graph",
  "Capture",
  "Test",
]

export enum WidgetTypes {
  CONTROLS = "OpMode Control",
  GAMEPAD = "Gamepad",
  FIELD = "Field",
  TELEMETRY = "Telemetry",
  CONFIGURABLES = "Configurables",
  GRAPH = "Graph",
  CAPTURE = "Capture",
  CUSTOM = "Custom",
  TEST = "Test",
}

export function defaultModuled(): Preset {
  return {
    id: uuidv4(),
    name: "Default",
    xSize: 10,
    ySize: 10,
    groups: [
      {
        id: uuidv4(),
        activeWidgetID: 0,
        widgets: [
          {
            type: WidgetTypes.CONTROLS,
          },
        ],
        start: {
          x: 1,
          y: 1,
        },
        sizes: {
          x: 3,
          y: 2,
        },
      },
      {
        id: uuidv4(),
        activeWidgetID: 0,
        widgets: [
          {
            type: WidgetTypes.CAPTURE,
          },
        ],
        start: {
          x: 4,
          y: 1,
        },
        sizes: {
          x: 3,
          y: 4,
        },
      },
      {
        id: uuidv4(),
        activeWidgetID: 0,
        widgets: [
          {
            type: WidgetTypes.GAMEPAD,
          },
        ],
        start: {
          x: 1,
          y: 3,
        },
        sizes: {
          x: 3,
          y: 2,
        },
      },
      {
        id: uuidv4(),
        activeWidgetID: 0,
        widgets: [
          {
            type: WidgetTypes.FIELD,
          },
        ],
        start: {
          x: 1,
          y: 5,
        },
        sizes: {
          x: 3,
          y: 6,
        },
      },
      {
        id: uuidv4(),
        activeWidgetID: 0,
        widgets: [
          {
            type: WidgetTypes.TELEMETRY,
          },
          {
            type: WidgetTypes.GRAPH,
          },
        ],
        start: {
          x: 4,
          y: 5,
        },
        sizes: {
          x: 3,
          y: 6,
        },
      },
      {
        id: uuidv4(),
        activeWidgetID: 0,
        widgets: [
          {
            type: WidgetTypes.CONFIGURABLES,
          },
        ],
        start: {
          x: 7,
          y: 1,
        },
        sizes: {
          x: 4,
          y: 10,
        },
      },
    ],
  }
}
