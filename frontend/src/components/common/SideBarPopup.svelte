<script>
    import {issues} from "../../stores/issue.js"
    import { tags } from '../../stores/tags.js'
    import { onMount } from "svelte"

    export let headerText

    let issueData
    $: issueData

    let currentUrl
    $: currentUrl

    onMount(async () => {
        currentUrl = window.location.href
        if (!currentUrl.endsWith('add')) {
            const issueId = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)
            issueData = await issues.fetchIssueDetail(issueId);
            console.log('새로 로딩된 issueData:', issueData)

            if (headerText.startsWith('담당자')) {
                tags.initAssigneeCheckedState($tags.members, issueData.assignees);
                console.log('체크 상태 초기화 :', $tags.checkedStates)
            }

            if (headerText.startsWith('레이블')) {
                tags.initLabelCheckedState($tags.labels, issueData.labels)
                console.log('체크 상태 초기화 :', $tags.checkedStates)
            }

            if (headerText.startsWith('마일스톤')) {
                tags.initMilestoneCheckedState($tags.milestones, issueData.milestoneId)
                console.log('체크 상태 초기화 :', $tags.checkedStates)
            }
        }
    })

    const onClickAssigneeName = async (selectedAssignee) => {
        if (currentUrl.endsWith('add')) {
            if ($tags.checkedStates.assignees[selectedAssignee.memberId] === true) {
                await tags.deleteAssignee(selectedAssignee.memberId);
            } else {
                await tags.selectAssignee(selectedAssignee.memberId);
            }
        } else {
            if ($tags.checkedStates.assignees[selectedAssignee.memberId] === true) {
                await tags.deleteAssignee(selectedAssignee.memberId);
                await tags.deleteAssigneeOnIssue(issueData.id, selectedAssignee.memberId);
            } else {
                await tags.selectAssignee(selectedAssignee.memberId);
                await tags.addAssigneeOnIssue(issueData.id, selectedAssignee.memberId);
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
                await tags.deleteLabelOnIssue(issueData.id, selectedLabel.labelId)
            } else {
                await tags.selectLabel(selectedLabel)
                await tags.addLabelOnIssue(issueData.id, selectedLabel.labelId)
            }
        }
    }

    const onClickMilestone = async (selectedMilestone) => {
        if (currentUrl.endsWith('add')) {
            if ($tags.checkedStates.milestone[selectedMilestone.id] === true) {
                await tags.deleteMilestone(selectedMilestone.id)
            } else {
                await tags.selectMilestone(selectedMilestone.id)
            }
        }
        else {
            if ($tags.checkedStates.milestone[selectedMilestone.id] === true) {
                await tags.deleteMilestone(selectedMilestone.id)
                await tags.deleteMilestoneOnIssue(issueData.id)
            } else {
                await tags.selectMilestone(selectedMilestone.id)
                await tags.assignMilestoneOnIssue(issueData.id, selectedMilestone.id)
            }
        }
    }
</script>

