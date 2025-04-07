<script lang="ts">
  import { info } from "$lib"
  import Section from "$ui/primitives/Section.svelte"
</script>

<Section title="Flows">
  <p>{JSON.stringify(info.flows)}</p>
</Section>
