import { writable, get } from 'svelte/store';

const createOptionsStore = () => {
    const { subscribe, update } = writable({
        isOpen: "open",
        assignees: "",
        labels: [],
        milestones: "",
        writers: ""
    });

    const updateOption = (category, option) => {
        update(store => {
            if (category === "isOpen") {
                store["isOpen"] = option;
                return store;
            }
            // 라벨의 경우 다중선택 가능한 구조
            if (category === "labels") {
                const index = store[category].indexOf(option);
                if (index === -1) {
                    store[category].push(option);
                } else {
                    store[category].splice(index, 1);
                }
            }
            // 라벨 외 옵션은 하나만 선택할 수 있도록 하는 구조
            else {
                if (store[category] === option) {
                    store[category] = "";
                } else {
                    store[category] = option;
                }
            }
            return store;
        });
    };

    const generateQueryString = (state) => {
        const queries = [];

        const addQuery = (key, value, noValue) => {
            if (value) {
                queries.push(value === noValue ? `no:${key}` : `${key}:${value.includes(" ") ? `"${value}"` : value}`);
            }
        };

        queries.push(`is:${state.isOpen}`);

        addQuery("assignee", state.assignees, "담당자가 없는 이슈");
        addQuery("milestone", state.milestones, "마일스톤 없는 이슈");
        addQuery("writer", state.writers);

        state.labels.forEach(label => {
            queries.push(`label:${label.includes(" ") ? `"${label}"` : label}`);
        });

        return queries.join(" ");
    }

    const logAllSelectedOptions = () => {
        subscribe(store => {
            console.log(store);
        });
    };

    logAllSelectedOptions();


    return {
        subscribe,
        toggleIsOpenOption: (option) => updateOption('isOpen', option),
        toggleAssigneeOption: (option) => updateOption('assignees', option),
        toggleLabelOption: (option) => updateOption('labels', option),
        toggleMilestoneOption: (option) => updateOption('milestones', option),
        toggleWriterOption: (option) => updateOption('writers', option),
        generateQueryString
    };
};

export const optionsStore = createOptionsStore();