<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
  import Button from "$ui/primitives/Button.svelte"
  import SelectInput from "$ui/primitives/SelectInput.svelte"
  import { onDestroy } from "svelte"

  let playbackSpeed = $state("1x")

  $effect(() => {
    if (playbackSpeed == null) return
    if (info.isForwarding) {
      startingTimestamp = info.timestamp
      startedForwardingAt = performance.now()
    }
  })

  let animationFrame: number | null = null
  let startedForwardingAt: number | null = null
  let startingTimestamp: number = 0

  function updatePlayback(time: number) {
    if (!info.isForwarding || startedForwardingAt === null) return

    const elapsed = time - startedForwardingAt
    const speedMultiplier = parseFloat(playbackSpeed.replace("x", ""))
    info.timestamp = Math.min(
      startingTimestamp + elapsed * speedMultiplier,
      info.duration
    )

    if (info.timestamp < info.duration) {
      animationFrame = requestAnimationFrame(updatePlayback)
    } else {
      info.timestamp = info.duration
      info.isForwarding = false
      animationFrame = null
      startedForwardingAt = null
    }
  }

  function togglePlayback() {
    info.isForwarding = !info.isForwarding

    if (info.isForwarding) {
      startingTimestamp = info.timestamp
      startedForwardingAt = performance.now()
      animationFrame = requestAnimationFrame(updatePlayback)
    } else {
      startedForwardingAt = null
      if (animationFrame !== null) {
        cancelAnimationFrame(animationFrame)
        animationFrame = null
      }
    }
  }

  onDestroy(() => {
    if (animationFrame !== null) {
      cancelAnimationFrame(animationFrame)
    }
  })
</script>

<Section title={"Capture"}>
  <p>Got {info.history.length} entries</p>
  <p>
    Timestamp: {(info.timestamp / 1000).toFixed(2)}s / {(
      info.duration / 1000
    ).toFixed(2)}s
  </p>
  <input
    disabled={!info.hasRecording}
    bind:value={info.timestamp}
    type="range"
    min={0}
    max={info.duration}
  />
  <div class="flex-item">
    <p>Animation speed</p>

    <SelectInput
      startValue={"1x"}
      bind:currentValue={playbackSpeed}
      value={"1x"}
      possibleValues={["0.1x", "0.25x", "0.5x", "1x", "2x", "3x", "4x"]}
      isValid={info.hasRecording}
      alwaysValid={true}
    ></SelectInput>
  </div>
  <div>
    <div class="flex">
      <Button
        onclick={() => {
          info.isRecording = true
        }}
        disabled={info.isRecording}>Start</Button
      >
      <Button
        onclick={() => {
          info.isRecording = false
        }}
        disabled={!info.isRecording}>Stop</Button
      >
      <Button
        onclick={() => {
          info.isRecording = false
          info.history = []
          info.timestamp = 0
        }}
        disabled={!info.hasRecording}>Reset</Button
      >
    </div>
    <div class="flex">
      <Button
        onclick={() => {
          info.isPlaying = !info.isPlaying
        }}
        disabled={!info.hasRecording}
        >{info.isPlaying ? "Disable" : "Enable"} Visual Sync</Button
      >
      <Button onclick={togglePlayback} disabled={!info.hasRecording}>
        {info.isForwarding ? "Stop" : "Start"} Playing
      </Button>
    </div>
  </div>
</Section>

<style>
  .flex-item {
    display: flex;
    gap: 1rem;
    align-items: center;
  }
  .flex {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
    margin-block: 1rem;
  }
</style>
