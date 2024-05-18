<script>
    import {onMount} from "svelte";
    import {getApi, postApi} from "../../service/api.js";
    import {Route, meta} from "tinro";
    import {MOCK_USER_ID} from "../../utils/constants.js";

    const route = meta();
    const issueId = Number(route.params.issueId);

    let issueData = {
        memberId: MOCK_USER_ID,
        issueId: issueId,
        createdAt: '',
        title: '',
        content: '',
        comments: [],
    }

    let commentInput = '';
    let isSubmitLocked = true;
    $:isSubmitLocked = commentInput.trim() === '';

    const fetchIssue = (issueID) => {
        try {
            const options = {
                path: `/api/v1/issues/${issueID}`,
            }
            return getApi(options);
        }
        catch (err) {
            alert("오류가 발생했습니다! 다시 시도해주세요!");
        }
    }

    const onCreateComment = () => {
        try {
            const options = {
                path: '/api/v1/comments',
                data: {
                    memberId: MOCK_USER_ID,
                    issueId: issueId,
                    content: commentInput,
                }
            }
            postApi(options);
            issueData.comments = [...issueData.comments, options.data];
            commentInput = '';
        }
        catch (err) {
            console.log(err);
            alert("코멘트 저장 중 오류가 발생했습니다! 다시 시도해주세요!")
        }
    }

    onMount(async () => {
         issueData = await fetchIssue(issueId);
         console.log(issueData.comments);
    });


</script>

<div id="header-area">
    <p id="title-text">{issueData.title}</p>
    <p class="detail-info">이 이슈는 {issueData.createdAt}에 {issueData.memberId}에 의해 열렸습니다</p>
</div>
<div id="main-area">
    <div id="content-area">
        <div class="left-section">
            <div class="content-box">
                <div class="content-box-header">
                    <p>{issueData.memberId}</p>
                </div>
                <div class="content-box-main">
                    <p>{issueData.content}</p>
                </div>
            </div>
            {#each issueData.comments as comment}
                <div class="content-box">
                    <div class="content-box-header">
                        <p>{comment.memberId}</p>
                    </div>
                    <div class="content-box-main">
                        <p>{comment.content}</p>
                    </div>
                </div>
            {/each}

            <div class="comment-container">
                <div class="comment-box">
                    <textarea placeholder="코멘트를 입력하세요"
                              bind:value={commentInput}></textarea>
                    <div class="attachment">
                        <span class="detail-info">📎 파일 첨부하기</span>
                    </div>
                </div>
                <div class="action-buttons">
                    <button class="submit-button"
                            disabled={isSubmitLocked} on:click={onCreateComment}>+ 코멘트 작성</button>
                </div>
            </div>
        </div>
    </div>

    <div id="additional-info-area">
        <div class="option-container">
            <div class="option-item">
                <span>담당자</span>
                <button class="add-button">+</button>
            </div>
            <div class="option-item">
                <span>레이블</span>
                <button class="add-button">+</button>
            </div>
            <div class="option-item">
                <span>마일스톤</span>
                <button class="add-button">+</button>
            </div>
        </div>
    </div>
</div>