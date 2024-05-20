<script>
    import {onMount} from "svelte";
    import {handleCheckboxes} from "../../service/buttonSelection.js";
    import {router} from "tinro";
    import IssueFilterOptionButtons from "../../components/issue/IssueFilterOptionButtons.svelte";

    const MOCK_ISSUE_LIST_DATA = {
        openedIssueCount: 4,
        closedIssueCount: 5,
        labelCount: 1,
        milestoneCount: 2,
        issues: [
            {
                issueId: 1,
                memberId: "testUser1",
                title: "예시 이슈 1번",
                isOpen: true,
                labels: [
                    {
                        labelId: "fix",
                        colorCode: "#FF3B30",
                        textColor: "#FEFEFE",
                    },
                    {
                        labelId: "feature",
                        colorCode: "#0025E6",
                        textColor: "#FEFEFE"
                    }
                    ],
                milestoneName: "마일스톤 1번",
                dueDate: "3초 전",
                createdAt: "3초",
            },
            {
                issueId: 2,
                memberId: "testUser2",
                title: "예시 이슈 2번",
                isOpen: true,
                labels: [
                    {
                        labelId: "bug",
                        colorCode: "#FF3B30",
                        textColor: "#FEFEFE",
                    },
                    {
                        labelId: "feature",
                        colorCode: "#0025E6",
                        textColor: "#FEFEFE",
                    }
                ],
                milestoneName: "마일스톤 2번",
                createdAt: "1일",
            },
        ]
    }

    onMount(() => {
        const parentCheckbox = document.querySelector(".parent-checkbox");
        const childCheckboxes = document.querySelectorAll(".child-checkbox");
        if (parentCheckbox && childCheckboxes.length > 0) {
            handleCheckboxes(parentCheckbox, childCheckboxes);
        }
    })
</script>


<div class="header">
    <div class="filter-container">
        <div class="search-bar-container">
            <button class="filter-button">필터</button>
            <input type="text" class="search-bar" placeholder="🔍 is:issue is:open">
        </div>
    </div>
    <div class="actions-container">
        <div class="label-milestone-action-buttons">
            <button class="action-button">
                <img src="/assets/label_icon.svg" alt="label icon" />
                <p>레이블 ({MOCK_ISSUE_LIST_DATA.labelCount})</p>
            </button>
            <button class="action-button">
                <img src="/assets/milestone_icon.svg" alt="milestone icon" />
                <p>마일스톤 ({MOCK_ISSUE_LIST_DATA.milestoneCount})</p>
            </button>
        </div>
        <button class="create-button" on:click={() => {router.goto("/issues/add")}}>+ 이슈 작성</button>
    </div>
