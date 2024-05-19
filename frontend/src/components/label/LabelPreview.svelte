<script>
  import LabelEditForm from "./LabelEditForm.svelte";

  export let label = {};
  let rgb = hexToRgb(label.colorCode);
  let hsl = rgbToHsl(rgb);

  function hexToRgb(hex) {
    hex = hex.replace(/^#/, "");
    const bigint = parseInt(hex, 16);
    const r = (bigint >> 16) & 255;
    const g = (bigint >> 8) & 255;
    const b = bigint & 255;
    return { r, g, b };
  }

  function rgbToHsl({ r, g, b }) {
    r /= 255;
    g /= 255;
    b /= 255;
    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    const l = (max + min) / 2;
    if (max === min) {
      return { h: 0, s: 0, l: Math.round(l * 100) };
    }
    const diff = max - min;
    const s = diff / (1 - Math.abs(2 * l - 1));
    let h;
    switch (max) {
      case r:
        h = ((g - b) / diff + (g < b ? 6 : 0)) % 6;
        break;
      case g:
        h = (b - r) / diff + 2;
        break;
      case b:
        h = (r - g) / diff + 4;
        break;
    }
    h = Math.round(h * 60);
    return { h, s: Math.round(s * 100), l: Math.round(l * 100) };
  }

  let editmode = false;
  function editmodeOn() {
    editmode = !editmode;
  }
</script>

<div class="label-item">
  {#if editmode}
    <LabelEditForm {label} />
  {:else}
    <div
      class="label-preview"
      style="--label-r: {rgb.r}; --label-g: {rgb.g}; --label-b: {rgb.b}; --label-h: {hsl.h}; --label-s: {hsl.s}; --label-l: {hsl.l}; color: {label.textColor}"
    >
      <div>{label.labelId}</div>
    </div>
    <div class="label-description">{label.description}</div>
    <div class="label-actions">
      <button class="edit-btn" on:click={editmodeOn}>편집</button>
      <button class="delete-btn">삭제</button>
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
  }
  .label-preview {
    --lightness-threshold: 0.453;
    --border-threshold: 0.96;
    --perceived-lightness: calc(
      (
          (var(--label-r) * 0.2126) + (var(--label-g) * 0.7152) +
            (var(--label-b) * 0.0722)
        ) / 255
    );
    --lightness-switch: max(
      0,
      min(
        calc((1 / (var(--lightness-threshold) - var(--perceived-lightness)))),
        1
      )
    );
    --border-alpha: max(
      0,
      min(calc((var(--perceived-lightness) - var(--border-threshold)) * 100), 1)
    );
    background: rgb(var(--label-r), var(--label-g), var(--label-b));
    display: inline-block;
    border-width: 1px;
    border-style: solid;
    border-color: hsla(
      var(--label-h),
      calc(var(--label-s) * 1%),
      calc((var(--label-l) - 25) * 1%),
      var(--border-alpha)
    );
    padding: 0px 10px;
    border-radius: 16px;
    line-height: 22px;
    font-size: 12px;
    font-weight: bold;
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
