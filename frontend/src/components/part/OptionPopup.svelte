<script>
    import { optionsStore } from '../../stores/filter.js';
    import { onMount } from "svelte";
    import { get } from "svelte/store";

    export let headerText = '레이블 필터';
    export let options = [];

    export let storeType;

    const optionUpdateFunction = {
        assignees: optionsStore.toggleAssigneeOption,
        labels: optionsStore.toggleLabelOption,
        milestones: optionsStore.toggleMilestoneOption,
        writers: optionsStore.toggleWriterOption
    };

    // 선택한 필터 조건을 상태에 업데이트
    const updateFilterOption = (option) => {
        const updateFunction = optionUpdateFunction[storeType];
        updateFunction(option);
    };

    // 체크 박스 이외의 영역을 눌러도 클릭되도록 하는 함수
    const selectOptionByItemClick = (event, option) => {
        // 이벤트 버블링 방지
        if (event.target.tagName.toLowerCase() === 'input') {
            return;
        }
        event.stopPropagation();
        updateFilterOption(option);
        const optionItem = event.target.closest(".popup-item");
        if(optionItem){
            const optionItemCheckbox = optionItem.querySelector(".popup-checkbox");
            if(optionItemCheckbox){
                optionItemCheckbox.checked = !optionItemCheckbox.checked;
            }
        }
    };

    // 팝업이 열릴 때마다 체크박스 상태를 설정하는 함수
    const initializeCheckboxState = () => {
        const storeData = get(optionsStore);
        const currentOptions = storeData[storeType];
        options.forEach(option => {
            const optionItem = document.querySelector(`.popup-item[data-name="${option.name}"]`);
            if (optionItem) {
                const optionItemCheckbox = optionItem.querySelector(".popup-checkbox");
                if (optionItemCheckbox) {
                    optionItemCheckbox.checked = currentOptions.includes(option.name);
                }
            }
        });
    };

    onMount(() => {
        initializeCheckboxState();
    });
</script>


<div class="popup-container">
    <div class="popup-header">{headerText}</div>
    {#each options as option}
        <div class="popup-item"
             data-name={option.name}
             on:click={(event) => selectOptionByItemClick(event, option.name)}>
            {#if option.color}
                <span class="popup-color"
                      style="background-color: {option.color};">
                </span>
            {/if}
            <span class="popup-label">{option.name}</span>
            <input type="checkbox"
                   class="popup-checkbox"
                   on:change={() => updateFilterOption(option.name)}>
        </div>
    {/each}
</div>


<style>
    .popup-container {
        width: 240px;
        border: 1px solid #d0d4da;
        border-radius: 10px;
        background-color: #fff;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        position: absolute;
        z-index: 1;
        top: 125%;
        right: 0;
    }

    .popup-header {
        font-size: 13px;
        color: #6E7191;
        padding: 8px 16px 8px 16px;
        border-bottom: 1px solid #d0d4da;
        background-color: #F7F7FC;
        border-top-left-radius: 10px;
        border-top-right-radius: 10px;
    }

    .popup-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 8px 16px 8px 16px;
        border-bottom: 1px solid #d0d4da;
        color: #4E4B66;
        font-size: 16px;
    }

    .popup-item:last-child {
        border-bottom: none;
    }

    .popup-label {
        flex-grow: 1;
        margin-left: 10px;
    }

    .popup-color {
        width: 16px;
        height: 16px;
        border-radius: 50%;
        margin-right: 10px;
    }

    .popup-checkbox {
        cursor: pointer;
    }
</style>
