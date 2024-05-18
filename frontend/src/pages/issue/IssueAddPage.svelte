<script>
    import {issues} from "../../stores/issue.js";
    import {router} from "tinro";

    let issueFormValues = {
        title: '',
        content: '',
    }

    const onCreateIssue = async () => {
        try {
            await issues.createIssue(issueFormValues.title, issueFormValues.content);
            onCancelCreateIssue();
        }
        catch (err) {
            alert(err);
        }
    }

    const onCancelCreateIssue = () => {
        issueFormValues.title = '';
        issueFormValues.content = '';
        router.goto("/");
    }

    let isSubmitLocked = true;
    $: {
        isSubmitLocked = issueFormValues.title.trim() === ''
            || issueFormValues.content.trim() === '';
    }

</script>


<div id="header-area">
    <strong id="title-text">새로운 이슈 작성</strong>
</div>
<div id="main-area">
    <div id="content-area">
        <div class="left-section">
            <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
            <div class="issue-header">
                <input type="text" placeholder="제목" class="title-input" bind:value={issueFormValues.title}>
            </div>
            <div class="content-input">
                <textarea placeholder="내용을 입력하세요" bind:value={issueFormValues.content}></textarea>
            </div>
            <div class="attachment">
                <span>📎 파일 첨부하기</span>
            </div>
        </div>

        <div class="action-buttons">
            <button class="cancel-button"
                    on:click={onCancelCreateIssue}>✕ 작성 취소</button>
            <button class="submit-button"
                    disabled={isSubmitLocked}
                    on:click={onCreateIssue}>완료</button>
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
    .profile-icon {
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }

    .title-input {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 8px;
        font-size: 16px;
    }

    .content-input textarea {
        width: 960px;
        height: 200px;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 8px;
        font-size: 16px;
        resize: none;
    }
</style>