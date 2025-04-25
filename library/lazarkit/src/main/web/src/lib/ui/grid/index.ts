import { Grid } from "./grid.svelte"

export let gridManagers: { [key: string]: Grid } = {
  Default: new Grid(null),
  "Lazar's Preset": new Grid(null),
}

export let allKeys: string[] = Object.keys(gridManagers)
