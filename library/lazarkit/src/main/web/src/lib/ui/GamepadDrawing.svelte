<script lang="ts">
  import Section from "./primitives/Section.svelte"

  let { gamepad }: { gamepad: Gamepad } = $props()
</script>

<Section title="Gamepad">
  {#if gamepad != null}
    <div class="photo reversed">
      <img src="/controller/front.png" alt="" />
      <img
        class="overlay"
        src="/controller/l1.png"
        alt=""
        style="--value:{gamepad.buttons[4].value};"
      />
      <img
        class="overlay"
        src="/controller/l2.png"
        alt=""
        style="--value:{gamepad.buttons[6].value};"
      />
      <img
        class="overlay"
        src="/controller/r1.png"
        alt=""
        style="--value:{gamepad.buttons[5].value};"
      />
      <img
        class="overlay"
        src="/controller/r2.png"
        alt=""
        style="--value:{gamepad.buttons[7].value};"
      />
    </div>
    <div class="photo">
      <img src="/controller/top.png" alt="" />
      <img
        class="stick"
        src="/controller/left.png"
        alt=""
        style="--offsetX:{gamepad.axes[0]};--offsetY:{gamepad.axes[1]};"
      />
      <img
        class="stick"
        src="/controller/right.png"
        alt=""
        style="--offsetX:{gamepad.axes[2]};--offsetY:{gamepad.axes[3]};"
      />
      <img
        class="stick overlay"
        src="/controller/left2.png"
        alt=""
        style="--offsetX:{gamepad.axes[0]};--offsetY:{gamepad
          .axes[1]};--value:{gamepad.buttons[10].value};"
      />
      <img
        class="stick overlay"
        src="/controller/right2.png"
        alt=""
        style="--offsetX:{gamepad.axes[2]};--offsetY:{gamepad
          .axes[3]};--value:{gamepad.buttons[11].value};"
      />
      <img
        class="overlay"
        src="/controller/touchpad.png"
        alt=""
        style="--value:{gamepad.buttons[17].value};"
      />
      <img
        class="overlay"
        src="/controller/options.png"
        alt=""
        style="--value:{gamepad.buttons[9].value};"
      />
      <img
        class="overlay"
        src="/controller/share.png"
        alt=""
        style="--value:{gamepad.buttons[8].value};"
      />

      <img
        class="overlay"
        src="/controller/dpad_up.png"
        alt=""
        style="--value:{gamepad.buttons[12].value};"
      />
      <img
        class="overlay"
        src="/controller/dpad_down.png"
        alt=""
        style="--value:{gamepad.buttons[13].value};"
      />
      <img
        class="overlay"
        src="/controller/dpad_left.png"
        alt=""
        style="--value:{gamepad.buttons[14].value};"
      />
      <img
        class="overlay"
        src="/controller/dpad_right.png"
        alt=""
        style="--value:{gamepad.buttons[15].value};"
      />
      <img
        class="overlay"
        src="/controller/ps.png"
        alt=""
        style="--value:{gamepad.buttons[16].value};"
      />

      <img
        class="overlay"
        src="/controller/cross.png"
        alt=""
        style="--value:{gamepad.buttons[0].value};"
      />
      <img
        class="overlay"
        src="/controller/circle.png"
        alt=""
        style="--value:{gamepad.buttons[1].value};"
      />
      <img
        class="overlay"
        src="/controller/square.png"
        alt=""
        style="--value:{gamepad.buttons[2].value};"
      />
      <img
        class="overlay"
        src="/controller/triangle.png"
        alt=""
        style="--value:{gamepad.buttons[3].value};"
      />
    </div>
  {:else}
    <p>No gamepad connected</p>
  {/if}
</Section>

<style>
  .photo.reversed {
    transform: scaleX(-1) translateX(calc(1920 / 1080 * -1 * 200px));
    height: 120px;
  }
  .photo {
    height: 200px;
    position: relative;
    width: fit-content;
  }
  img {
    height: 200px;
    aspect-ratio: 1920 / 1080;
    position: absolute;
  }
  .stick {
    --offsetX: 0;
    --offsetY: 0;

    left: calc(var(--offsetX) * 10px);
    top: calc(var(--offsetY) * 10px);
  }
  .overlay {
    --value: 0;
    opacity: var(--value);
  }
</style>
