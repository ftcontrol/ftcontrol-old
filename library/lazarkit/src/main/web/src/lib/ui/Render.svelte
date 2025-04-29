<script lang="ts">
  import { settings } from "$lib/settings.svelte"
  import { onMount } from "svelte"
  export let html = ""

  let hostElement: HTMLDivElement

  function injectShadowContent(content: string) {
    const shadow =
      hostElement.shadowRoot || hostElement.attachShadow({ mode: "open" })

    shadow.innerHTML = ""

    const style = document.createElement("style")
    style.textContent = `
        .wrapper {
        --bg: #f6f6f6;
        --card: #ffffff;
        --cardTransparent: #ffffff50;
        --text: #1b1b1b;

        --primary: #e60012;
        --primary: #005bac;
        }

        .wrapper.dark-mode {
        --bg: #1b1b1b;
        --card: #131314;
        --cardTransparent: #13131450;
        --text: #c4c7c5;
        }
        .wrapper.instant {
        --multiplier: 0;
        }
        .wrapper.fast {
        --multiplier: 0.1;
        }
        .wrapper.normal {
        --multiplier: 0.15;
        }
        .wrapper.slow {
        --multiplier: 0.225;
        }
        .wrapper {
        --d: calc(var(--multiplier) * 1s);
        --d1: calc(var(--multiplier) * 1s);
        --d2: calc(var(--multiplier) * 2s);
        --d3: calc(var(--multiplier) * 3s);
        }

        .wrapper.blue {
        --primary: #005bac;
        }
        .wrapper.red {
        --primary: #e60012;
        }
      `
    const wrapper = document.createElement("div")
    wrapper.className = "wrapper"
    wrapper.classList.add(settings.isDark ? "dark-mode" : "")
    wrapper.classList.add(settings.animationSpeed)
    wrapper.classList.add(settings.primaryColor)
    wrapper.innerHTML = content

    shadow.appendChild(style)
    shadow.appendChild(wrapper)
  }

  onMount(() => {
    injectShadowContent(html)
  })
</script>

<div bind:this={hostElement} class="shadow-host"></div>

<style>
</style>
