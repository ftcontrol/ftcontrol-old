<script lang="ts">
  let {
    currentValue = $bindable(),
    possibleValues,
    onchange = () => {},
  }: {
    currentValue: string
    possibleValues: string[]
    onchange?: (newValue: string) => void
  } = $props()
  let isShown = $state(false)
</script>

<div
  onclick={() => {
    isShown = !isShown
  }}
>
  {currentValue}
</div>
{#if isShown}
  <div>
    {#each possibleValues.filter((it) => it != currentValue) as value}
      <button
        onclick={() => {
          currentValue = value
          isShown = false
          onchange(currentValue)
        }}>{value}</button
      >
    {/each}
  </div>
{/if}
