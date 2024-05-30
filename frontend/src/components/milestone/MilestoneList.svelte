<script>
    import {milestones} from "../../stores/milestone.js";
    import MilestonePreview from "./MilestonePreview.svelte";
    import NoMilestone from "./NoMilestone.svelte";

    $: openMilestones = $milestones.milestones.filter(milestone => milestone.open === true)
    $: closedMilestones = $milestones.milestones.filter(milestone => milestone.open === false)

    $: isOpenMilestoneView = true

    const onOpenMilestonesView = () => {
        isOpenMilestoneView = true
    }

    const onClosedMilestonesView = () => {
        isOpenMilestoneView = false
    }
</script>

<div class="flex flex-col w-full min-w-[1020px] items-center">
    <div class="issue-table-header">
        <button class="issue-filter open" on:click={onOpenMilestonesView}>
            <span class="pr-[3px]">
                <i class="bi bi-exclamation-circle"></i>
            </span>
            <span class={isOpenMilestoneView ? 'font-bold text-black' : ''}>열린 마일스톤({openMilestones.length})</span>
        </button>
        <button class="issue-filter closed" on:click={onClosedMilestonesView}>
            <span class="pr-[3px]">
                <i class="bi bi-archive"></i>
            </span>
            <span class={!isOpenMilestoneView ? 'font-bold text-black' : ''}>닫힌 마일스톤({closedMilestones.length})</span>
        </button>
    </div>
    {#if isOpenMilestoneView}
        {#each openMilestones as milestone}
            <MilestonePreview {milestone} />
        {/each}
        {#if openMilestones.length === 0}
            <NoMilestone isOpen={true} />
        {/if}
    {:else}
        {#each closedMilestones as milestone}
            <MilestonePreview {milestone} />
        {/each}
        {#if closedMilestones.length === 0}
            <NoMilestone isOpen={false} />
        {/if}
    {/if}
</div>
