<script>
  import LabelPreview from "./LabelPreview.svelte";
  import {labels} from "../../store/label.js";

</script>

<div class="label-list-box">
  <div class="label-list">
    <h3>{$labels.labels.length}개의 레이블</h3>
  </div>
  <div class="label-element">
    {#each $labels.labels as label}
      <LabelPreview {label} />
    {/each}
  </div>
</div>

<style>
  h3 {
    font-size: 14px;
    line-height: 24px;
    margin: 0px;
  }
  .label-list-box {
    border: 1px solid #d9dbe9;
    border-radius: 16px;
  }
  .label-list {
    border-color: transparent;
    border-radius: 16px 16px 0 0;
    padding: 16px;
    background: #f7f7fc;
  }
</style>
