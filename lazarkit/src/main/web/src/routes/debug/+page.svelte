<script lang="ts">
  import { socket, info } from "$lib"

  let type = ""
  let data: string[] = []

  function addDataField() {
    data = [...data, ""]
  }

  function removeDataField(index: number) {
    data = data.filter((_, i) => i !== index)
  }
</script>

<h3>Send Message</h3>

<label>
  Type:
  <input bind:value={type} placeholder="Enter message type" />
</label>

<div>
  <h3>Data</h3>
  {#each data as value, index}
    <div>
      <input type="text" bind:value={data[index]} placeholder="Enter data" />
      <button on:click={() => removeDataField(index)}>Remove</button>
    </div>
  {/each}
  <button on:click={addDataField}>Add Data Field</button>
</div>

<button
  on:click={() => {
    socket.sendMessage(type, data)
  }}>Send Message</button
>
