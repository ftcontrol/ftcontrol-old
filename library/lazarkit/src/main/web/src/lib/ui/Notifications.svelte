<script lang="ts">
  import { notifications } from "$lib"
  import type { Notification } from "$lib/notifications.svelte"
  import Button from "./primitives/Button.svelte"
  let date = $state(Date.now())

  $effect(() => {
    const interval = setInterval(() => {
      date = Date.now()
    }, 250)

    return () => {
      clearInterval(interval)
    }
  })

  function getSimple(notifications: Notification[]) {
    return notifications.filter((it) => it.actions.length <= 0)
  }
  function getActions(notifications: Notification[]) {
    return notifications.filter((it) => it.actions.length > 0)
  }
</script>

<section>
  {#each getSimple(notifications.data).filter((it) => date - it.timestamp <= 2500) as notif}
    <div class="notif">
      <p>{notif.text}</p>
    </div>
  {/each}
</section>
<section>
  {#each getActions(notifications.data) as notif}
    <div class="notif">
      <p class="center">{notif.text}</p>
      <div class="flex">
        {#each notif.actions as action}
          <Button
            onclick={() => {
              action.task()
            }}
          >
            {action.text}
          </Button>
        {/each}
      </div>
    </div>
  {/each}
</section>

<style>
  section {
    position: absolute;
    right: 1rem;
    top: 1rem;
    text-align: right;
    z-index: 100;
  }
  .notif {
    border: 1px solid var(--text);
    background-color: var(--card);
    padding: 1rem;
    margin-bottom: 0.25rem;
  }
  p.center {
    text-align: center;
  }
  .flex {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
  }
</style>
