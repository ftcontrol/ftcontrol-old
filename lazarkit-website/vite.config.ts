import { defaultTheme } from "@sveltepress/theme-default"
import { sveltepress } from "@sveltepress/vite"
import { defineConfig } from "vite"

const config = defineConfig({
  plugins: [
    sveltepress({
      theme: defaultTheme({
        highlighter: {
          languages: [
            "svelte",
            "sh",
            "js",
            "html",
            "ts",
            "md",
            "css",
            "scss",
            "groovy",
            "java",
            "kotlin",
          ],
        },
        themeColor: {
          light: "#ffffff",
          dark: "#131314",
          primary: "#005bac",
          hover: "#1a7fd9",
          gradient: {
            start: "#1a7fd9",
            end: "#005bac",
          },
        },
        navbar: [
          {
            title: "Docs",
            to: "/docs/overview",
          },
        ],
        sidebar: {
          "/docs": [
            {
              title: "Getting started",
              collapsible: true,
              items: [
                {
                  title: "Overview",
                  to: "/docs/overview",
                },
                {
                  title: "Prerequisites",
                  to: "/docs/prerequisites",
                },
                {
                  title: "Changelog",
                  to: "/docs/changelog",
                },
              ],
            },
            {
              title: "Core Panels",
              collapsible: true,
              to: "/docs/panels",
              items: [
                {
                  title: "Overview",
                  to: "/docs/panels/overview",
                },
                {
                  title: "Customization",
                  to: "/docs/panels/customization",
                },
                {
                  title: "OpModes Control",
                  to: "/docs/panels/opmodecontrol",
                },
                {
                  title: "Telemetry",
                  to: "/docs/panels/telemetry",
                },
                {
                  title: "Field View",
                  to: "/docs/panels/fieldview",
                },
                {
                  title: "Graph View",
                  to: "/docs/panels/graph",
                },
                {
                  title: "Capture",
                  to: "/docs/panels/capture",
                },
                {
                  title: "Configurables",
                  to: "/docs/panels/configurables",
                },
              ],
            },
            {
              title: "Plugins",
              items: [
                {
                  title: "Overview",
                  to: "/docs/plugins/overview",
                },
                {
                  title: "installing a plugin",
                  to: "/docs/plugins/install",
                },
                {
                  title: "Creating a plugin",
                  to: "/docs/plugins/create",
                },
                {
                  title: "Plugin Coding Guide",
                  to: "/docs/plugins/guide",
                },
                {
                  title: "Limelight Proxy",
                  to: "/docs/plugins/core/limelight",
                },
              ],
            },
            {
              title: "External github page",
              to: "https://github.com/lazarcloud/ftcontrol",
            },
          ],
        },
        github: "https://github.com/lazarcloud/ftcontrol",
        logo: "/logo.svg",
      }),
      siteConfig: {
        title: "FTControl",
        description: "An all in one toolbox for FTC Programming.",
      },
    }),
  ],
})

export default config
