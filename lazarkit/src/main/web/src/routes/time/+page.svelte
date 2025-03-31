<script lang="ts">
  import { info } from "$lib"
  import Section from "$ui/primitives/Section.svelte"
</script>

<Section title="Current Time">
  <h1>Current Time:</h1>
  <p>{info.time}</p>
</Section>
