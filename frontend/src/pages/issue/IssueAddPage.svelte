<script>
    import { issues } from "../../stores/issue";
    import { router } from "tinro";

    let issueFormValues = {
        memberId: 'testUser',
        title: '',
        content: '',
        labels: [],
        milestone: '',
    }

    const onCreateIssue = async () => {
        try {
            await issues.createIssue(issueFormValues);
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
        isSubmitLocked = issueFormValues.title.trim() === '' || issueFormValues.content.trim() === '';
    }

</script>

<div class="flex flex-col w-full animate-slidein">

    <div id="header-area" class="text-gray-800 text-4xl">
        <strong id="title-text">새로운 이슈 작성</strong>
        <hr class="mt-9 mb-3">
    </div>

    <div id="main-area">
        <div id="content-area" class="flex gap-4 justify-between">
            <!-- 프로필 아이콘 -->
            <div class="flex flex-auto gap-1 mt-3">
                <div class="flex-none size-9">
                    <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
                </div>

                <!-- 입력 폼 -->
                <div class="flex flex-col w-full">
                    <div class="flex issue-header mb-2">
                        <input type="text" placeholder="제목" class="" bind:value={issueFormValues.title}>
                    </div>
                    <div class="flex flex-col content-input rounded-lg ">
                        <textarea placeholder="내용을 입력하세요" class="mb-1 w-full h-[310px] rounded-lg p-3 focus:bg-white focus:outline outline-neutral-500" bind:value={issueFormValues.content}></textarea>
                        <div class="attachment flex justify-start items-center bg-neutral-200/50 rounded-md cursor-pointer">
                        <span class="m-1">
                            <i class="bi bi-paperclip"></i>
                        </span>
                            <span class="text-sm text-gray-600">파일 첨부하기</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 우측 패널 -->
            <div id="additional-info-area" class="option-parent">
                <div class="option-item">
                    <div class="option-item-container">
                        <span>담당자</span>
                        <button type="button" class="add-button">+</button>
                    </div>
                </div>
                <div class="option-item">
                    <div class="option-item-container">
                        <span>레이블</span>
                        <button type="button" class="add-button">+</button>
                    </div>
                </div>
                <div class="option-item">
                    <div class="option-item-container">
                        <span>마일스톤</span>
                        <button type="button" class="add-button">+</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <hr class="mt-9 mb-3">

    <!-- 작성 취소 / 완료 버튼 -->
    <div class="flex justify-end action-buttons">
        <button type="button" class="btn issue cancel"
                on:click={onCancelCreateIssue}>✕ 작성 취소</button>
        <button type="button" class="btn issue create"
                disabled={isSubmitLocked}
                on:click={onCreateIssue}>완료</button>
    </div>

</div>
