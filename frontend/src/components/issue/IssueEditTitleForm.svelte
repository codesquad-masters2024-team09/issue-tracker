<script>
    export let issueId
    export let defaultTitle

    import { issues } from '../../stores/issue'
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    $: form = {
        title: defaultTitle
    }

    /* 편집 버튼 제어 */
    let isTitleUpdateLocked = true;
    $: {
        isTitleUpdateLocked = form.title.trim() === ''
    }

    const onCloseEditModeIssueTitle = () => {
        issues.closeEditModeIssueTitle()
    }

    const onUpdateIssue = () => {
        issues.updateIssue(issueId, form)
        dispatch('updateIssueTitle', defaultTitle)
    }
</script>


<div class="flex w-full rounded-e-md relative mr-2">
    <span class="absolute translate-y-2 ml-1 top-[9px] left-[1rem] text-[14px] text-gray-500 pointer-events-none">
        제목
    </span>
    <input id="title-text" class="m-1 px-[6rem] bg-neutral-200/50 whitespace-nowrap" type="text" placeholder="수정할 제목을 입력해 주세요."
        bind:value={defaultTitle} />
</div>

<!-- 버튼 컨테이너 -->
<div class="edit-title-contianer">
    <!-- 편집 취소 버튼 -->
    <button id="edit-title-cancel" class="edit-title-btn blue-border" type="button" on:click={onCloseEditModeIssueTitle}>
        <span class="text-[12px] text-center pr-1">
            <i class="bi bi-x-lg"></i>
        </span>
        편집 취소
    </button>
    <!-- 편집 완료 버튼 -->
    <button id="edit-title-apply" type="button" class="edit-title-btn apply" disabled={isTitleUpdateLocked} on:click={onUpdateIssue}>
        <span class="text-[12px] text-center pr-1">
            <i class="bi bi-archive"></i>
        </span>
        편집 완료
    </button>
</div>
