import {writable} from "svelte/store";
import {delApi, getApi, patchApi, postApi} from '../service/api.js'

const urlPrefix = '/api/v1'

function setLabels() {
    let initValues = {
        labels : [],
        addMode: '',
        editMode: '',
    }

    const {subscribe, update, set} = writable({...initValues})

    const fetchLabels = async () => {
        try {
            const options = {
                path: urlPrefix + '/labels',
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
                data: {...changes}
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