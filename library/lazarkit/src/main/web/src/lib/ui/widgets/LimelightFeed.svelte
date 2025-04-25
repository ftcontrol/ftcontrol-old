<script lang="ts">
  import Section from "$ui/primitives/Section.svelte"

  function getURL() {
    if (window.location.hostname == "localhost") return "http://192.168.43.1"
    return window.location.hostname
  }

  async function fetchImage() {
    const url = `http://${getURL()}:5800`
    const response = await fetch(url)
    if (response.ok) {
      return url
    } else {
      console.error("Failed to fetch image")
    }
  }
</script>

<Section title="Limelight Feed">
  {#await fetchImage()}
    <p>Loading...</p>
  {:then url}
    <img src={url} />
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
