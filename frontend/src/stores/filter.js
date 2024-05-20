import { writable, get } from 'svelte/store';

const createOptionsStore = () => {
    const { subscribe, update } = writable({
        assignees: [],
        labels: [],
        milestones: [],
        writers: []
    });

    const updateOption = (category, option) => {
        update(store => {
            const index = store[category].indexOf(option);
            if (index === -1) {
                store[category].push(option); // 선택한 옵션을 리스트에 추가
            } else {
                store[category].splice(index, 1); // 선택한 옵션을 리스트에서 제거
            }
            return store;
        });
    };

    const logAllSelectedOptions = () => {
        subscribe(store => {
            console.log(store);
        });
    };

    logAllSelectedOptions();


    return {
        subscribe,
        toggleAssigneeOption: (option) => updateOption('assignees', option),
        toggleLabelOption: (option) => updateOption('labels', option),
        toggleMilestoneOption: (option) => updateOption('milestones', option),
        toggleWriterOption: (option) => updateOption('writers', option),
    };
};

export const optionsStore = createOptionsStore();