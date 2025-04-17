<script lang="ts">
  function getURL() {
    if (window.location.hostname == "localhost") return "http://192.168.43.1"
    return window.location.hostname
  }
</script>

<iframe src="http://{getURL()}:5801"></iframe>

<style>
  iframe {
    width: 100%;
    height: calc(100% - 0.5rem);
    margin: 0;
  }
</style>
