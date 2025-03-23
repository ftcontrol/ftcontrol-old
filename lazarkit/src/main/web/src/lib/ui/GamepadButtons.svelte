<script lang="ts">
  import { gamepads } from "$lib"
  let { gamepad }: { gamepad: Gamepad } = $props()
</script>

<h4>Buttons</h4>

<div class="buttons">
  {#each gamepad.buttons as btn, i}
    {#if i === 6 || i === 7}
      <div class="trigger">
        <p>{gamepads.getPS4ButtonLabel(i)} ({(btn.value * 100).toFixed(0)}%)</p>
        <progress max="1" value={btn.value}></progress>
      </div>
    {:else}
      <button class:pressed={btn.pressed}
        >{gamepads.getPS4ButtonLabel(i)}</button
      >
    {/if}
  {/each}
</div>

<h4>Axes</h4>
<div class="axes">
  {#each gamepad.axes as axis, i}
    <div class="axis">
      <label>Axis {i} ({axis.toFixed(2)})</label>
      <progress max="2" value={axis + 1}></progress>
    </div>
  {/each}
</div>

<style>
  .buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
  }

  button {
    width: 50px;
    height: 30px;
    border: 1px solid #ccc;
    background: #f4f4f4;
    cursor: pointer;
    font-size: 14px;
  }

  button.pressed {
    background: #4caf50;
    color: white;
  }

  .trigger {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 10px;
  }

  progress {
    width: 100px;
  }

  .axes {
    display: flex;
    flex-direction: column;
    gap: 5px;
  }

  .axis {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
</style>
