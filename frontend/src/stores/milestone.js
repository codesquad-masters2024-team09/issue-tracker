import {get, writable} from "svelte/store";
import {postApi, getApi, patchApi, delApi, putApi} from "../service/api.js";
import {auth} from "./auth.js";
import {urlPrefix} from "../utils/constants.js";

function setMilestones() {
    let initValues = {
        milestones: [],
        addMode: '',
        editMode: '',
    }

    const {subscribe, update, set} = writable({...initValues})

    const fetchMilestones = async () => {
        try {
            const options = {
                path: urlPrefix + '/milestones',
                access_token: get(auth).accessToken,
            }

            const response = await getApi(options)
            const milestonesData = {
                milestones: response.milestones
            }

            update(data => {
                data.milestones = milestonesData.milestones
                return data
            })
        } catch (err) {
            alert(err)
        }
    }

    const resetMilestones = () => {
        set({...initValues})
        return fetchMilestones()
    }

    const registerMilestone = async (milestone) => {
        try {
            const options = {
                path: urlPrefix + '/milestones',
                data: {
                    id: milestone.id,
                    dueDate: milestone.dueDate,
                    description: milestone.description,
                },
                access_token: get(auth).accessToken,
            }

            await postApi(options)
            return resetMilestones()
        } catch(err) {
            alert(err)
        }
    }

    const toggleAddModeMilestone = () => {
        update(data => {
            data.addMode = !data.addMode
            return data;
        })
    }

    const openEditModeMilestone = (id) => {
        update(data => {
            data.editMode = id;
            return data;
        })
    }

    const closeEditModeMilestone = () => {
        update(data => {
            data.editMode = ''
            return data;
        })
    }

    const patchMilestone = async (milestoneId, isOpen) => {
        try {
            const options = {
                path: urlPrefix + `/milestones/status?milestoneId=${milestoneId}&isOpen=${isOpen}`,
            }

            await patchApi(options);

            update(data => {
                data.milestones = data.milestones.map(milestone => {
                    if(milestone.id === milestoneId) {
                        milestone.open = isOpen
                    }
                    return milestone;
                });
                return data;
            })

            alert('마일스톤을 닫았습니다.')
        } catch (err) {
            alert('마일스톤을 닫는 중 오류가 발생했습니다. 다시 시도해 주세요.')
        }
    }

    const updateMilestone = async (milestoneId, changes) => {
        try {
            const options = {
                path: urlPrefix + `/milestones/${milestoneId}`,
                data: {...changes},
                access_token: get(auth).accessToken,
            }

            const editedMilestone = await putApi(options);

            update(data => {
                data.milestones = data.milestones.map(milestone => {
                    if(milestoneId === editedMilestone.id) {
                        milestone = editedMilestone
                    }
                    return milestone;
                });
                return data;
            })

            milestones.closeEditModeMilestone()
            alert('수정이 완료되었습니다.')
        } catch (err) {
            alert('수정 중 오류가 발생했습니다. 다시 시도해 주세요.')
        }
    }

    const deleteMilestone = async (id) => {
        try {
            const options = {
                path: urlPrefix + `/milestones/${id}`,
                access_token: get(auth).accessToken,
            }

            await delApi(options)

            update(data => {
                data.milestones = data.milestones.filter(milestone => milestone.id !== id);
                return data;
            })
        } catch (err) {
            alert('삭제 중 오류가 발생했습니다. 다시 시도해 주세요.')
        }
    }

    return {
        subscribe,
        fetchMilestones,
        resetMilestones,
        registerMilestone,
        toggleAddModeMilestone,
        openEditModeMilestone,
        closeEditModeMilestone,
        updateMilestone,
        deleteMilestone,
        patchMilestone,
    }
}

export const milestones = setMilestones()
