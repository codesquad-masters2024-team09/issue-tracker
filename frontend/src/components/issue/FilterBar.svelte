<script>
    import FilterBarPopup from "./FilterBarPopup.svelte";

    let showPopup = '';

    const onShowPopup = (btnId) => {
        if (showPopup === btnId) {
            showPopup = '';
        }
        else {
            showPopup = btnId;
        }
    };
</script>

<div class="filterBar-container">
    <div class="filterBar-btn-container">
        <button id="assignee" type="button" on:click={(e) => onShowPopup(e.currentTarget.id)}>
            담당자
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'assignee'}
            <FilterBarPopup headerText="담당자 필터" />
        {/if}
    </div>
    <div class="filterBar-btn-container">
        <button id="label" type="button" on:click={(e) => onShowPopup(e.currentTarget.id)}>
            레이블
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'label'}
            <FilterBarPopup headerText="레이블 필터" />
        {/if}
    </div>
    <div class="filterBar-btn-container">
        <button id="milestone" type="button" on:click={(e) => onShowPopup(e.currentTarget.id)}>
            마일스톤
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'milestone'}
            <FilterBarPopup headerText="마일스톤 필터" />
        {/if}
    </div>
    <div class="filterBar-btn-container">
        <button id="writer" type="button" on:click={(e) => onShowPopup(e.currentTarget.id)}>
            작성자
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'writer'}
            <FilterBarPopup headerText="작성자 필터" />
        {/if}
    </div>
</div>
