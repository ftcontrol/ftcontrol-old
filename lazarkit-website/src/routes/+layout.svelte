<script>
  import { Link } from "@sveltepress/theme-default/components"
  const { children } = $props()
</script>

<!-- Leave this. Or you can add more content for your custom layout -->
{@render children?.()}

<p>
  A library by <Link to="https://bylazar.com" label="Lazar" /> from 19234 ByteForce.
</p>

<style>
  p {
    text-align: center;
  }
  :global(a > span.title) {
    display: none;
  }
</style>
