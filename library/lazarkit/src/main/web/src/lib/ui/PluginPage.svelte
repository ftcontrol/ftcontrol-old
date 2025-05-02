<script lang="ts">
  let { pluginID, pageID }: { pluginID: String; pageID: string } = $props()
  import { info } from "$lib"
  import type { Page, Plugin } from "$lib/socket.svelte"
  import Render from "$ui/Render.svelte"
  import { onMount } from "svelte"

  let plugin = $derived(info.plugins.find((it) => it.id === pluginID)) as Plugin
  let page = $derived(plugin.pages.find((it) => it.id === pageID)) as Page

  let processedHTML = $derived.by(() => {
    return page.html.replace(/<dynamic>(.*?)<\/dynamic>/gi, (_, key) => {
      const trimmedKey = key.trim()

      if (!plugin.globalVariables[key.trim()]) {
        console.warn("Empty or invalid key inside <dynamic> tag")
        return '<span class="dynamic-placeholder-error">[Missing Key]</span>'
      }

      return `<span class="dynamic" data-key="${trimmedKey}">${plugin.globalVariables[key.trim()]}</span>`
    })
  })

  onMount(() => {
    const handleActionClick = (event: Event) => {
      const target = event.currentTarget as HTMLElement
      const action = target.getAttribute("data-action")
      if (action !== null) {
        console.log("Action triggered:", action)
      }
    }

    const buttons = document.querySelectorAll("button.action")

    buttons.forEach((button) => {
      button.addEventListener("click", handleActionClick)
    })

    return () => {
      buttons.forEach((button) => {
        button.removeEventListener("click", handleActionClick)
      })
    }
  })
</script>

{#if plugin && page}
  <Render html={processedHTML} id={plugin.id} />
{:else}
  <p>Error</p>
{/if}
