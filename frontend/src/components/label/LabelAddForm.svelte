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

    let isSubmitLocked = true;
    $: isSubmitLocked = label.labelId.trim() === '' || label.colorCode.trim() === '' || label.textColor.trim() === ''

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
        labels.toggleAddModeLabel()
    }

    function generateRandomColor() {
        const red = Math.floor(Math.random() * 256);
        const green = Math.floor(Math.random() * 256);
        const blue = Math.floor(Math.random() * 256);
        const colorCode = `#${red.toString(16).padStart(2, "0").toUpperCase()}${green.toString(16).padStart(2, "0").toUpperCase()}${blue.toString(16).padStart(2, "0").toUpperCase()}`;
        const rgb = {r: red, g: green, b: blue};
        const hsl = rgbToHsl(rgb);
        return {colorCode, rgb, hsl};
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
        const {colorCode} = generateRandomColor();
        updateLabelColor(colorCode);
    }

    function onTextColorChange(event) {
        label.textColor = event.target.value;
    }
</script>

<div class="label-form-container animate-slidein">
    <div class="flex flex-col gap-2 justify-between">
        <div class="label-form-header">새로운 레이블 추가</div>
        <div class="flex items-center">
            <!--      레이블 미리보기 창      -->
            <div class="label-form-preview-container">
                <div class="label"
                     style="--label-r: {rgb.r}; --label-g: {rgb.g}; --label-b: {rgb.b}; --label-h: {hsl.h}; --label-s: {hsl.s}; --label-l: {hsl.l}; color: {label.textColor}">
                    {#if label.labelId}
                        <div>{label.labelId}</div>
                    {:else}
                        <div>Label Preview</div>
                    {/if}
                </div>
            </div>
            <div class="label-form-input-container">
                <div class="flex flex-col gap-3" role="group">
                    <div class="label-form-input-box">
                        <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                            이름
                        </span>
                        <input id="labelId" class="px-[6rem]" type="text" bind:value={label.labelId}
                               placeholder="레이블의 이름을 입력하세요" maxlength="20"/>
                    </div>
                    <div class="label-form-input-box">
                        <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                            설명(선택)
                        </span>
                        <input id="description" class="px-[6rem]" type="text" bind:value={label.description}
                               placeholder="레이블에 대한 설명을 입력하세요" maxlength="50"/>
                    </div>
                    <div class="label-form-input-box-bg">
                        <div class="max-w-[300px] rounded-e-md relative">
                            <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                              배경 색상
                            </span>
                            <input id="colorCode" class="px-[6rem]" type="text" bind:value={label.colorCode}
                                   on:input={onColorCodeInput} maxlength="7"/>
                            <!-- 회전 버튼 -->
                            <button class="absolute right-5 top-2.5 rotate-btn rotate-45"
                                    on:click={onGenerateRandomColorClick}>
                                <span class="text-lg">
                                    <i class="bi bi-arrow-repeat"></i>
                                </span>
                            </button>
                        </div>
                        <div class="label-form-select-box">
                            <label class="text-sm" for="textColor">텍스트 색상</label>
                            <select id="textColor" class="outline-0" on:change={onTextColorChange} bind:value={label.textColor}>
                                <option class="text-sm" value={lightColor}>밝은 색</option>
                                <option class="text-sm" value={darkColor}>어두운 색</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="edit-title-container gap-3">
            <button class="edit-title-btn blue-border gap-2" on:click={onCancelAddLabel}>
                <span class="">
                    <i class="bi bi-x-lg"></i>
                </span>
                취소
            </button>
            <button class="edit-title-btn apply gap-2" on:click={onAddLabel} disabled={isSubmitLocked}>
                <span class="text-white">
                    <i class="bi bi-plus-lg"></i>
                </span>
                완료
            </button>
        </div>
    </div>
</div>
