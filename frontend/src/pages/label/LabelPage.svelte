<script>
    import { labels } from "../../stores/label.js";
    import { onMount } from "svelte";
    import LabelList from "../../components/label/LabelList.svelte";
    import LabelAddForm from "../../components/label/LabelAddForm.svelte";
    import LabelMilestoneNavTab from "../../components/common/LabelMilestoneNavtab.svelte";

    onMount(() => {
        labels.fetchLabels();
    });

    function toggleAddMode() {
        labels.toggleAddModeLabel()
    }
</script>

<div class="flex my-[3rem] justify-between items-center w-full min-w-[1020px]">
    <LabelMilestoneNavTab />
    <button on:click={toggleAddMode} class="btn issue create max-h-[44px] h-lvh" class:addMode={$labels.addMode}>
        <span>
            <i class="bi bi-plus-lg"></i>
        </span>
        레이블 추가
    </button>
</div>

<div class="mb-[3rem]">
    {#if $labels.addMode}
        <LabelAddForm />
    {/if}
</div>

<LabelList />
