<script>
    import { onMount } from "svelte";
    import { filters } from '../../stores/filter'

    export let headerText

    const onClickAssigneeName = async (selectedAssigneeId) => {
        if ($filters.checkedStates.assignee[selectedAssigneeId] === true) {
            await filters.deleteAssignee(selectedAssigneeId)
        } else {
            await filters.singleSelectAssignee(selectedAssigneeId)
        }
        await filters.searchIssueByFilterCondition()
    }

    const onClickLabelName = async (selectedLabel) => {
        if ($filters.checkedStates.labels[selectedLabel.labelId] === true) {
            await filters.deleteLabel(selectedLabel)
        } else {
            await filters.multiSelectLabel(selectedLabel)
        }
        await filters.searchIssueByFilterCondition()
    }

    const onClickMilestone = async (selectedMilestoneId) => {
        if ($filters.checkedStates.milestone[selectedMilestoneId] === true) {
            await filters.deleteMilestone(selectedMilestoneId)
        } else {
            await filters.singleSelectMilestone(selectedMilestoneId)
        }
        await filters.searchIssueByFilterCondition()
    }

    const onClickWriterName = async (selectedWriterId) => {
        if ($filters.checkedStates.writer[selectedWriterId] === true) {
            await filters.deleteWriter(selectedWriterId);
        } else {
            await filters.singleSelectWriter(selectedWriterId)
        }
        await filters.searchIssueByFilterCondition()
    }

    onMount(async () => {
        if (headerText.startsWith('담당자') || headerText.startsWith('작성자')) {
            await filters.fetchMembers()
            return
        }

        if (headerText.startsWith('레이블')) {
            await filters.fetchLabels()
            return
        }

        if (headerText.startsWith('마일스톤')) {
            await filters.fetchMilestones()
        }
    });
</script>


<div class="filterBar-popup-container">
    <div class="filterBar-popup-header">{headerText}</div>
    {#if headerText.startsWith('담당자')}
        {#each $filters.members as assignee}
            <div class="filterBar-popup-item" role="menuitemcheckbox" aria-checked={$filters.checkedStates.assignee[`${assignee.memberId}`]}
                 tabindex="0" data-name={assignee.memberId} on:click={() => {onClickAssigneeName(assignee.memberId)}}>
                <span class="filterBar-popup-item-detail">
                    <span class="filterBar-popup-label">{assignee.memberId}</span>
                    {#if $filters.checkedStates.assignee[assignee.memberId]}
                        <svg width="16" height="17" viewBox="0 0 16 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 15.1666C11.6818 15.1666 14.6666 12.1818 14.6666 8.49992C14.6666 4.81802 11.6818 1.83325 7.99992 1.83325C4.31802 1.83325 1.33325 4.81802 1.33325 8.49992C1.33325 12.1818 4.31802 15.1666 7.99992 15.1666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M10.9999 6.83325L7.33325 10.5066L5.33325 8.50659" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {:else}
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 14.6666C11.6818 14.6666 14.6666 11.6818 14.6666 7.99992C14.6666 4.31802 11.6818 1.33325 7.99992 1.33325C4.31802 1.33325 1.33325 4.31802 1.33325 7.99992C1.33325 11.6818 4.31802 14.6666 7.99992 14.6666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {/if}
                </span>
            </div>
        {/each}
        {#if $filters.members.length === 0}
            <div class="filterBar-popup-item">
                담당자가 존재하지 않습니다.
            </div>
        {/if}
    {/if}

    {#if headerText.startsWith('레이블')}
        {#each $filters.labels as label}
            <div class="filterBar-popup-item" role="menuitemcheckbox" aria-checked={$filters.checkedStates.labels[`${label.labelId}`]}
                 tabindex="0" data-name={label.labelId} on:click={(e) => {onClickLabelName(label)}}>
                <div class="filterBar-popup-item-detail">
                    <div class="filterBar-popup-label-circle" style="background-color: {label.colorCode};"></div>
                    <span class="filterBar-popup-label">{label.labelId}</span>
                    {#if $filters.checkedStates.labels[label.labelId]}
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
        {#if $filters.labels.length === 0}
            <div class="filterBar-popup-item">
                레이블이 존재하지 않습니다.
            </div>
        {/if}
    {/if}

    {#if headerText.startsWith('마일스톤')}
        {#each $filters.milestones as milestone}
            <div class="filterBar-popup-item" role="menuitemcheckbox" aria-checked={$filters.checkedStates.milestone[`${milestone.id}`]}
                 tabindex="0" data-name={milestone.id} on:click={() => onClickMilestone(milestone.id)}>
                <div class="filterBar-popup-item-detail">
                    <span class="filterBar-popup-label">{milestone.id}</span>
                    {#if $filters.checkedStates.milestone[milestone.id]}
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
        {#if $filters.milestones.length === 0}
            <div class="filterBar-popup-item">
                마일스톤이 존재하지 않습니다.
            </div>
        {/if}
    {/if}

    {#if headerText.startsWith('작성자')}
        {#each $filters.members as writer}
            <div class="filterBar-popup-item" role="menuitemcheckbox" aria-checked={$filters.checkedStates.writer[`${writer.memberId}`]}
                     tabindex="0" data-name={writer.memberId} on:click={() => {onClickWriterName(writer.memberId)}}>
                <span class="filterBar-popup-item-detail">
                    <span class="filterBar-popup-label">{writer.memberId}</span>
                    {#if $filters.checkedStates.writer[writer.memberId]}
                        <svg width="16" height="17" viewBox="0 0 16 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 15.1666C11.6818 15.1666 14.6666 12.1818 14.6666 8.49992C14.6666 4.81802 11.6818 1.83325 7.99992 1.83325C4.31802 1.83325 1.33325 4.81802 1.33325 8.49992C1.33325 12.1818 4.31802 15.1666 7.99992 15.1666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M10.9999 6.83325L7.33325 10.5066L5.33325 8.50659" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {:else}
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M7.99992 14.6666C11.6818 14.6666 14.6666 11.6818 14.6666 7.99992C14.6666 4.31802 11.6818 1.33325 7.99992 1.33325C4.31802 1.33325 1.33325 4.31802 1.33325 7.99992C1.33325 11.6818 4.31802 14.6666 7.99992 14.6666Z" stroke="#4E4B66" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    {/if}
                </span>
            </div>
        {/each}
        {#if $filters.members.length === 0}
            <div class="filterBar-popup-item">
                작성자가 존재하지 않습니다.
            </div>
        {/if}
    {/if}
</div>
