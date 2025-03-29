<script lang="ts">
  let {
    startValue = $bindable(),
    currentValue = $bindable(),
  }: {
    startValue: string
    currentValue: string
  } = $props()
</script>

<div class="input">
  <div class:selected={currentValue == "false"} class="selector"></div>
  <button
    onclick={() => {
      currentValue = "true"
    }}>True</button
  >
  <button
    onclick={() => {
      currentValue = "false"
    }}>False</button
  >
</div>

<style>
  .input {
    background-color: var(--bg);
    display: flex;
    justify-content: space-between;
    width: 300px;
    padding-block: 1rem;
    position: relative;
  }
  .selector {
    background-color: var(--card);
    position: absolute;
    height: calc(100% - 8px);
    top: 0;
    left: 0;
    width: calc(50% - 4px);
    margin: 4px;
    transition: left 0.3s;
  }
  .selector.selected {
    left: calc(50% - 4px);
  }
  button {
    border: 0;
    background-color: transparent;
    margin: 0;
    text-align: center;
    width: 40%;
    z-index: 1;
    width: 100%;
  }
</style>
