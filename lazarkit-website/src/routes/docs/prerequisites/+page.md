<script>
  async function getData() {
    try {
      const response = await fetch("https://mymaven.bylazar.com/api/maven/latest/version/releases/com/bylazar/ftcontrol");
      if (!response.ok) throw new Error("Failed to fetch");
      const text = await response.json();
      return text;
    } catch (err) {
      throw err;
    }
  }
</script>
# project setup

# clone ftc quickstart

# install kotlin
# install FTControl


{#await getData()}
  <h3 class="text-orange">0.0.0</h3>
{:then res}
  <h3 class="text-green">{res.version}</h3>
{:catch err}
  <h3 class="text-red">{err.message}</h3>
{/await}