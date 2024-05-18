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


<style>
    .content-box {
        background-color: #FEFEFE;
        width: 960px;
        border-radius: 10px;       /* 모서리 둥글게 처리 (반지름 10px) */
        border: 1px solid #e0e0e0; /* 테두리 회색으로 설정, 두께는 1px */
        overflow: hidden;          /* 내용이 상자를 넘어갈 경우 숨기기 (내부 요소가 박스 밖으로 나가지 못하게 함) */
    }

    .content-box-header {
        background-color: #F7F7FC;
        padding: 20px;             /* 헤더 내부에 상하좌우 20px 여백 추가 */
        border-bottom: 1px solid #e0e0e0; /* 헤더와 본문 구분 하단 경계선 추가 */
    }

    .content-box-main {
        padding: 20px; /* 패딩 설정 */
    }

    .comment-container {
        width: 100%;
        max-width: 960px;
        justify-content: center;
        margin-top: 20px;
    }

    .comment-box {
        background-color: #EFF0F6;
        border-radius: 10px;
        padding: 10px;
        width: 100%;
    }

    .content-box-header > img {
        display: inline-block;
    }

    .comment-box textarea {
        width: 100%; /* 너비를 부모 요소에 맞춤 */
        height: 120px; /* 높이를 150px로 설정 */
        border: none; /* 테두리를 없앰 */
        padding: 15px; /* 내부 여백을 15px로 설정 */
        font-size: 16px; /* 글자 크기를 16px로 설정 */
        resize: none; /* 사용자가 텍스트 영역 크기를 조정할 수 없도록 함 */
        box-sizing: border-box; /* 패딩과 테두리를 포함한 전체 너비와 높이를 설정 */
        background-color: #EFF0F6; /* 배경색을 연한 회색으로 설정 */
        margin-bottom: 10px; /* 아래쪽 여백 추가 */
    }

    input:focus, textarea:focus {
        outline: none; /* 포커스 시 나타나는 외곽선을 없앰 */
    }
</style>