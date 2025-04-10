<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
  import Button from "$ui/primitives/Button.svelte"
</script>

<Section title={"Playback"}>
  <p>Got {info.history.length} entries</p>
  <p>Start: {info.startTimestamp}</p>
  <p>End: {info.endTimestamp}</p>
  <p>Duration: {info.duration}</p>
  <p>Value: {info.timestamp}</p>
  <p>{JSON.stringify(info.entry)}</p>
  <div>
    <Button
      onclick={() => {
        info.isRecording = true
      }}
      disabled={info.isRecording}>Start Recording</Button
    >
    <Button
      onclick={() => {
        info.isRecording = false
      }}
      disabled={!info.isRecording}>Stop Recording</Button
    >
    <Button
      onclick={() => {
        info.isRecording = false
      }}
      disabled={!info.hasRecording}>Reset Recording</Button
    >
    <Button
      onclick={() => {
        info.isPlaying = !info.isPlaying
      }}
      disabled={!info.hasRecording}
      >{info.isPlaying ? "Disable" : "Enable"} Playback</Button
    >
  </div>
  <input bind:value={info.timestamp} type="range" min={0} max={info.duration} />
</Section>

<style>
</style>
