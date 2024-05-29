<script>
    import { milestones } from "../../stores/milestone.js";
    import MilestoneAddForm from "../../components/milestone/MilestoneAddForm.svelte";
    import MilestoneList from "../../components/milestone/MilestoneList.svelte";
    import { onMount } from "svelte";
    import Header from "../../components/common/Header.svelte";

    onMount(() => {
        milestones.fetchMilestones();
    });

    function toggleAddMode() {
        milestones.toggleAddModeMilestone()
    }
</script>

<Header />
<div class="milestone-page">
    <h1>마일스톤</h1>
    <button on:click={toggleAddMode} class:addMode={$milestones.addMode}>
        마일스톤 추가
    </button>

    {#if $milestones.addMode}
        <MilestoneAddForm />
    {/if}

    <MilestoneList />
</div>

<style>
    .milestone-page {
        padding: 20px;
    }

    button {
        margin: 10px 0;
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: opacity 0.3s;
    }

    button:hover {
        background-color: #0056b3;
    }

    button.addMode {
        opacity: 0.5;
    }
</style>