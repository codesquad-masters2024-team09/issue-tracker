import {get, writable} from "svelte/store";
import {delApi, getApi, patchApi, postApi} from '../service/api.js'
import { auth } from './auth.js'

const urlPrefix = '/api/v1'

function setLabels() {
    let initValues = {
        labels : [],
        addMode: '',
        editMode: '',
        labelCnt: '',
    }

    const {subscribe, update, set} = writable({...initValues})

    const countLabels = async () => {
        try {
            const options = {
                path: `${urlPrefix}/labels/count`,
                access_token: get(auth).accessToken,
            }

            const response = await getApi(options)
            update(data => {
                data.labelCnt = response.countResult
                return data
            })
        } catch (error) {
            alert(error)
        }
    }

    const fetchLabels = async () => {
        try {
            const options = {
                path: urlPrefix + '/labels',
                access_token: get(auth).accessToken,
            }

            const response = await getApi(options)
            const labelsData = {
                labels: response.labels
            }

            update(data => {
                data.labels = labelsData.labels
                return data;
            })
        } catch (err) {
            alert(err)
        }
    }

    const resetLabels = () => {
        set({...initValues})
        return fetchLabels()
    }

    const registerLabel = async (label) => {
        try {
            const options = {
                path: urlPrefix + '/labels',
                data: {
                    labelId: label.labelId,
                    description: label.description,
                    colorCode: label.colorCode,
                    textColor: label.textColor,
                },
                access_token: get(auth).accessToken,
            }

            await postApi(options)
            return resetLabels()
        } catch (err) {
            alert(err)
        }
    }

    const toggleAddModeLabel = () => {
        update(data => {
            data.addMode = !data.addMode;
            return data;
        })
    }

    const openEditModeLabel = (id) => {
        update(data => {
            data.editMode = id;
            return data;
        })
    }

    const closeEditModeLabel = () => {
        update(data => {
            data.editMode = ''
            return data;
        })
    }

    const updateLabel = async (labelId, changes) => {
        try {
            const options = {
                path: urlPrefix + `/labels/${labelId}`,
                data: {...changes},
                access_token: get(auth).accessToken,
            }

            const editedLabel = await patchApi(options);

            update(data => {
                data.labels = data.labels.map(label => {
                    if (labelId === editedLabel.labelId) {
                        label = editedLabel;
                    }
                    return label;
                });
                return data;
            })

            labels.closeEditModeLabel()
            alert('수정이 완료되었습니다.')
        } catch (err) {
            alert('수정 중 오류가 발생했습니다. 다시 시도해 주세요.')
        }
    }

    const deleteLabel = async (id) => {
        try {
            const options = {
                path: urlPrefix + `/labels/${id}`,
                access_token: get(auth).accessToken,
            }

            await delApi(options)

            update(data => {
                data.labels = data.labels.filter(label => label.labelId !== id);
                return data;
            })
        } catch (err) {
            alert('삭제 중 오류가 발생했습니다. 다시 시도해 주세요.')
        }
    }

    return {
        subscribe,
        countLabels,
        fetchLabels,
        resetLabels,
        registerLabel,
        toggleAddModeLabel,
        openEditModeLabel,
        closeEditModeLabel,
        updateLabel,
        deleteLabel
    }
}

export const labels = setLabels()