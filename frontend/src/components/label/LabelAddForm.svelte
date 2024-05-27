<script>
  import {hexToRgb, rgbToHsl} from "../../utils/color.js";
  import {labels} from '../../stores/label.js'
  import {labelValidate} from "../../utils/validates.js";

  const defaultBgColor = "#004DE3";
  const lightColor = "#FFFFFF";
  const darkColor = "#000000";

  let label = {
    labelId: "",
    description: "",
    colorCode: defaultBgColor,
    textColor: lightColor
  }

  $: rgb = hexToRgb(label.colorCode);
  $: hsl = rgbToHsl(rgb);

  const onAddLabel = async () => {
    try {
      await labelValidate.validate(label, {abortEarly: true})
      await labels.registerLabel(label)
      onCancelAddLabel()
    } catch (err) {
      alert(err)
    }
  }
  
  const onCancelAddLabel = () => {
    label.labelId = "";
    label.description = "";
    label.colorCode = defaultBgColor;
    label.textColor = lightColor;
  }

  function generateRandomColor() {
    const red = Math.floor(Math.random() * 256);
    const green = Math.floor(Math.random() * 256);
    const blue = Math.floor(Math.random() * 256);
    const colorCode = `#${red.toString(16).padStart(2, "0").toUpperCase()}${green.toString(16).padStart(2, "0").toUpperCase()}${blue.toString(16).padStart(2, "0").toUpperCase()}`;
    const rgb = { r: red, g: green, b: blue };
    const hsl = rgbToHsl(rgb);
    return { colorCode, rgb, hsl };
  }

  function determineTextColor(rgb) {
    const perceivedLightness = (rgb.r * 0.299 + rgb.g * 0.587 + rgb.b * 0.114) / 255;
    return perceivedLightness > 0.453 ? darkColor : lightColor;
  }

  function updateLabelColor(colorCode) {
    const rgb = hexToRgb(colorCode);
    label.colorCode = colorCode;
    label.textColor = determineTextColor(rgb);
  }

  function onColorCodeInput(event) {
    updateLabelColor(event.target.value);
  }

  function onGenerateRandomColorClick() {
    const { colorCode } = generateRandomColor();
    updateLabelColor(colorCode);
  }

  function onTextColorChange(event) {
    label.textColor = event.target.value;
  }
</script>

<div class="label-form-container">
  <div class="label" style="--label-r: {rgb.r}; --label-g: {rgb.g}; --label-b: {rgb.b}; --label-h: {hsl.h}; --label-s: {hsl.s}; --label-l: {hsl.l}; color: {label.textColor}">
    {#if label.labelId}
      <div>{label.labelId}</div>
    {:else}
      <div>Label Preview</div>
    {/if}
  </div>

  <div>
    <div>
      <label for="labelId">이름</label>
      <input id="labelId" type="text" bind:value={label.labelId} placeholder="레이블의 이름을 입력하세요" maxlength="20" />
    </div>
    <div>
      <label for="description">설명(선택)</label>
      <input id="description" type="text" bind:value={label.description} placeholder="레이블에 대한 설명을 입력하세요" maxlength="50" />
    </div>
    <div>
      <label for="colorCode">배경 색상</label>
      <input id="colorCode" type="text" bind:value={label.colorCode} on:input={onColorCodeInput} maxlength="7" />
      <button on:click={onGenerateRandomColorClick}>배경 색상 변경</button>
    </div>
    <div>
      <label for="textColor">텍스트 색상</label>
      <select id="textColor" on:change={onTextColorChange} bind:value={label.textColor}>
        <option value={lightColor}>밝은 색</option>
        <option value={darkColor}>어두운 색</option>
      </select>
    </div>
  </div>

  <div>
    <button on:click={onCancelAddLabel}>취소</button>
    <button on:click={onAddLabel}>완료</button>
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
</style>