</div>
<div class="issue-list">
    <div class="issue-list-header">
        <div class="checkbox-container">
            <input type="checkbox" class="parent-checkbox">
        </div>
        <div class="issue-status">
            <div class="open-issues">
                <img src="/assets/issue_icon_black.svg" alt="issue icon black">
                열린 이슈({MOCK_ISSUE_LIST_DATA.openedIssueCount})
            </div>
            <div class="closed-issues">
                <img src="/assets/issue_icon_closed.svg" alt="issue closed icon">
                닫힌 이슈({MOCK_ISSUE_LIST_DATA.closedIssueCount})
            </div>
        </div>
        <IssueFilterOptionButtons />
    </div>
    {#each MOCK_ISSUE_LIST_DATA.issues as issue}
    <div class="issue-item">
        <input type="checkbox" class="child-checkbox"/>
        <div class="issue-info" on:click={() => router.goto(`/issues/${issue.issueId}`)}>
            <div class="issue-header">
                <img src="/assets/issue_icon_blue.svg" alt="issue open icon">
                <p class="issue-title">{issue.title}</p>
                {#each issue.labels as label}
                <p class="issue-label"
                      style="
                      color: {label.textColor};
                      background-color: {label.colorCode}">
                    {label.labelId}
                </p>
                {/each}
            </div>
            <div class="issue-details">
                <p>#{issue.issueId}</p>
                <p>{issue.createdAt} 전, {issue.memberId}에 의해 작성되었습니다</p>
                <p>
                    <img src="/assets/milestone_icon.svg" alt="milestone icon" />
                    {issue.milestoneName}
                </p>
            </div>
        </div>
        <div class="issue-avatar">
            <img src="/assets/profile_icon_duck.svg" alt="issue profile duck" />
        </div>
    </div>
    {/each}
    <div class="issue-item">
        <input type="checkbox" class="child-checkbox"/>
        <div class="issue-info">
            <div class="issue-header">
                <img src="/assets/issue_icon_blue.svg" alt="issue open icon">
                <p class="issue-title">이슈 제목이 들어갑니다</p>
                <p class="issue-label">라벨 정보가 들어갑니다</p>
            </div>
            <div class="issue-details">
                <p>#이슈번호</p>
                <p>언제 누구에 의해 작성이 되었습니다</p>
                <p>마일스톤 이름이 들어갑니다</p>
            </div>
        </div>
        <div class="issue-avatar">
            <img src="/assets/profile_icon.svg" alt="issue profile frog" />
        </div>
    </div>
</div>


<style>
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
        background-color: #F7F7FC;
        width: 1280px;
    }

    .filter-container {
        display: flex;
        align-items: center;
        border-radius: 8px;
        background-color: #F7F7FC;
        width: 560px ;
        height: 40px;
    }

    .filter-button {
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 8px 12px;
        margin-right: 10px;
        cursor: pointer;
        font-size: 16px;
    }

    .filter-button:hover {
        background-color: #e0e0e0;
    }

    .search-bar-container {
        display: flex;
        align-items: center;
        padding: 8px 12px;
    }

    .search-bar {
        border: none;
        outline: none;
        background-color: #f6f8fa;
        font-size: 16px;
        width: 200px;
    }

    .actions-container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #F7F7FC;
        padding: 5px;
    }

    .label-milestone-action-buttons {
        display: flex;
        align-items: center;
        border: 1px solid #d0d4da;
        border-radius: 8px;
        overflow: hidden;
        background-color: #F7F7FC;
        margin: auto;
    }

    .action-button {
        background-color: #F7F7FC;
        border: none;
        padding: 5px 15px;
        min-width: 100px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        border-right: 1px solid #d0d4da;
    }

    .action-button:last-child {
        border-right: none;
    }

    .action-button:hover {
        background-color: #e0e0e0;
    }

    .action-button > p {
        font-size: 15px;
    }

    .action-button > img {
        margin-right: 5px;
    }

    .create-button {
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 8px;
        padding: 10px 20px;
        font-size: 15px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-left: 20px;
    }

    .create-button:hover {
        background-color: #0056b3;
    }

    .create-button {
        padding: 5px 15px; /* 패딩 조정 */
        min-width: 150px; /* 최소 너비 설정 */
    }

    .search-bar {
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 8px;
        width: 300px;
    }

    .issue-list {
        width: 1280px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 10px;
    }

    .issue-list-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 64px;
        padding: 10px 20px;
        border-top-right-radius: 10px;
        border-top-left-radius: 10px;
        background: #F7F7FC;
    }

    .checkbox-container {
        margin-right: 10px;
    }

    .issue-status {
        display: flex;
        align-items: center;
        flex-grow: 1;
    }

    .issue-status > div {
        margin-right: 20px;
        font-weight: bold;
    }

    .filter-button {
        background: none;
        color: #586069;
        cursor: pointer;
        font-size: 14px;
        padding: 5px 10px;
        border-radius: 6px;
        display: flex;
        align-items: center;
        gap: 5px;
    }

    .issue-item {
        display: flex;
        align-items: center;
        padding: 15px;
        border-bottom: 1px solid #e0e0e0;
        background-color: #FEFEFE;
        margin-left: 5px;
        cursor: pointer;
    }

    .issue-item:last-child {
        /* 마지막 박스 하단만 둥그스름하게 깎기 */
        border-bottom-left-radius: 10px;
        border-bottom-right-radius: 10px;
    }

    .issue-info {
        flex: 1;
        margin-left: 15px;
    }

    .issue-title {
        font-weight: bold;
        border-radius: 4px;
        padding: 2px 4px;
        margin-left: 5px;
    }

    .issue-label {
        border-radius: 10px;
        padding: 4px 8px 4px 8px;
        margin-left: 5px;
        font-size: 10px;
    }

    .issue-details {
        margin-top: 10px;
        display: flex;
        gap: 15px;
        font-size: 14px;
        color: #555;
    }

    .issue-avatar {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .issue-avatar img {
        margin-right: 30px;
        width: 30px;
        height: 30px;
        border-radius: 50%;
    }
</style>
