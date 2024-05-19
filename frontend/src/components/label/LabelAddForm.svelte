<script>
  const defaultBgColor = "#004DE3";
  const lightColor = "#FFFFFF";
  const darkColor = "#000000";

  let labelId = "";
  let description = "";
  let colorCode = defaultBgColor;
  let textColor = "";

  let rgb = hexToRgb(colorCode);
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

  function generateRandomColor() {
    const red = Math.floor(Math.random() * 256);
    const green = Math.floor(Math.random() * 256);
    const blue = Math.floor(Math.random() * 256);
    const rgb = { r: red, g: green, b: blue };
    const colorCode = `#${red.toString(16).padStart(2, "0").toUpperCase()}${green.toString(16).padStart(2, "0").toUpperCase()}${blue.toString(16).padStart(2, "0").toUpperCase()}`;
    const hsl = rgbToHsl(rgb);
    updateTextColor(rgb);
    return { colorCode, rgb, hsl };
  }

  function determineTextColor(rgb) {
    const perceivedLightness =
      (rgb.r * 0.299 + rgb.g * 0.587 + rgb.b * 0.114) / 255;
    return perceivedLightness > 0.453 ? darkColor : lightColor;
  }

  function updateTextColor(rgb) {
    textColor = determineTextColor(rgb);
  }

  function onColorCodeInput(event) {
    colorCode = event.target.value;
    rgb = hexToRgb(colorCode);
    hsl = rgbToHsl(rgb);
    updateTextColor(rgb);
  }

  function onGenerateRandomColorClick() {
    const newColor = generateRandomColor();
    colorCode = newColor.colorCode;
    rgb = newColor.rgb;
    hsl = newColor.hsl;
  }

  function onTextColorChange(event) {
    textColor = event.target.value === lightColor ? lightColor : darkColor;
  }
</script>

<div class="label-form-container">
  <div
    class="label"
    style="--label-r: {rgb.r}; --label-g: {rgb.g}; --label-b: {rgb.b}; --label-h: {hsl.h}; --label-s: {hsl.s}; --label-l: {hsl.l};"
  >
    {#if labelId}
      <div>{labelId}</div>
    {:else}
      <div>Label</div>
    {/if}
  </div>

  <div>
    <div>
      <label for="labelId">이름</label>
      <input
        id="labelId"
        type="text"
        bind:value={labelId}
        placeholder="레이블의 이름을 입력하세요"
        maxlength="20"
      />
    </div>
    <div>
      <label for="description">설명(선택)</label>
      <input
        id="description"
        type="text"
        bind:value={description}
        placeholder="레이블에 대한 설명을 입력하세요"
        maxlength="50"
      />
    </div>
    <div>
      <label for="colorCode">배경 색상</label>
      <input
        id="colorCode"
        type="text"
        bind:value={colorCode}
        on:input={onColorCodeInput}
        maxlength="7"
      />
      <button on:click={onGenerateRandomColorClick}>배경 색상 변경</button>
    </div>
    <div>
      <label for="textColor">텍스트 색상</label>
      <select
        id="textColor"
        on:change={onTextColorChange}
        bind:value={textColor}
      >
        <option value="" disabled selected hidden>텍스트 색상</option>
        <option value={lightColor}>밝은 색</option>
        <option value={darkColor}>어두운 색</option>
      </select>
    </div>
  </div>

  <div>
    <button>취소</button>
    <button>완료</button>
  </div>
</div>

<style>
  .label-form-container {
    background-color: #fefefe;
    display: block;
    padding: 16px;
    margin: 20px 0 20px 0;
    border: 1px solid #d9dbe9;
    border-radius: 16px;
  }
  .label {
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
    color: hsl(0deg, 0%, calc(var(--lightness-switch) * 100%));
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
</style>
