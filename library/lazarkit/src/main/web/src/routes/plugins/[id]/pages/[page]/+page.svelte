<script lang="ts">
  import { info } from "$lib"
  import type { Page, Plugin } from "$lib/socket.svelte"
  import Render from "$ui/Render.svelte"
  import type { PageProps } from "./$types"

  let { data }: PageProps = $props()

  let plugin = $derived(info.plugins.find((it) => it.id === data.id)) as Plugin
  let page = $derived(plugin.pages.find((it) => it.id === data.page)) as Page
</script>

<h1>{page.title}</h1>
<h2>{data.id}</h2>
<h2>{data.page}</h2>

<Render html={page.html} />
