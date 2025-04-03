<script lang="ts">
  import Section from "$ui/primitives/Section.svelte"
  import TextTypedInput from "$ui/primitives/TextTypedInput.svelte"
</script>

<h1>Welcome to Custom Dashboard</h1>

<Section title="Homepage">
  <p>Check the time: <a href="/time">Click here</a></p>
  <p>Check the dash: <a href="/control">Click here</a></p>
  <p>Hello: <a href="/hello">Click here</a></p>
  <p>Game Field: <a href="/field">Click here</a></p>

  <TextTypedInput text="da" type="TEST" />
</Section>
