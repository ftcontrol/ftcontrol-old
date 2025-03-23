<script>
  import { notifications } from "$lib"
  let date = $state(Date.now())

  $effect(() => {
    const interval = setInterval(() => {
      date = Date.now()
    }, 250)

    return () => {
      clearInterval(interval)
    }
  })
</script>

<section>
  {#each notifications.data.filter((it) => date - it.timestamp <= 2500) as notif}
    <p>{notif.text}</p>
  {/each}
</section>

<style>
  section {
    position: absolute;
    right: 1rem;
    top: 1rem;
    text-align: right;
  }
  p {
    border: 1px solid black;
    padding: 1rem;
  }
</style>
