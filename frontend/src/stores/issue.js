import {get, writable} from "svelte/store";
import {getApi, postApi, putApi, delApi} from "../service/api.js";
import {router} from "tinro";
import {MOCK_USER_ID, MOCK_USER_PWD} from "../utils/constants.js";

function setIssues() {
    let initValues = {
        memberId: MOCK_USER_ID,
        issueList: [],
    }

    const { subscribe, update, set } = writable({...initValues});

    const createIssue = async (title, content) => {
        try {
            const options = {
                path: "/api/v1/issues",
                data: {
                    memberId: MOCK_USER_ID,
                    title: title,
                    content: content,
                },
            }
            await postApi(options);
            router.goto("/");
        }
        catch (err) {
            console.log(err);
            alert("이슈 등록 중 에러가 발생했습니다. 다시 시도해 주세요!");
        }
    }

    return {
        createIssue,
    }
}


export const issues = setIssues();
