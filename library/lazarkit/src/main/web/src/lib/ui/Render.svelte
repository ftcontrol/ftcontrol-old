<script lang="ts">
  import { info, socket } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import type { PluginAction } from "$lib/socket.svelte"
  import { onMount } from "svelte"
  import { v4 as uuidv4 } from "uuid"

  let { html, id = "" }: { html: string; id?: string } = $props()

  let hostElement: HTMLDivElement
  let shadowRoot: ShadowRoot | null = $state(null)

  $effect(() => {
    if (shadowRoot == undefined) return
    const elements = shadowRoot.querySelectorAll(".dynamic")

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
    const scripts: string[] = []

    const newContent = content.replace(
      /<script>([\s\S]*?)<\/script>/gi,
      (_, script) => {
        scripts.push(script)
        return ""
      }
    )

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
      .wrapper{
        width: 100%;
        height: 100%;
      }
      iframe{
        outline: none;
        border: none;
        background-color:white;
      }

      .widget-header {
        display: flex;
        gap: 1rem;
        justify-content: space-between;
        align-items: center;
        position: relative;
        margin-inline: 1rem;
        padding-top: 0.5rem;
        margin-bottom: 0.5rem;
      }

      .widget-header > p {
        margin: 0;
        text-align: center;
        flex-grow: 1;
        font-size: 1.25rem;
        font-weight: bold;
      }
    `

    const wrapper = document.createElement("div")
    wrapper.className = "wrapper"
    wrapper.id = "wrapper"
    wrapper.classList.add(settings.isDark ? "dark-mode" : "")
    wrapper.classList.add(settings.animationSpeed)
    wrapper.classList.add(settings.primaryColor)
    wrapper.innerHTML = newContent

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

    const id = "shadow" + uuidv4().toString()

    window[id] = shadow

    scripts.forEach((it) => {
      const script = document.createElement("script")
      script.textContent = `
          const document = window["${id}"];
          ${it}
        `
      script.type = "module"
      shadow.appendChild(script)
    })

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
  div {
    width: 100%;
    height: 100%;
  }
</style>
