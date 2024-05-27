<script>
    import LabelEditForm from "./LabelEditForm.svelte";
    import {hexToRgb, rgbToHsl} from "../../utils/color.js";
    import {labels} from "../../stores/label.js";

    export let label;

    $: rgb = hexToRgb(label.colorCode);
    $: hsl = rgbToHsl(rgb);

    const onEditModeLabel = (id) => {
        labels.openEditModeLabel(id)
    }

    const onDeleteLabel = (id) => {
        if (confirm('삭제하시겠습니까?')) {
            labels.deleteLabel(id)
        }
    }
</script>

<div class="issue-table-row">
    {#if $labels.editMode === label.labelId}
        <LabelEditForm {label}/>
    {:else}
        <div class="label-row">
            <!--      레이블 미리보기      -->
            <div class="label-preview-container">
                <div class="label"
                     style="--label-r: {rgb.r}; --label-g: {rgb.g}; --label-b: {rgb.b}; --label-h: {hsl.h}; --label-s: {hsl.s}; --label-l: {hsl.l}; color: {label.textColor}">
                    <div>{label.labelId}</div>
                </div>
            </div>

            <!--      레이블 설명      -->
            <div class="label-description">
                <span>{label.description}</span>
            </div>

            <!--      레이블 편집 버튼      -->
            <div class="label-btn-container ml-auto flex gap-2 mx-4 my-1 items-center text-sm whitespace-nowrap">
                <button class="text-gray-800" on:click={() => onEditModeLabel(label.labelId)}>
                    <span>
                        <i class="bi bi-pencil-square"></i>
                    </span>
                    편집
                </button>
                <button class="text-red-500" on:click={() => onDeleteLabel(label.labelId)}>
                    <span>
                        <i class="bi bi-trash"></i>
                    </span>
                    삭제
                </button>
            </div>
        </div>
    {/if}
</div>
