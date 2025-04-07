<script lang="ts">
  let {
    isOpened,
    isVertical = true,
  }: { isOpened: boolean; isVertical?: boolean } = $props()
</script>

<svg
  width="100%"
  height="100%"
  class:opened={isOpened}
  class:vertical={isVertical}
  viewBox="0 0 11 7"
  version="1.1"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xml:space="preserve"
  style="fill-rule:evenodd;clip-rule:evenodd;stroke-linejoin:round;stroke-miterlimit:2;"
>
  <path
    d="M9.072,0.031l0.335,0.001c0.32,0.001 0.608,0.193 0.733,0.488c0.125,0.295 0.063,0.635 -0.159,0.866l-4.287,4.465c-0.15,0.157 -0.359,0.245 -0.577,0.245c-0.217,0 -0.426,-0.088 -0.577,-0.245l-4.317,-4.497c-0.222,-0.232 -0.285,-0.574 -0.158,-0.869c0.126,-0.295 0.417,-0.486 0.738,-0.485l0.331,0.001l3.406,3.547c0.151,0.158 0.36,0.246 0.577,0.246c0.218,0 0.427,-0.088 0.577,-0.246l3.378,-3.517Z"
  />
</svg>

<style>
  svg {
    height: 7px;
    width: fit-content;
    transform-origin: center;
    transform: rotate(-90deg);

    transition: transform 0.2s;
  }
  svg.opened {
    transform: rotate(0deg);
  }
  svg.vertical {
    transform: rotate(0deg);
  }
  svg.opened.vertical {
    transform: rotate(180deg);
  }

  path {
    fill: var(--text);
  }
</style>
