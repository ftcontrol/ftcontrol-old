import { defaultTheme } from "@sveltepress/theme-default"
import { sveltepress } from "@sveltepress/vite"
import { defineConfig } from "vite"

const config = defineConfig({
  plugins: [
    sveltepress({
      theme: defaultTheme({
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
            to: "/docs/",
          },
          // {
          //   title: "With dropdown",
          //   items: [
          //     {
          //       title: "Bar page",
          //       to: "/bar/",
          //     },
          //     {
          //       title: "External Github page",
          //       to: "https://github.com/",
          //       external: true,
          //     },
          //   ],
          // },
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
              title: "Features",
              collapsible: false,
              items: [
                {
                  title: "Panels",
                  collapsible: true,
                  items: [
                    {
                      title: "OpModes Control",
                    },
                    {
                      title: "Telemetry",
                    },
                    {
                      title: "Field View",
                    },
                    {
                      title: "Capture",
                    },
                    {
                      title: "Configurables",
                    },
                  ],
                },
                {
                  title: "Flows",
                  collapsible: true,
                  items: [
                    {
                      title: "Test Chapter",
                    },
                  ],
                },
              ],
            },
            {
              title: "External github page",
              to: "https://github.com",
            },
          ],
        },
        // github: "https://github.com/Blackman99/sveltepress",
        discord: "https://discord.gg/9F7Kk4fRxB",
        logo: "/favicon.svg",
      }),
      siteConfig: {
        title: "FTControl",
        description: "An all in one toolbox for FTC Programming.",
      },
    }),
  ],
})

export default config
