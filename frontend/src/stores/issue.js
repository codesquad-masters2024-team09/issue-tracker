import { get, writable, derived } from "svelte/store";
import { getApi, postApi, putApi, delApi, patchApi } from "../service/api.js";
import { router } from "tinro";
import { urlPrefix, MOCK_USER_ID, MOCK_USER_PWD } from "../utils/constants.js";
import { optionsStore } from "./filter.js";

function setIssues() {
    let initValues = {
        memberId: MOCK_USER_ID,
        issueList: [],
        editTitlePopup: '',
        editContentPopup: '',
        editModeTitle: '', // 게시글 중 수정모드로 전환 시 게시글의 식별자 저장하는 필드
        editModeContent: '', // 게시글 중 수정모드로 전환 시 게시글의 식별자 저장하는 필드
    }

    const { subscribe, update, set } = writable({...initValues});

    const fetchIssues = async (queryString="") => { // 이슈 목록을 서버로부터 받아오는 역할

        loadingIssue.turnOnLoading() // 로딩 효과

        try {
            const options = {
                path: `${urlPrefix}/issues?q=`+queryString
                // TODO: 기본 쿼리
            }
            console.log("URL: " + options.path);
            const getDatas = await getApi(options);

            const newData = {
                issueList: getDatas.issues,
            }

            update(datas => {

                // TODO: paging 처리 시 첫 페이지냐 아니냐에 따라 분기 처리 필요
                const newIssues = [...newData.issueList, ...datas.issueList] // 현재까지 받은 데이터에 새로 받은 데이터를 뒤에 더하기
                datas.issueList = newIssues
                console.log('fetch issue list: ', datas.issueList)

                return datas
            })

            loadingIssue.turnOffLoading()
        }
        catch(error) {
            console.log('fetch issue list error:', error)
            loadingIssue.turnOffLoading()
            throw error
        }
    }

    const fetchIssueDetail = async (issueID) => { // 이슈 단건 조회
        try {
            const options = {
                path: `${urlPrefix}/issues/${issueID}`,
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
                    memberId: form.memberId,
                    title: form.title,
                    content: form.content,
                    labels: form.labels,
                    milestone: form.milestone,
                },
            }

            const savedId = await postApi(options);
            router.goto(`/issues/${savedId.issueId}`);
        }
        catch (err) {
            console.log(err);
            alert("이슈 등록 중 에러가 발생했습니다. 다시 시도해 주세요!");
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
            }

            await delApi(options)

            update(datas => {
                const newIssueList = datas.issueList.filter(issue => issue.issueId !== issueId)
                datas.issueList = newIssueList

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

    optionsStore.subscribe(state => {
        const queryString = optionsStore.generateQueryString(state);
        fetchIssues(queryString)
            .then(res => console.log(res))
            .catch(err => alert("이슈 로딩 중 에러가 발생했습니다! 다시 시도해 주세요"));
    })

    return {
        subscribe,
        fetchIssues,
        fetchIssueDetail,
        createIssue,
        updateIssue,
        deleteIssue,
        resetIssues,
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
