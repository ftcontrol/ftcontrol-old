<script lang="ts">
  let { isOpened }: { isOpened: boolean } = $props()
</script>

<svg
  class:opened={isOpened}
  width="100%"
  height="100%"
  viewBox="0 0 8 5"
  version="1.1"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xml:space="preserve"
  style="fill-rule:evenodd;clip-rule:evenodd;stroke-linejoin:round;stroke-miterlimit:2;"
>
  <path
    d="M1.307,1.361c-0.223,-0.232 -0.285,-0.574 -0.158,-0.869c0.126,-0.295 0.417,-0.486 0.738,-0.485c1.269,0.005 2.934,0.011 4.199,0.016c0.32,0.001 0.608,0.193 0.733,0.487c0.125,0.295 0.063,0.636 -0.159,0.867c-0.706,0.735 -1.546,1.61 -2.092,2.178c-0.151,0.158 -0.359,0.246 -0.577,0.246c-0.218,0 -0.426,-0.088 -0.577,-0.246c-0.549,-0.571 -1.395,-1.452 -2.107,-2.194Z"
  />
</svg>

<style>
  svg {
    height: 8px;
    width: fit-content;
    transform-origin: center;
    transition: transform 0.2s cubic-bezier(0.33, 1, 0.68, 1);
    transition: rotate 0.2s cubic-out;
  }
  svg.opened {
    transform: rotate(180deg);
  }
</style>
