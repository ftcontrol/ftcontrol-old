<script lang="ts">
  import type { LayoutProps } from "./$types"

  let { children }: LayoutProps = $props()
</script>

<section>
  {@render children?.()}
</section>

<style>
  section {
    padding: 1rem;
  }
</style>
