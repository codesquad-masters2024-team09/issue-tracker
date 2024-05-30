<script>
    import { tags } from "../../stores/tags"
    import { issues } from "../../stores/issue"
    import {onMount} from "svelte";

    export let itemType

    let issueData
    $: issueData

    let currentUrl
    $: currentUrl

    let issueId
    $: issueId

    let canShowMilestone
    $: canShowMilestone = $tags.selectedMilestone !== null && $tags.selectedMilestone !== ''

    onMount(async () => {
        currentUrl = window.location.href
        if (!currentUrl.endsWith('add')) {
            issueId = currentUrl.substring(currentUrl.lastIndexOf('/') + 1);
            issueData = await issues.fetchIssueDetail(issueId);

            if (itemType === 'assignee') {
                tags.initAssigneeCheckedState($tags.members, issueData.assignees)
                return
            }

            if (itemType === 'label') {
                tags.initLabelCheckedState($tags.labels, issueData.labels)
                return
            }

            if (itemType === 'milestone') {
                tags.initMilestoneCheckedState($tags.milestones, issueData.milestoneId)
            }
        }
    })

    const onClickAssigneeName = async (selectedAssignee) => {
        if (currentUrl.endsWith('add')) {
            if ($tags.checkedStates.assignees[selectedAssignee.memberId] === true) {
                await tags.deleteAssignee(selectedAssignee)
            } else {
                await tags.selectAssignee(selectedAssignee)
            }
        } else {
            if ($tags.checkedStates.assignees[selectedAssignee.memberId] === true) {
                await tags.deleteAssignee(selectedAssignee)
                await tags.deleteAssigneeOnIssue(issueId, selectedAssignee.memberId)
            } else {
                await tags.selectAssignee(selectedAssignee)
                await tags.addAssigneeOnIssue(issueId, selectedAssignee.memberId)
            }
        }
    }

    const onClickLabelName = async (selectedLabel) => {
        if (currentUrl.endsWith('add')) {
            if ($tags.checkedStates.labels[selectedLabel.labelId] === true) {
                await tags.deleteLabel(selectedLabel)
            } else {
                await tags.selectLabel(selectedLabel)
            }
        }
        else {
            if ($tags.checkedStates.labels[selectedLabel.labelId] === true) {
                await tags.deleteLabel(selectedLabel)
                await tags.deleteLabelOnIssue(issueId, selectedLabel.labelId)
            } else {
                await tags.selectLabel(selectedLabel)
                await tags.addLabelOnIssue(issueId, selectedLabel.labelId)
            }
        }
    }

    const onClickMilestone = async (selectedMilestoneId) => {
        console.log('마일스톤: 삭제전 체크상태', $tags.checkedStates.milestone)
        if (currentUrl.endsWith('add')) {
            if ($tags.checkedStates.milestone[selectedMilestoneId] === true) {
                await tags.deleteMilestone(selectedMilestoneId)
            } else {
                await tags.selectMilestone(selectedMilestoneId)
            }
        }
        else {
            if ($tags.checkedStates.milestone[selectedMilestoneId] === true) {
                await tags.deleteMilestone(selectedMilestoneId)
                await tags.deleteMilestoneOnIssue(issueId)
            } else {
                console.log('보여지는 마일스톤:', selectedMilestoneId)
                await tags.selectMilestone(selectedMilestoneId)
                await tags.assignMilestoneOnIssue(issueId, selectedMilestoneId)
            }
        }
    }
</script>
{#if itemType === 'assignee' && $tags.selectedAssignees.length > 0}
    <div class="selected-items-list">
        {#each $tags.selectedAssignees as assignee}
            <div class="selected-items-container">
                <button class="selected-items-label" on:click={() => onClickAssigneeName(assignee)}>
                <span class="text-[11px] -translate-x-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </span>
                    <span class="label-badge border border-gray-200">
                    {assignee.memberId}
                </span>
                </button>
            </div>
        {/each}
    </div>
{/if}

{#if itemType === 'label' && $tags.selectedLabels.length > 0}
    <div class="selected-items-list">
    {#each $tags.selectedLabels as label}
        <div class="selected-items-container">
            <button class="selected-items-label" on:click={() => onClickLabelName(label)}>
                <span class="text-[11px] -translate-x-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </span>
                <span class="label-badge border border-gray-200"
                     style="background-color: {label.colorCode}; color: {label.textColor};">
                    {label.labelId}
                </span>
            </button>
        </div>
    {/each}
    </div>
{/if}

{#if itemType === 'milestone' && canShowMilestone}
    <div class="selected-items-list">
        <div class="selected-items-container">
            <button class="selected-items-label" on:click={() => onClickMilestone($tags.selectedMilestone)}>
                <span class="selected-items-cancel">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-circle" viewBox="0 0 16 16">
                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </span>
                <span class="milestone-badge">
                    {$tags.selectedMilestone}
                </span>
            </button>
        </div>
    </div>
{/if}