<script lang="ts">
  import { info, socket } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import type { PluginAction } from "$lib/socket.svelte"
  import { onMount } from "svelte"

  let { html, id = "" }: { html: string; id?: string } = $props()

  let hostElement: HTMLDivElement
  let shadowRoot: ShadowRoot | null = $state(null)

  $effect(() => {
    if (shadowRoot == undefined) return
    const elements = shadowRoot.querySelectorAll(".dynamic")
    console.log(elements)

    info.plugins.forEach((it) => {
      const global = it.globalVariables
      elements.forEach((el) => {
        const key = el.getAttribute("data-key")?.trim()
        if (key && global[key]) {
          el.textContent = global[key]
        }
      })
    })
  })

  function injectShadowContent(content: string) {
    const shadow =
      hostElement.shadowRoot || hostElement.attachShadow({ mode: "open" })
    shadowRoot = shadow
    shadow.innerHTML = ""

    const style = document.createElement("style")
    style.textContent = `
      .wrapper {
        --bg: #f6f6f6;
        --card: #ffffff;
        --cardTransparent: #ffffff50;
        --text: #1b1b131414;

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

    const handleClick = (event: MouseEvent) => {
      const target = event.target as HTMLElement

      if (target.matches("button.action")) {
        const action = target.getAttribute("data-action")
        if (action !== null) {
          console.log("Action triggered:", action)
          var message: PluginAction = {
            kind: "pluginsAction",
            id: id,
            action: action,
          }
          socket.sendMessage(message)
        }
      }
    }

    shadow.addEventListener("click", handleClick as EventListener)

    shadow.appendChild(style)
    shadow.appendChild(wrapper)

    return () => {
      shadow.removeEventListener("click", handleClick as EventListener)
    }
  }

  onMount(() => {
    injectShadowContent(html)
  })
</script>

<div bind:this={hostElement} class="shadow-host"></div>

<style>
</style>
