<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
  import Button from "$ui/primitives/Button.svelte"
  import { onDestroy } from "svelte"

  let animationFrame: number | null = null
  let startedForwardingAt: number | null = null
  let startingTimestamp: number = 0

  function updatePlayback(time: number) {
    if (!info.isForwarding || startedForwardingAt === null) return

    const elapsed = time - startedForwardingAt
    info.timestamp = Math.min(startingTimestamp + elapsed, info.duration)

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
  .flex {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
    margin-block: 1rem;
  }
</style>
