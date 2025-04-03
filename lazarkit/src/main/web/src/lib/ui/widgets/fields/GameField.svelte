<script lang="ts">
  import { canvas } from "$lib/canvas"
  import { Section } from "$primitives"
</script>

<Section title={"Field"}>
  <img src="/fields/field.png" alt="field" />
  <canvas></canvas>
  {JSON.stringify(canvas)}
</Section>

<style>
  img {
    width: 100%;
  }
</style>
