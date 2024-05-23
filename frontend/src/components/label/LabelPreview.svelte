<script>
  import LabelEditForm from "./LabelEditForm.svelte";
  import {hexToRgb, rgbToHsl} from "../../utils/color.js";
  import {labels} from "../../store/label.js";

  export let label;

  $: rgb = hexToRgb(label.colorCode);
  $: hsl = rgbToHsl(rgb);

  const onEditModeLabel = (id) => {
    labels.openEditModeLabel(id)
  }

  const onDeleteLabel = (id) => {
    if(confirm('삭제하시겠습니까?')) {
      labels.deleteLabel(id)
    }
  }
</script>

<div class="label-item">
  {#if $labels.editMode === label.labelId}
    <LabelEditForm {label} />
  {:else}
    <div
      class="label"
      style="--label-r: {rgb.r}; --label-g: {rgb.g}; --label-b: {rgb.b}; --label-h: {hsl.h}; --label-s: {hsl.s}; --label-l: {hsl.l}; color: {label.textColor}"
    >
      <div>{label.labelId}</div>
    </div>
    <div class="label-description">{label.description}</div>
    <div class="label-actions">
      <button class="edit-btn" on:click={() => onEditModeLabel(label.labelId)}>편집</button>
      <button class="delete-btn" on:click={() => onDeleteLabel(label.labelId)}>삭제</button>
    </div>
  {/if}
</div>

<style>
  .label-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    border-top: 1px solid #d9dbe9;
    background-color: #fefefe;
  }
  .label-description {
    flex: 1;
    padding-left: 10px;
    font-size: 14px;
  }
  .label-actions {
    display: flex;
    gap: 10px;
  }
  .edit-btn,
  .delete-btn {
    background: none;
    border: none;
    cursor: pointer;
  }
  .edit-btn {
    color: #007bff;
  }
  .delete-btn {
    color: #dc3545;
  }
</style>
