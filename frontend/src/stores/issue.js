import {get, writable} from "svelte/store";
import {delApi, getApi, patchApi, postApi} from "../service/api.js";
import {router} from "tinro";
import {urlPrefix} from "../utils/constants.js";
import {auth} from "./auth.js";

function setIssues() {
    let initValues = {
        memberId: get(auth).memberId,
        issueList: [],
        editTitlePopup: '',
        editContentPopup: '',
        editModeTitle: '', // 게시글 중 수정모드로 전환 시 게시글의 식별자 저장하는 필드
        editModeContent: '', // 게시글 중 수정모드로 전환 시 게시글의 식별자 저장하는 필드
    }

    const { subscribe, update, set } = writable({...initValues});

    const fetchIssues = async () => { // 이슈 목록을 서버로부터 받아오는 역할

        loadingIssue.turnOnLoading() // 로딩 효과

        try {
            const options = {
                path: `${urlPrefix}/issues`,
                access_token: get(auth).accessToken,
            }

            const getDatas = await getApi(options);

            const newData = {
                issueList: getDatas.issues,
            }

            update(datas => {

                // TODO: paging 처리 시 첫 페이지냐 아니냐에 따라 분기 처리 필요
                 // 현재까지 받은 데이터에 새로 받은 데이터를 뒤에 더하기
                datas.issueList = [...newData.issueList, ...datas.issueList]
                console.log('fetch issue list: ', datas.issueList)

                return datas
            })

            loadingIssue.turnOffLoading()
        }
        catch(error) {
            loadingIssue.turnOffLoading()
            throw error;
        }
    }

    const fetchIssueDetail = async (issueID) => { // 이슈 단건 조회
        try {
            const options = {
                path: `${urlPrefix}/issues/${issueID}`,
                access_token: get(auth).accessToken,
            }
            const responseData = await getApi(options);
            console.log('단건 조회: ', responseData)

            return responseData;
        }
        catch (err) {
            console.error(err);
            alert("이슈 단건 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    const createIssue = async (form) => { // 이슈 단건을 생성하는 역할
        try {
            const options = {
                path: urlPrefix + "/issues",
                data: {
                    memberId: get(auth).memberId,
                    title: form.title,
                    content: form.content,
                    labels: form.labels,
                    assignees: form.assignees,
                    milestoneId: form.milestone
                },
                access_token: get(auth).accessToken
            }

            const savedId = await postApi(options);
            router.goto(`/issues/${savedId.issueId}`);
        }
        catch (err) {
            console.log(err);
            alert("이슈 등록 중 에러가 발생했습니다. 다시 시도해 주세요!");
        }
    }

    const updateIssueState = async (issueId, state) => {
        try {
            const options = {
                path: `${urlPrefix}/issues/status?issueId=${issueId}&isOpen=${state}`,
                access_token: get(auth).accessToken,
            }

            await patchApi(options)
        } catch(err) {
            throw err;
        }
    }

    const updateIssue = async (issueId, form) => {

        try {

            const updateData = {
                title: form.title !== null ? form.title : null,
                content: form.content !== null ? form.content : null
            }

            const options = {
                path: `${urlPrefix}/issues/${issueId}`,
                data: updateData,
                access_token: get(auth).accessToken,
            }

            await patchApi(options)

            if (form.title) {
                closeEditModeIssueTitle()
            }

            if (form.content) {
                closeEditModeIssueContent()
            }

            return true
        }
        catch(error) {
            alert('수정 중 오류가 발생했습니다. 다시 시도해 주세요.')
            throw error
        }
    }

    const deleteIssue = async (issueId) => {
        try {
            const options = {
                path: `${urlPrefix}/issues/${issueId}`,
                access_token: get(auth).accessToken,
            }

            await delApi(options)

            update(datas => {
                datas.issueList = datas.issueList.filter(issue => issue.issueId !== issueId)

                return datas
            })
        }
        catch(error) {
            alert('삭제 중 오류가 발생했습니다.')
            throw error
        }
    }

    const resetIssues = () => { // 이슈 목록을 초기화해주는 역할
        set({...initValues})
        issuesPageLock.set(false)
    }

    const openEditModeIssueTitle = (issueId) => {
        update(datas => {
            datas.editModeTitle = issueId

            return datas
        })
    }

    const openEditModeIssueContent = (issueId) => {
        update(datas => {
            datas.editModeContent = issueId

            return datas
        })
    }

    const closeEditModeIssueTitle = () => {
        update(datas => {
            datas.editModeTitle = ''

            return datas
        })
    }

    const closeEditModeIssueContent = () => {
        update(datas => {
            datas.editModeContent = ''

            return datas
        })
    }

    return {
        subscribe,
        fetchIssues,
        fetchIssueDetail,
        createIssue,
        updateIssue,
        deleteIssue,
        resetIssues,
        updateIssueState,
        openEditModeIssueTitle,
        openEditModeIssueContent,
        closeEditModeIssueTitle,
        closeEditModeIssueContent,
    }
}

function setLoadingIssue() {
    const { subscribe, set } = writable(false)

    const turnOnLoading = () => {
        set(true)
        issuesPageLock.set(true)
    }

    const turnOffLoading = () => {
        set(false)
        issuesPageLock.set(false)
    }

    return {
        subscribe,
        turnOnLoading,
        turnOffLoading,
    }
}


export const issues = setIssues()
export const issuesPageLock = writable(false)
export const loadingIssue = setLoadingIssue()
