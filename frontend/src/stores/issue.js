import {get, writable} from "svelte/store";
import {getApi, postApi, putApi, delApi} from "../service/api.js";
import {router} from "tinro";
import {urlPrefix, MOCK_USER_ID, MOCK_USER_PWD} from "../utils/constants.js";

function setIssues() {
    let initValues = {
        memberId: MOCK_USER_ID,
        issueList: [],
    }

    const { subscribe, update, set } = writable({...initValues});

    const createIssue = async (form) => {
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
            router.goto(/issues/ + savedId.issueId);
        }
        catch (err) {
            console.log(err);
            alert("이슈 등록 중 에러가 발생했습니다. 다시 시도해 주세요!");
        }
    }

    return {
        subscribe,
        createIssue,
    }
}


export const issues = setIssues();
