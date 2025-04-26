<script lang="ts">
  import Content from "$ui/primitives/Content.svelte"
  import Section from "$ui/primitives/Section.svelte"

  let isDisabled = $state(false)

  function getURL() {
    if (window.location.hostname == "localhost") return "http://192.168.43.1"
    return window.location.hostname
  }

  async function fetchDash() {
    const url = `http://${getURL()}:5801`
    try {
      const response = await fetch(url)

      return url
    } catch {
      isDisabled = true
      throw new Error("no limelight")
    }
  }
</script>

<Section title="Limelight Dashboard" {isDisabled}>
  <Content>
    {#await fetchDash()}
      <p>Loading...</p>
    {:then url}
      <iframe src={url} title="Limelight Dashboard"> </iframe>
    {:catch error}
      <p>Limelight not found.</p>
    {/await}
  </Content>
</Section>

<style>
  iframe {
    position: absolute;
    top: 3.25rem;
    right: 0;
    bottom: 0;
    left: 0;
    width: 100%;
    height: calc(100% - 3.25rem);
    border: none;
  }
</style>
