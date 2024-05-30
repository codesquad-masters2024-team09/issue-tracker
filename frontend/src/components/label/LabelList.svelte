<script>
    import {labels} from "../../stores/label.js";
    import LabelPreview from "./LabelPreview.svelte";
    import NoLabel from "./NoLabel.svelte";

</script>

<div class="flex flex-col w-full min-w-[1020px] items-center">
    <div class="issue-table-header">
        <div class="ml-4 text-sm text-gray-600">
            <span>{$labels.labels.length}개의 레이블</span>
        </div>
    </div>

    {#each $labels.labels as label}
        <LabelPreview {label}/>
    {/each}

    {#if $labels.labels.length === 0}
        <NoLabel/>
    {/if}
</div>


