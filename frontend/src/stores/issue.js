import { get, writable, derived } from "svelte/store";
import { getApi, postApi, putApi, delApi } from "../service/api.js";
import { router } from "tinro";
import { urlPrefix, MOCK_USER_ID, MOCK_USER_PWD } from "../utils/constants.js";

function setIssues() {
    let initValues = {
        memberId: MOCK_USER_ID,
        issueList: [],
        editMode: '', // 게시글 중 수정모드로 전환 시 게시글의 식별자 저장하는 필드
    }

    const { subscribe, update, set } = writable({...initValues});

    const fetchIssues = async () => { // 이슈 목록을 서버로부터 받아오는 역할

        loadingIssue.turnOnLoading() // 로딩 효과

        try {
            const options = {
                path: `${urlPrefix}/issues?q=` // TODO: 기본 쿼리
            }

            const getDatas = await getApi(options);

            const newData = {
                issueList: getDatas.issues,
            }

            update(datas => {

                // TODO: paging 처리 시 첫 페이지냐 아니냐에 따라 분기 처리 필요
                const newIssues = [...datas.issueList, ...newData.issueList] // 현재까지 받은 데이터에 새로 받은 데이터를 뒤에 더하기
                datas.issueList = newIssues
                console.log('fetch issue list: ', datas.issueList)

                return datas
            })

            subscribe(state => {
                console.log('Current state:', state);
            });

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

    const createIssues = async (form) => { // 이슈 단건을 생성하는 역할
        try {
            const options = {
                path: urlPrefix + "/issues",
                data: {
                    memberId: form.memberId,
                    title: form.title,
                    content: form.content,
                    lables: form.lables,
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
        }
    }

    const resetIssues = () => { // 이슈 목록을 초기화해주는 역할
        set({...initValues})
        issuesPageLock.set(false)
    }

    const openEditModeIssue = (issueId) => {
        update(datas => {
            datas.editMode = issueId

            return datas
        })
    }

    const closeEditModeIssue = () => {
        update(datas => {
            datas.editMode = ''

            return datas
        })
    }

    return {
        subscribe,
        fetchIssues,
        fetchIssueDetail,
        createIssues,
        deleteIssue,
        resetIssues,
        openEditModeIssue,
        closeEditModeIssue,
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
