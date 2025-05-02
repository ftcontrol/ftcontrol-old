export const baseStyles = `
      .wrapper {
        --bg: #f6f6f6;
        --card: #ffffff;
        --cardTransparent: #ffffff50;
        --text: #1b1b131414;

        --primary: #e60012;
        --primary: #005bac;
      }

      .wrapper.dark-mode {
        --bg: #1b1b1b;
        --card: #131314;
        --cardTransparent: #13131450;
        --text: #c4c7c5;
      }

      .wrapper.instant {
        --multiplier: 0;
      }
      .wrapper.fast {
        --multiplier: 0.1;
      }
      .wrapper.normal {
        --multiplier: 0.15;
      }
      .wrapper.slow {
        --multiplier: 0.225;
      }

      .wrapper {
        --d: calc(var(--multiplier) * 1s);
        --d1: calc(var(--multiplier) * 1s);
        --d2: calc(var(--multiplier) * 2s);
        --d3: calc(var(--multiplier) * 3s);
      }

      .wrapper.blue {
        --primary: #005bac;
      }

      .wrapper.red {
        --primary: #e60012;
      }
      .wrapper{
        width: 100%;
        height: 100%;
      }
      iframe{
        outline: none;
        border: none;
        background-color:white;
      }

      .widget-header {
        display: flex;
        gap: 1rem;
        justify-content: space-between;
        align-items: center;
        position: relative;
        margin-inline: 1rem;
        padding-top: 0.5rem;
        margin-bottom: 0.5rem;
      }

      .widget-header > p {
        margin: 0;
        text-align: center;
        flex-grow: 1;
        font-size: 1.25rem;
        font-weight: bold;
      }
    `
