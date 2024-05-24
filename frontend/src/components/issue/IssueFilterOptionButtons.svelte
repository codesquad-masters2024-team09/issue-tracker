<script>
    import OptionPopup from "../../components/part/OptionPopup.svelte";

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
            color: 'blue',
            name: 'documentation'
        },
        {
            id: 2,
            color: 'red',
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


<div class="issue-filters">
    <div class="filter-button-container">
        <button class="filter-button" on:click={() => togglePopup('assignees')}>
            담당자 <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        </button>
        {#if showPopup === 'assignees'}
            <OptionPopup headerText="담당자 필터" options={assigneeOptions} storeType="assignees" />
        {/if}
    </div>
    <div class="filter-button-container">
        <button class="filter-button" on:click={() => togglePopup('labels')}>
            레이블 <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        </button>
        {#if showPopup === 'labels'}
            <OptionPopup headerText="레이블 필터" options={labelOptions} storeType="labels" />
        {/if}
    </div>
    <div class="filter-button-container">
        <button class="filter-button" on:click={() => togglePopup('milestones')}>
            마일스톤 <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        </button>
        {#if showPopup === 'milestones'}
            <OptionPopup headerText="마일스톤 필터" options={milestoneOptions} storeType="milestones" />
        {/if}
    </div>
    <div class="filter-button-container">
        <button class="filter-button" on:click={() => togglePopup('writers')}>
            작성자 <img src="/assets/toggle_btn.svg" alt="toggle_btn">
        </button>
        {#if showPopup === 'writers'}
            <OptionPopup headerText="작성자 필터" options={writerOptions} storeType="writers" />
        {/if}
    </div>
</div>


<style>
    .filter-button-container {
        position: relative;
    }

    .filter-button {
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 8px 12px;
        margin-right: 10px;
        cursor: pointer;
        font-size: 16px;
    }

    .filter-button:hover {
        background-color: #e0e0e0;
    }

    .issue-filters {
        display: flex;
        gap: 10px;
    }
</style>
