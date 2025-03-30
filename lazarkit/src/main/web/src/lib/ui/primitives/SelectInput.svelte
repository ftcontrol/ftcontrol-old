<script lang="ts">
  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
    value = $bindable(),
    possibleValues,
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
    value: string
    possibleValues: string[]
  } = $props()
  $effect(() => {
    if (value != null) return
    value = startValue
    isValid = true
  })
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
    {#each possibleValues.filter((it) => it != currentValue) as v}
      <button
        onclick={() => {
          currentValue = v
          value = v
          isShown = false
        }}>{v}</button
      >
    {/each}
  </div>
{/if}
