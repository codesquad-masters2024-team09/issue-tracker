<script>
    import OptionPopup from "../part/PopupOption.svelte";

    let assigneeOptions = [
        {
            id: 1,
            name: '담당자가 없는 이슈'
        },
        {
            id: 2,
            name: 'jinbbangs2'
        },
        {
            id: 3,
            name: 'htmlH3AD'
        },
    ];

    let labelOptions = [
        {
            id: 1,
            color: '#3232FF',
            name: 'documentation'
        },
        {
            id: 2,
            color: '#FF3232',
            name: 'bug'
        }
    ];

    let milestoneOptions = [
        {
            id: 1,
            name: 'v1.0'
        },
        {
            id: 2,
            name: 'v2.0'
        }
    ];

    let writerOptions = [
        {
            id: 1,
            name: 'writer1'
        },
        {
            id: 2,
            name: 'writer2'
        }
    ];

    let showPopup = '';

    const togglePopup = (popup) => {
        if (showPopup === popup) {
            showPopup = '';
        }
        else {
            showPopup = popup;
        }
    };
</script>

<div class="filter-parent">
    <div class="filter-btn-container">
        <button type="button" on:click={() => togglePopup('assignees')}>
            담당자
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'assignees'}
            <OptionPopup headerText="담당자 필터" options={assigneeOptions} storeType="assignees" />
        {/if}
    </div>
    <div class="filter-btn-container">
        <button type="button" on:click={() => togglePopup('labels')}>
            레이블
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'labels'}
            <OptionPopup headerText="레이블 필터" options={labelOptions} storeType="labels" />
        {/if}
    </div>
    <div class="filter-btn-container">
        <button type="button" on:click={() => togglePopup('milestones')}>
            마일스톤
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'milestones'}
            <OptionPopup headerText="마일스톤 필터" options={milestoneOptions} storeType="milestones" />
        {/if}
    </div>
    <div class="filter-btn-container">
        <button type="button" on:click={() => togglePopup('writers')}>
            작성자
        </button>
        <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        {#if showPopup === 'writers'}
            <OptionPopup headerText="작성자 필터" options={writerOptions} storeType="writers" />
        {/if}
    </div>
</div>
