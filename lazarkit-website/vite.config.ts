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
              title: "Panels",
              collapsible: true,
              to: "/docs/panels",
              items: [
                {
                  title: "Overview",
                  to: "/docs/panels/overview",
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
                {
                  title: "Limelight Proxy",
                  to: "/docs/panels/limelight",
                },
              ],
            },
            // {
            //   title: "Flows",
            //   collapsible: true,
            //   items: [
            //     {
            //       title: "Test Chapter",
            //     },
            //   ],
            // },
            // {
            //   title: "External github page",
            //   to: "https://github.com",
            // },
          ],
        },
        // github: "https://github.com/Blackman99/sveltepress",
        // discord: "https://discord.gg/9F7Kk4fRxB",
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
