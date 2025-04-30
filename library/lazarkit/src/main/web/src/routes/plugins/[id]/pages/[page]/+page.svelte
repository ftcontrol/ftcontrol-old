<script lang="ts">
  import { info } from "$lib"
  import type { Page, Plugin } from "$lib/socket.svelte"
  import Render from "$ui/Render.svelte"
  import { onMount } from "svelte"
  import type { PageProps } from "./$types"

  let { data }: PageProps = $props()

  let plugin = $derived(info.plugins.find((it) => it.id === data.id)) as Plugin
  let page = $derived(plugin.pages.find((it) => it.id === data.page)) as Page

  let processedHTML = $derived.by(() => {
    return page.html.replace(/<dynamic>(.*?)<\/dynamic>/gi, (_, key) => {
      return plugin.globalVariables[key.trim()] ?? "VARIABLE DOESN'T EXIST"
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

<h1>{page.title}</h1>
<h2>{data.id}</h2>
<h2>{data.page}</h2>
{#key processedHTML}
  <Render html={processedHTML} id={plugin.id} />
{/key}
