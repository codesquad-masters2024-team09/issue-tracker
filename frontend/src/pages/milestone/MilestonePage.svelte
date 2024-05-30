<script>
    import { milestones } from "../../stores/milestone.js";
    import MilestoneAddForm from "../../components/milestone/MilestoneAddForm.svelte";
    import MilestoneList from "../../components/milestone/MilestoneList.svelte";
    import { onMount } from "svelte";
    import LabelMilestoneNavTab from "../../components/common/LabelMilestoneNavtab.svelte";

    onMount(() => {
        milestones.fetchMilestones();
    });

    function toggleAddMode() {
        milestones.toggleAddModeMilestone()
    }
</script>

<div class="flex my-[3rem] justify-between items-center w-full min-w-[1020px]">
    <LabelMilestoneNavTab />
    <button on:click={toggleAddMode} class="btn issue create max-h-[44px] h-lvh" class:addMode={$milestones.addMode}>
        <span>
            <i class="bi bi-plus-lg"></i>
        </span>
        마일스톤 추가
    </button>
</div>

<div class="mb-[3rem]">
    {#if $milestones.addMode}
        <MilestoneAddForm />
    {/if}
</div>

<MilestoneList />
