<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
</script>

<Section title={"Configurables"}>
  {#each info.jvmFields as line}
    <div class="item" style={line.type == "UNKNOWN" ? "opacity: 0.3;" : ""}>
      <p>{line.className.split("." + line.className.split(".").at(-1))[0]}</p>

      <p>{line.className.split(".").at(-1)} {line.fieldName} {line.type}</p>
      <p>
        {line.valueString}
      </p>
      {#if line.type == "ENUM"}
        <p>{JSON.stringify(line.possibleValues)}</p>
      {/if}
      {#if line.type == "CUSTOM"}
        <p>{JSON.stringify(line.customValues)}</p>
      {/if}
      {#if line.type == "ARRAY"}
        <p>{JSON.stringify(line.arrayValues)}</p>
      {/if}
      {JSON.stringify(line)}
    </div>
  {/each}
  {#if info.jvmFields.length == 0}
    <p>No configurables found.</p>
  {/if}
</Section>

<style>
  .item {
    border-bottom: 1px solid black;
  }
</style>