<div class="sidebar-popup-container">
    <div class="sidebar-popup-header">{headerText}</div>
    {#if headerText.startsWith('담당자')}
        {#each $tags.members as assignee}
            <div class="sidebar-popup-item" role="menuitemcheckbox" aria-checked={$tags.checkedStates.assignees[`${assignee.memberId}`]} tabindex="0" data-name={assignee.memberId}
                 on:click={(e) => {
                     e.stopPropagation();
                     onClickAssigneeName(assignee);
                 }}
            >
                <div class="sidebar-popup-item-detail">
                    <span class="sidebar-popup-label">{assignee.memberId}</span>
                    {#if $tags.checkedStates.assignees[assignee.memberId]}
                        <svg width="16" height="17" viewBox="0 0 16 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 15.1666C11.6818 15.1666 14.6666 12.1818 14.6666 8.49992C14.6666 4.81802 11.6818 1.83325 7.99992 1.83325C4.31802 1.83325 1.33325 4.81802 1.33325 8.49992C1.33325 12.1818 4.31802 15.1666 7.99992 15.1666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M10.9999 6.83325L7.33325 10.5066L5.33325 8.50659" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {:else}
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 14.6666C11.6818 14.6666 14.6666 11.6818 14.6666 7.99992C14.6666 4.31802 11.6818 1.33325 7.99992 1.33325C4.31802 1.33325 1.33325 4.31802 1.33325 7.99992C1.33325 11.6818 4.31802 14.6666 7.99992 14.6666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {/if}
                </div>
            </div>
        {/each}
        {#if !$tags.members}
            <div class="sidebar-popup-item">
                담당자가 존재하지 않습니다.
            </div>
        {/if}
    {/if}

    {#if headerText.startsWith('레이블')}
        {#each $tags.labels as label}
            <div class="sidebar-popup-item" role="menuitemcheckbox" aria-checked={$tags.checkedStates.labels[`${label.labelId}`]} tabindex="0" data-name={label.labelId}
                 on:click={(e) => {
                     e.stopPropagation();
                     onClickLabelName(label);
                 }}
            >
                <div class="sidebar-popup-item-detail">
                    <div class="sidebar-popup-label-circle" style="background-color: {label.colorCode};"></div>
                    <span class="sidebar-popup-label">{label.labelId}</span>
                    {#if $tags.checkedStates.labels[label.labelId]}
                        <svg width="16" height="17" viewBox="0 0 16 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 15.1666C11.6818 15.1666 14.6666 12.1818 14.6666 8.49992C14.6666 4.81802 11.6818 1.83325 7.99992 1.83325C4.31802 1.83325 1.33325 4.81802 1.33325 8.49992C1.33325 12.1818 4.31802 15.1666 7.99992 15.1666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M10.9999 6.83325L7.33325 10.5066L5.33325 8.50659" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {:else}
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 14.6666C11.6818 14.6666 14.6666 11.6818 14.6666 7.99992C14.6666 4.31802 11.6818 1.33325 7.99992 1.33325C4.31802 1.33325 1.33325 4.31802 1.33325 7.99992C1.33325 11.6818 4.31802 14.6666 7.99992 14.6666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {/if}
                </div>
            </div>
        {/each}
        {#if !$tags.labels}
            <div class="sidebar-popup-item">
                레이블이 존재하지 않습니다.
            </div>
        {/if}
    {/if}

    {#if headerText.startsWith('마일스톤')}
        {#each $tags.milestones as milestone}
            <div class="sidebar-popup-item" role="menuitemcheckbox" aria-checked={$tags.checkedStates.milestone[`${milestone.id}`]} tabindex="0" data-name={milestone.id}
                 on:click={() => onClickMilestone(milestone)}>
                <div class="sidebar-popup-item-detail">
                    <span class="sidebar-popup-label">{milestone.id}</span>
                    {#if $tags.checkedStates.milestone[milestone.id]}
                        <svg width="16" height="17" viewBox="0 0 16 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 15.1666C11.6818 15.1666 14.6666 12.1818 14.6666 8.49992C14.6666 4.81802 11.6818 1.83325 7.99992 1.83325C4.31802 1.83325 1.33325 4.81802 1.33325 8.49992C1.33325 12.1818 4.31802 15.1666 7.99992 15.1666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M10.9999 6.83325L7.33325 10.5066L5.33325 8.50659" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {:else}
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 14.6666C11.6818 14.6666 14.6666 11.6818 14.6666 7.99992C14.6666 4.31802 11.6818 1.33325 7.99992 1.33325C4.31802 1.33325 1.33325 4.31802 1.33325 7.99992C1.33325 11.6818 4.31802 14.6666 7.99992 14.6666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {/if}
                </div>
            </div>
        {/each}
        {#if !$tags.milestones}
            <div class="sidebar-popup-item">
                마일스톤이 존재하지 않습니다.
            </div>
        {/if}
    {/if}
</div>
