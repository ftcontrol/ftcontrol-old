<script lang="ts">
  import Section from "$ui/primitives/Section.svelte"

  let isDisabled = $state(false)

  function getURL() {
    if (window.location.hostname == "localhost") return "http://192.168.43.1"
    return window.location.hostname
  }

  async function fetchImage() {
    const url = `http://${getURL()}:5800`
    try {
      const response = await fetch(url)

      return url
    } catch {
      isDisabled = true
      throw new Error("no limelight")
    }
  }
</script>

<Section title="Limelight Feed" {isDisabled}>
  {#await fetchImage()}
    <p>Loading...</p>
  {:then url}
    <img src={url} alt="limelight feed" />
  {:catch error}
    <p>Limelight not found.</p>
  {/await}
</Section>

<style>
  img {
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
