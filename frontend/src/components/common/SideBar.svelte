<script>
    import { onMount } from "svelte";
    import { tags } from '../../stores/tags'
    import SideBarPopup from "./SideBarPopup.svelte"
    import SideBarSelectedItems from "./SideBarSelectedItems.svelte";

    let isShowPopup = '';

    let mountComplete
    $: isFullMount = mountComplete

    const onShowPopup = (btnId) => {
        const isSameButton = isShowPopup === btnId;

        if (isSameButton) {
            isShowPopup = '';
        } else {
            isShowPopup = btnId;
        }
    }

    onMount(async () => {
        try {
            await Promise.all([
                tags.fetchMembers(),
                tags.fetchLabels(),
                tags.fetchMilestones()
            ]);

            console.log('패널에 로딩된 담당자들', $tags.members);
            console.log('패널에 로딩된 레이블', $tags.labels);
            console.log('패널에 로딩된 마일스톤', $tags.milestones);
        } finally {
            mountComplete = true
        }
    })

    const closePopup = () => {
        isShowPopup = ''
    }
</script>

{#if isFullMount}
    {#if isShowPopup !== ''}
        <div class="overlay" on:click={closePopup}></div>
    {/if}
<div class="sidebar-container">
    <div class="sidebar-group-box">
        <button id="assignees" type="button" class="sidebar-btn" on:click={(e) => onShowPopup(e.currentTarget.id)}>
            담당자
            <span class="absolute right-6"><i class="bi bi-plus"></i></span>
            {#if isShowPopup === 'assignees'}
                <SideBarPopup headerText="담당자 설정" />
            {/if}
        </button>
        <SideBarSelectedItems itemType="assignee" />
    </div>
    <div class="sidebar-group-box">
        <button id="labels" type="button" class="sidebar-btn" on:click={(e) => onShowPopup(e.currentTarget.id)}>레이블
            <span class="absolute right-6"><i class="bi bi-plus"></i></span>
            {#if isShowPopup === 'labels'}
                <SideBarPopup headerText="레이블 설정" />
            {/if}
        </button>
        <SideBarSelectedItems itemType="label" />
    </div>
    <div class="sidebar-group-box">
        <button id="milestones" type="button" class="sidebar-btn" on:click={(e) => onShowPopup(e.currentTarget.id)}>마일스톤
            <span class="absolute right-6"><i class="bi bi-plus"></i></span>
            {#if isShowPopup === 'milestones'}
                <SideBarPopup headerText="마일스톤 설정" />
            {/if}
        </button>
        <SideBarSelectedItems itemType="milestone" />
    </div>
</div>
{/if}