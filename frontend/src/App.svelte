<script>
  import Router from './route.svelte'
  import { auth, isRefresh } from "./stores/auth.js";
  import {onMount} from "svelte";

  const refresh_time = 1000 * 60 * 30;

  onMount(() => {
    const onRefresh = setInterval(() => {
      if($isRefresh) {
        auth.refresh()
      } else {
        clearInterval(onRefresh)
      }
    }, refresh_time)
  })
</script>


<div id="main-container">
    <Router />
</div>
