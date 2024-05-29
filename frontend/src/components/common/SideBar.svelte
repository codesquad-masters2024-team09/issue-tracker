<script>
    import { onMount } from "svelte";
    import { tags } from '../../stores/tags'
    import SideBarPopup from "./SideBarPopup.svelte"
    import SideBarSelectedItems from "./SideBarSelectedItems.svelte";

    let isShowPopup = '';

    const onShowPopup = (btnId) => {
        const isSameButton = isShowPopup === btnId;

        if (isSameButton) {
            isShowPopup = '';
        } else {
            isShowPopup = btnId;
        }
    }

    onMount(() => {
        tags.fetchMembers().then(() => console.log('패널에 로딩된 담당자들', $tags.members))
        tags.fetchLabels().then(() => console.log('패널에 로딩된 레이블', $tags.labels))
        tags.fetchMilestones().then(() => console.log('패널에 로딩된 마일스톤', $tags.milestones))
    })
</script>

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