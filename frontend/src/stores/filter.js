import { get, writable } from "svelte/store";
import { getApi } from "../service/api.js";
import { urlPrefix } from "../utils/constants.js";
import { auth } from "./auth.js";
import { issues } from "./issue.js";

function setFilters() {
    let initValues = {
        members: [],
        labels: [],
        milestones: [],
        selectedAssignee: '',
        selectedLabels: [],
        selectedMilestone: '',
        selectedWriter: '',
        checkedStates: {
            assignee: {},
            labels: {},
            milestone: {},
            writer: {}
        },
        isOpenView: true,
        searchCondition: '',
        userInputCondition: '',
    }

    const {subscribe, update, set} = writable({...initValues});

    const fetchMembers = async () => {
        const options = {
            path: `${urlPrefix}/members`,
            access_token: get(auth).accessToken,
        }

        try {
            const getData = await getApi(options)

            const newData = {
                memberList: getData.members,
            }

            update(data => {
                data.members = [...newData.memberList]
                return data
            })
        } catch (error) {
            console.error('fetch member list error', error)
            throw error
        }
    }

    const fetchLabels = async () => {
        const options = {
            path: `${urlPrefix}/labels`,
            access_token: get(auth).accessToken,
        }

        try {
            const getData = await getApi(options)

            const newData = {
                labelList: getData.labels,
            }

            update(data => {
                data.labels = [...newData.labelList]
                return data
            })
        } catch (error) {
            console.error('fetch label list error', error)
            throw error
        }
    }

    const fetchMilestones = async () => {
        const options = {
            path: `${urlPrefix}/milestones`,
            access_token: get(auth).accessToken,
        }

        try {
            const getData = await getApi(options)

            const newData = {
                milestoneList: getData.milestones,
            }

            update(data => {
                data.milestones = [...newData.milestoneList]
                return data
            })
        } catch (error) {
            console.error('fetch milestone list error', error)
            throw error
        }
    }

    const singleSelectAssignee = async (selectedAssigneeId) => {
        update(data => {
            if (data.selectedAssignee !== selectedAssigneeId) {
                data.checkedStates.assignee[data.selectedAssignee] = false
                data.selectedAssignee = selectedAssigneeId
                data.checkedStates.assignee[selectedAssigneeId] = true
            }
            return data
        })
    }

    const deleteAssignee = async (selectedAssigneeId) => {
        update(data => {
            data.selectedAssignee = ''
            data.checkedStates.assignee[selectedAssigneeId] = false
            return data
        })
    }

    const multiSelectLabel = async (selectedLabel) => {
        update(data => {
            if (!data.selectedLabels.find(label => label.labelId === selectedLabel.labelId)) {
                data.selectedLabels.push(selectedLabel)
                data.checkedStates.labels[selectedLabel.labelId] = !data.checkedStates.labels[selectedLabel.labelId]
            }
            return data
        })
    }

    const deleteLabel = async (selectedLabel) => {
        update(data => {
            data.selectedLabels = data.selectedLabels.filter(label => label.labelId !== selectedLabel.labelId)
            data.checkedStates.labels[selectedLabel.labelId] = !data.checkedStates.labels[selectedLabel.labelId]
            return data
        })
    }

    const singleSelectMilestone = async (selectedMilestoneId) => {
        update(data => {
            if (data.selectedMilestone !== selectedMilestoneId) {
                data.checkedStates.milestone[data.selectedMilestone] = false
                data.selectedMilestone = selectedMilestoneId
                data.checkedStates.milestone[selectedMilestoneId] = true
            }
            return data
        })
    }

    const deleteMilestone = async (selectedMilestoneId) => {
        update(data => {
            data.selectedMilestone = ''
            data.checkedStates.milestone[selectedMilestoneId] = false
            return data
        })
    }

    const singleSelectWriter = async (selectedWriterId) => {
        update(data => {
            if (data.selectedWriter !== selectedWriterId) {
                data.checkedStates.writer[data.selectedWriter] = false
                data.selectedWriter = selectedWriterId
                data.checkedStates.writer[selectedWriterId] = true
            }
            return data
        })
    }

    const deleteWriter = async (selectedMilestoneId) => {
        update(data => {
            data.selectedWriter = ''
            data.checkedStates.writer[selectedMilestoneId] = false
            return data
        })
    }

    const searchIssueByFilterCondition = async () => {
        console.log('검색할 필터:', get(filters))

        let searchCond = []

        // 필터 조건 생성
        const openCond = get(filters).isOpenView ? 'is:open' : 'is:closed'
        searchCond.push(openCond)

        const selectedAssignee = get(filters).selectedAssignee.trim()
        if (selectedAssignee.length > 0) {
            if (selectedAssignee.includes(' ')) {
                searchCond.push(`assignee:"${selectedAssignee}"`);
            } else {
                searchCond.push(`assignee:${selectedAssignee}`);
            }
        }

        const selectedLabels = get(filters).selectedLabels
        if (selectedLabels.length > 0) {
            const labelConditions = selectedLabels.map(label => {
                if (label.labelId.includes(' ')) {
                    return `label:"${label.labelId}"`;
                } else {
                    return `label:${label.labelId}`;
                }
            }).join(' ')
            searchCond.push(`${labelConditions}`)
        }

        const selectedMilestone = get(filters).selectedMilestone.trim()
        if (selectedMilestone.length > 0) {
            if (selectedMilestone.includes(' ')) {
                searchCond.push(`milestone:"${selectedMilestone}"`);
            } else {
                searchCond.push(`milestone:${selectedMilestone}`);
            }
        }

        const selectedWriter = get(filters).selectedWriter.trim()
        if (selectedWriter.length > 0) {
            if (selectedWriter.includes(' ')) {
                searchCond.push(`"author:${selectedWriter}"`);
            } else {
                searchCond.push(`author:${selectedWriter}`);
            }
        }

        update(data => {
            data.searchCondition = searchCond.join(' ')
            return data
        })

        console.log('최종 조건:', searchCond)

        await issues.resetIssues();
        await issues.fetchIssues(searchCond)
    }

    const resetCheckedState = () => {
        update(data => {
            data.checkedStates = {
                assignee: {},
                labels: {},
                milestone: {},
                writer: {},
            }
            return data
        })
    }

    const resetSelectedItems = () => {
        update(data => {
            data.selectedAssignee = ''
            data.selectedLabels = []
            data.selectedMilestone = ''
            data.selectedWriter = ''
            return data
        })
    }

    const changeViewMode = () => {
        update(data => {
            data.isOpenView = !data.isOpenView
            return data
        })
    }

    const updateUserInput = (query) => {
        update(data => {
            data.userInputCondition = query
            return data
        })
    }

    const searchIssueByUserInput = async () => {
        const userQuery = get(filters).userInputCondition.trim();
        if (userQuery.length > 0) {
            await issues.resetIssues();
            await issues.fetchIssuesByUserInput(userQuery);
        }
    }

    const reloadSearchCondition = () => {
        let searchCond = []

        // 필터 조건 생성
        const openCond = get(filters).isOpenView ? 'is:open' : 'is:closed'
        searchCond.push(openCond)

        const selectedAssignee = get(filters).selectedAssignee.trim()
        if (selectedAssignee.length > 0) {
            if (selectedAssignee.includes(' ')) {
                searchCond.push(`assignee:"${selectedAssignee}"`);
            } else {
                searchCond.push(`assignee:${selectedAssignee}`);
            }
        }

        const selectedLabels = get(filters).selectedLabels
        if (selectedLabels.length > 0) {
            const labelConditions = selectedLabels.map(label => {
                if (label.labelId.includes(' ')) {
                    return `label:"${label.labelId}"`;
                } else {
                    return `label:${label.labelId}`;
                }
            }).join(' ')
            searchCond.push(`${labelConditions}`)
        }

        const selectedMilestone = get(filters).selectedMilestone.trim()
        if (selectedMilestone.length > 0) {
            if (selectedMilestone.includes(' ')) {
                searchCond.push(`milestone:"${selectedMilestone}"`);
            } else {
                searchCond.push(`milestone:${selectedMilestone}`);
            }
        }

        const selectedWriter = get(filters).selectedWriter.trim()
        if (selectedWriter.length > 0) {
            if (selectedWriter.includes(' ')) {
                searchCond.push(`"author:${selectedWriter}"`);
            } else {
                searchCond.push(`author:${selectedWriter}`);
            }
        }

        update(data => {
            data.searchCondition = searchCond.join(' ')
            return data
        })
    }

    return {
        subscribe,
        fetchMembers,
        fetchLabels,
        fetchMilestones,

        singleSelectAssignee,
        multiSelectLabel,
        singleSelectMilestone,
        singleSelectWriter,

        deleteAssignee,
        deleteLabel,
        deleteMilestone,
        deleteWriter,

        searchIssueByFilterCondition,
        searchIssueByUserInput,
        updateUserInput,
        reloadSearchCondition,

        resetCheckedState,
        resetSelectedItems,

        changeViewMode,
    }
}

export const filters = setFilters()
