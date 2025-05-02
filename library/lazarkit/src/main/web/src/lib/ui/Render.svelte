<script lang="ts">
  import { info, socket } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import type { PluginAction } from "$lib/socket.svelte"
  import { onMount } from "svelte"
  import { v4 as uuidv4 } from "uuid"
  import { baseStyles } from "./styles"

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
    style.textContent = baseStyles

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
