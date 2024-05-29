import {get, writable} from "svelte/store";
import { delApi, getApi, postApi } from "../service/api.js";
import { urlPrefix } from "../utils/constants.js";
import { auth } from "./auth.js";

function setTags() {
    let initValues = {
        members: [],
        labels: [],
        milestones: [],
        selectedMembers: [],
        selectedLabels: [],
        selectedMilestone: '',
        checkedStates: {
            assignees: {},
            labels: {},
            milestone: {}
        },
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

    const addLabelOnIssue = async (issueId, labelId) => {
        try {
            console.log('add issueId label ===> ', issueId)
            const data = {
                labelId: labelId
            }

            const options = {
                path: `${urlPrefix}/issues/${issueId}/label`,
                data: data,
                access_token: get(auth).accessToken,
            }

            console.log(`${issueId}에 레이블 등록:${labelId}`)

            await postApi(options);
            return true
        } catch (err) {
            alert('레이블을 할당하는 중 오류가 발생했습니다. 다시 시도해 주세요.')
            throw err
        }
    }

    const deleteLabelOnIssue = async (issueId, labelId) => {
        try {
            console.log('del issueId label ===> ', issueId)
            const deleteData = {
                labelId: labelId
            }

            const options = {
                path: `${urlPrefix}/issues/${issueId}/label`,
                data: deleteData,
                access_token: get(auth).accessToken,
            }

            await delApi(options)
            return true
        } catch (err) {
            alert('레이블을 할당 해제하는데 문제가 있습니다. 다시 시도해주세요.')
            throw err
        }
    }

    const selectLabel = async (selectedLabel) => {
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

    const assignMilestoneOnIssue = async (issueId, milestoneId) => {
        try {
            console.log('add issueId milestoneId ===> ', issueId)
            const data = {
                milestoneId: milestoneId
            }

            const options = {
                path: `${urlPrefix}/issues/${issueId}/milestone`,
                data: data,
                access_token: get(auth).accessToken,
            }

            console.log(`${issueId}에 milestone 등록:${milestoneId}`)

            await postApi(options);
            return true
        } catch (err) {
            alert('마일스톤을 할당하는 중 오류가 발생했습니다. 다시 시도해 주세요.')
            throw err
        }
    }

    const deleteMilestoneOnIssue = async (issueId) => {
        try {
            console.log('del issueId label ===> ', issueId)

            const options = {
                path: `${urlPrefix}/issues/${issueId}/milestone`,
                access_token: get(auth).accessToken,
            }

            await delApi(options)
            return true
        } catch (err) {
            alert('마을스톤을 할당 해제하는데 문제가 있습니다. 다시 시도해주세요.')
            throw err
        }
    }

    const selectMilestone = async (selectedMilestoneId) => {
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

    const initAssigneeCheckedState = (options, assignedMembers) => {
        update(data => {
            options.forEach(assignee => {
                data.checkedStates.assignees[`${assignee.memberId}`] = !!assignedMembers.includes(assignee.memberId);
            })
            data.selectedMembers = assignedMembers
            return data
        })
    }

    const initLabelCheckedState = (options, assignedLabels) => {
        const assignedLabelIds = assignedLabels.map(label => label.labelId)
        update(data => {
            options.forEach(label => {
                data.checkedStates.labels[`${label.labelId}`] = !!assignedLabelIds.includes(label.labelId);
            })
            data.selectedLabels = assignedLabels
            return data
        })
    }

    const initMilestoneCheckedState = (options, assignedMilestoneId) => {
        update(data => {
            options.forEach(milestone => {
                return data.checkedStates.milestone[`${milestone.id}`] = milestone.id === assignedMilestoneId;
            })
            data.selectedMilestone = assignedMilestoneId
            return data
        })
    }

    const resetCheckedState = () => {
        update(data => {
            data.checkedStates = {
                assignees: {},
                labels: {},
                milestone: {}
            }
            return data
        })
    }

    const resetSelectedItems = () => {
        update(data => {
            data.selectedMembers = []
            data.selectedLabels = []
            data.selectedMilestone = ''
            return data
        })
    }

    return {
        subscribe,
        fetchMembers,
        fetchLabels,
        fetchMilestones,
        selectLabel,
        addLabelOnIssue,
        deleteLabel,
        deleteLabelOnIssue,
        selectMilestone,
        assignMilestoneOnIssue,
        deleteMilestone,
        deleteMilestoneOnIssue,
        initAssigneeCheckedState,
        initLabelCheckedState,
        initMilestoneCheckedState,
        resetCheckedState,
        resetSelectedItems
    }
}

export const tags = setTags()
