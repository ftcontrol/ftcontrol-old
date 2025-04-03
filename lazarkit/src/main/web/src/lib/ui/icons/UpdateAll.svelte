<script lang="ts">
  let { isActive = true }: { isActive?: boolean } = $props()
  let spinning = $state(false)

  function handleClick() {
    if (!isActive) return
    spinning = true
    setTimeout(() => {
      spinning = false
    }, 300)
  }
</script>

<svg
  class:disabled={!isActive}
  class:spin={spinning}
  width="100%"
  height="100%"
  viewBox="0 0 14 13"
  version="1.1"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xml:space="preserve"
  style="fill-rule:evenodd;clip-rule:evenodd;stroke-linejoin:round;stroke-miterlimit:2;"
  onclick={handleClick}
>
  <g>
    <path
      d="M3.654,5.076c-0.113,-0.097 -0.265,-0.133 -0.41,-0.099c-0.145,0.035 -0.264,0.136 -0.322,0.274c-0.329,0.858 -0.404,1.825 -0.145,2.779c0.639,2.363 3.076,3.761 5.438,3.122c2.363,-0.64 3.762,-3.077 3.122,-5.439c-0.366,-1.353 -1.321,-2.389 -2.515,-2.908c-0.109,-0.046 -0.17,-0.163 -0.147,-0.279c0.059,-0.297 0.171,-0.852 0.239,-1.189c0.015,-0.072 0.06,-0.134 0.123,-0.169c0.064,-0.036 0.141,-0.042 0.209,-0.017c1.763,0.679 3.191,2.159 3.722,4.121c0.883,3.262 -1.049,6.628 -4.311,7.511c-3.263,0.883 -6.628,-1.049 -7.511,-4.311c-0.432,-1.596 -0.191,-3.216 0.55,-4.559c0.111,-0.194 0.068,-0.439 -0.102,-0.583c-0.192,-0.166 -0.455,-0.389 -0.711,-0.607c-0.173,-0.147 -0.25,-0.377 -0.201,-0.597c0.05,-0.221 0.217,-0.396 0.436,-0.455c1.371,-0.371 3.683,-0.997 5.055,-1.369c0.218,-0.059 0.451,0.008 0.605,0.174c0.154,0.166 0.203,0.403 0.128,0.616c-0.436,1.24 -1.138,3.237 -1.533,4.359c-0.066,0.189 -0.222,0.333 -0.415,0.385c-0.194,0.053 -0.401,0.007 -0.553,-0.123c-0.249,-0.212 -0.525,-0.446 -0.751,-0.637Z"
    />
    <g transform="matrix(4.00107,0,0,4.00107,10.405,8.71918)"></g>
    <text
      x="4.247px"
      y="8.719px"
      style="font-family:'Varta-Bold', 'Varta';font-weight:700;font-size:4.001px;"
    >
      ALL
    </text>
  </g>
</svg>

<style>
  svg {
    height: 20px;
    cursor: pointer;
    transition: none;
  }

  svg.disabled {
    opacity: 0.5;
    cursor: default;
  }

  svg.spin {
    transform: rotate(360deg);
    transition: transform 0.3s ease-in-out;
  }
</style>
