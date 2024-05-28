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
        // 라벨이 아닌 경우 단일 선택만 가능, 라벨인 경우 다중 선택 가능
        const isLabel = storeType === "labels";
        if (!isLabel) {
            options.forEach(opt => {
                if (opt.name !== option) {
                    const optionItem = document.querySelector(`.popup-filter-item[data-name="${opt.name}"]`);
                    if(optionItem) {
                        const optionItemCheckbox = optionItem.querySelector(".popup-filter-checkbox");
                        if (optionItemCheckbox) {
                            optionItemCheckbox.checked = false;
                        }
                    }
                }
            })
        }
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
        const optionItem = event.target.closest(".popup-filter-item");
        if(optionItem){
            const optionItemCheckbox = optionItem.querySelector(".popup-filter-checkbox");
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
            const optionItem = document.querySelector(`.popup-filter-item[data-name="${option.name}"]`);
            if (optionItem) {
                const optionItemCheckbox = optionItem.querySelector(".popup-filter-checkbox");
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


<div class="popup-filter-container">
    <div class="popup-filter-header">{headerText}</div>
    {#each options as option}
    <div class="popup-filter-item" data-name={option.name} on:click={(event) => selectOptionByItemClick(event, option.name)}>
        <div class="popup-filter-inner-container">
            <div>
                {#if option.color}
                <div class="popup-filter-label-circle" style="background-color: {option.color};"></div>
                {/if}
                <span class="popup-filter-label">{option.name}</span>
            </div>
            <div>
                <input type="checkbox" class="popup-filter-checkbox" on:change={() => updateFilterOption(option.name)}>
            </div>
        </div>
    </div>
    {/each}
</div>
