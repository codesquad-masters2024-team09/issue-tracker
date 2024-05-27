<script>
    export let issueId
    export let defaultContent
    export let isFocused

    import { issues } from '../../stores/issue'
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    $: form = {
        content: defaultContent
    }

    /* 편집 버튼 제어 */
    let isContentUpdateLocked = true;
    $: {
        isContentUpdateLocked = form.content.trim() === ''
    }

    const onCloseEditModeIssueContent = () => {
        issues.closeEditModeIssueContent()
        isFocused = false
    }

    const onUpdateIssue = () => {
        issues.updateIssue(issueId, form)
        dispatch('updateIssueContent', defaultContent)
    }
</script>

<div class={`${isFocused ? 'content-box main-border flex gap-2 justify-start items-center' : 'content-box main flex gap-2 justify-start items-center'}`}>
    <textarea placeholder="내용을 입력하세요" class="comment-edit-textarea"
              bind:value={defaultContent} on:focus={() => isFocused = true} on:blur={() => isFocused = false} />
</div>


<!-- 버튼 컨테이너 -->
<div class="edit-title-contianer mt-3">
    <!-- 편집 취소 버튼 -->
    <button id="edit-title-cancel" class="edit-title-btn blue-border" type="button" on:click={onCloseEditModeIssueContent}>
        <span class="text-[12px] text-center pr-1">
            <i class="bi bi-x-lg"></i>
        </span>
        편집 취소
    </button>
    <!-- 편집 완료 버튼 -->
    <button id="edit-title-apply" type="button" class="edit-title-btn apply" disabled={isContentUpdateLocked} on:click={onUpdateIssue}>
        <span class="text-[12px] text-center pr-1">
            <i class="bi bi-archive"></i>
        </span>
        편집 완료
    </button>
</div>
