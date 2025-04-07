import { defaultTheme } from "@sveltepress/theme-default"
import { sveltepress } from "@sveltepress/vite"
import { defineConfig } from "vite"

const config = defineConfig({
  plugins: [
    sveltepress({
      theme: defaultTheme({
        navbar: [
          {
            title: "Docs",
            to: "/docs/",
          },
          {
            title: "With dropdown",
            items: [
              {
                title: "Bar page",
                to: "/bar/",
              },
              {
                title: "External Github page",
                to: "https://github.com/",
                external: true,
              },
            ],
          },
        ],
        sidebar: {
          "/foo/": [
            {
              title: "Bar",
              to: "/foo/bar/",
            },
            {
              title: "Zoo",
              collapsible: false,
              items: [
                {
                  title: "Sub item",
                  collapsible: true,
                  items: [
                    {
                      title: "Sub item",
                      to: "/sub/item/link2",
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
        github: "https://github.com/Blackman99/sveltepress",
        logo: "/sveltepress.svg",
      }),
      siteConfig: {
        title: "Sveltepress",
        description: "A content centered site build tool",
      },
    }),
  ],
})

export default config
