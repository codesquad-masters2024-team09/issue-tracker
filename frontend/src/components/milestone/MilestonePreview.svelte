<script>
    import {milestones} from "../../stores/milestone.js";
    import MilestoneEditForm from "./MilestoneEditForm.svelte";

    export let milestone;

    const formatDateStr = (isoString) => {
        const date = new Date(isoString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    $: milestone.dueDate = milestone.dueDate ? formatDateStr(milestone.dueDate) : milestone.dueDate;

    const onEditModeMilestone = (id) => {
        milestones.openEditModeMilestone(id)
    }

    const onDeleteMilestone = (id) => {
        if(confirm('삭제하시겠습니까?')) {
            milestones.deleteMilestone(id)
        }
    }
</script>

<div class="milestone-item">
    {#if $milestones.editMode === milestone.id}
        <MilestoneEditForm {milestone} />
    {:else}
        <div>{milestone.id}</div>
        <div>
            {#if milestone.dueDate}
                <p>{milestone.dueDate}</p>
            {:else}
                <p>No due date<p>
            {/if}
        </div>
        <div>
            {#if milestone.description}
                <div class="milestone-description">
                    {milestone.description}
                </div>
            {/if}
        </div>
        <div class="label-actions">
            <button class="edit-btn" on:click={() => onEditModeMilestone(milestone.id)}>편집</button>
            <button class="delete-btn" on:click={() => onDeleteMilestone(milestone.id)}>삭제</button>
        </div>
    {/if}
</div>

<style>
    .milestone-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 16px;
        border-top: 1px solid #d9dbe9;
        background-color: #fefefe;
    }
    .milestone-description {
        flex: 1;
        padding-left: 10px;
        font-size: 14px;
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