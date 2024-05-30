<script>
    import {onMount} from "svelte";
    import {issues} from '../../stores/issue';
    import {postApi} from "../../service/api.js";
    import {meta, router} from "tinro";
    import {MAX_FILE_SIZE, urlPrefix} from "../../utils/constants.js";
    import IssueEditTitleForm from '../../components/issue/IssueEditTitleForm.svelte';
    import IssueEditContentForm from "../../components/issue/IssueEditContentForm.svelte";
    import SideBar from "../../components/common/SideBar.svelte";
    import Header from "../../components/common/Header.svelte";
    import {get} from "svelte/store";
    import {auth} from "../../stores/auth.js";
    import axios from "axios";

    const route = meta();
    const issueId = Number(route.params.issueId);

    let issueData = {
        id: issueId,
        memberId: '',
        title: '',
        content: '',
        comments: [],
        assignees: [],
        labels: [],
        milestoneId: '',
        milestoneProgress: '',
        createdAt: '',
    }

    let isFullMount = issueData.id !== null
    $: isFullMount

    onMount(async () => {
        const responseData = await issues.fetchIssueDetail(issueId);
        if (responseData) {
            issueData = {...responseData}
            console.log('로딩된 issueData:', issueData)
        }
    });

    /* 댓글 입력 제어 */
    let commentInput = '';
    let isSubmitLocked = true;

    $:isSubmitLocked = commentInput.trim() === '';

    /* 이슈 제목 수정 제어 */
    $:defaultTitle = issueData.title // 입력 폼에 전달할 현재 화면에 보이는 이슈의 제목

    let isViewEditModeTitle = false
    $: {
        if ($issues.editTitlePopup === issueId.toString()) {
            isViewEditModeTitle = true
        } else {
            isViewEditModeTitle = false
        }
    }

    /* 업데이트 된 이슈 제목을 IssueEditTitleForm 컴포넌트로부터 받아 갱신 */
    function updateIssueTitle(newTitle) {
        defaultTitle = newTitle;
    }

    /* 이슈 본문 내용 수정 제어 */
    let isFocused = false

    $:defaultContent = issueData.content // 입력 폼에 전달할 현재 화면에 보이는 이슈의 내용

    let isViewEditModeContent = false
    $: {
        if ($issues.editContentPopup === issueId.toString()) {
            isViewEditModeContent = true
        } else {
            isViewEditModeContent = false
        }
    }

    /* 업데이트 된 이슈 본문 내용을 IssueEditContentForm 컴포넌트로부터 받아 갱신 */
    function updateIssueContent(newContent) {
        defaultContent = newContent;
    }

    const onCreateComment = async () => {
        try {
            const options = {
                path: `${urlPrefix}/comments`,
                data: {
                    memberId: get(auth).memberId,
                    issueId: issueId,
                    content: commentInput,
                },
                access_token: get(auth).accessToken
            }
            await postApi(options);
            const newComments = [...issueData.comments, options.data]
            issueData.comments = newComments
            commentInput = '';
        } catch (err) {
            console.log(err);
            alert("코멘트 저장 중 오류가 발생했습니다! 다시 시도해주세요!")
        }
    }

    const onToggleEditTitlePopup = (issueId) => {
        if (isViewEditModeTitle === true) {
            issues.closeEditModeIssueTitle()
            return
        }
        issues.openEditModeIssueTitle(issueId.toString())
    }

    const onToggleEditContentPopup = (issueId) => {
        if (isViewEditModeContent === true) {
            issues.closeEditModeIssueContent()
            return
        }
        issues.openEditModeIssueContent(issueId.toString())
    }

    const onDeleteIssue = () => {
        if (confirm('현재 이슈를 삭제하시겠습니까?')) {
            issues.deleteIssue(issueId)
            router.goto('/')
        }
    }

    const onUpdateComment = async (commentId) => {
        // TODO: 댓글 수정 api 연결
    }

    /* 파일 업로드 */
    let selectedFile;

    function handleFileUpload(event) {
        const files = event.target.files;
        if (files.length > 0) {
            const selectedFile = files[0];
            if (selectedFile.size <= MAX_FILE_SIZE) {
                const reader = new FileReader();
                reader.readAsDataURL(selectedFile);
                reader.onload = function(e) {
                    const base64ImageContent = e.target.result;
                    const fileType = selectedFile.type.split('/')[1];
                    const data = {
                        name: selectedFile.name,
                        content: base64ImageContent,
                        type: fileType,
                    };
                    axios.post(import.meta.env.API_GATE_WAY_END_POINT, data)
                        .then(res => {
                            commentInput += '\n' + `[${selectedFile.name}](${res.data.url})`;
                        })
                        .catch(err => alert("에러!" + err));
                }
            } else {
                alert("10MB 이하의 파일만 업로드 가능합니다");
            }
        }
    }

    function triggerFileUpload() {
        document.getElementById('file-upload').click();
    }
</script>

<Header />
<div class="flex flex-col w-full">
    <div class="w-full">
        <!-- 이슈 제목 컴포넌트 -->
        {#if !($issues.editModeTitle === issueId.toString())}
        <div class="flex w-full justify-between items-center">
            <div class="flex gap-3 mx-2 p-2 justify-start items-center">
                <span id="title-text" class="inline-block w-full text-4xl whitespace-nowrap">{defaultTitle}</span>
                <span class="inline-block text-2xl w-full text-gray-400">#{issueId}</span>
            </div>

            <!-- 버튼 컨테이너 -->
            <div class="edit-title-container">
                <button type="button" class="edit-title-btn blue-border"
                        on:click={() => onToggleEditTitlePopup(issueId)}>
                <span class="text-[12px] text-center pr-1">
                    <i class="bi bi-archive"></i>
                </span>
                    제목 편집
                </button>
                <button type="button" id="close-issue" class="edit-title-btn blue-border"
                        on:click={() => onToggleEditTitlePopup(issueId)}>
                <span class="text-[12px] text-center pr-1">
                    <i class="bi bi-x-lg"></i>
                </span>
                    이슈 닫기
                </button>
            </div>
        </div>
        {/if}

        <!-- 제목 편집 폼 -->
        {#if $issues.editModeTitle === issueId.toString()}
        <div class="flex justify-between items-center" class:block={isViewEditModeTitle}>
            <IssueEditTitleForm {issueId} {defaultTitle} on:updateIssueTitle={e => updateIssueTitle(e.detail)} />
        </div>
        {/if}

        <div class="flex gap-1 m-4 justify-start items-center translate-y-3">
            <div class="flex m-1 p-1 justify-center items-center bg-blue-500 text-white text-[11px] w-[80px] min-w-[80px] rounded-2xl">
                <span class="pr-[3px]">
                    <i class="bi bi-exclamation-circle"></i>
                    열린 이슈
                </span>
            </div>
            <p class="inline-block whitespace-nowrap">이 이슈는 {issueData.createdAt}에 {issueData.memberId}에 의해 열렸습니다</p>
            <!-- 이슈 삭제 버튼 -->
            <div class="mr-2 ml-auto translate-x-2">
                <button type="submit" class="text-sm text-red-500" on:click={onDeleteIssue}>
                    <span class="text-red-500 pr-[3px]"><i class="bi bi-trash"></i></span>
                    이슈 삭제
                </button>
            </div>
        </div>
    </div>

    <div class="w-full mb-3">
        <hr>
    </div>

    <div class="main-area">
        <div id="content-area" class="flex flex-col w-full">
            <div class="flex gap-4 w-full">

                <!-- 이슈와 댓글 -->
                <div class="parent-container w-full flex-col my-3 w-min-[500px]">
                    <!-- 이슈 -->
                    <div class={`${isFocused ? 'content-box header-border flex gap-2 justify-start items-center' : 'content-box header flex gap-2 justify-start items-center'}`} >
                        <!-- 프로필 아이콘 -->
                        <div class="size-9">
                            <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
                        </div>
                        <!-- 작성자 -->
                        <div class="grow">{issueData.memberId}</div>
                        <!-- 작성자 뱃지 -->
                        <div class="writer-badge">작성자</div>
                        <!-- 편집 버튼 -->
                        <div class="issue-edit-container">
                            <span class="pr-[3px]">
                                <i class="bi bi-pencil-square"></i>
                            </span>
                            <button type="button" on:click={() => onToggleEditContentPopup(issueId)}>편집</button>
                        </div>
                    </div>

                    <!-- 이슈 내용 -->
                    {#if !($issues.editModeContent === issueId.toString())}
                    <div class="content-box main">
                        <p>{defaultContent}</p>
                    </div>

                    <!--이슈 내용 편집 폼 -->
                    {:else if $issues.editModeContent === issueId.toString()}
                    <div class:block={isViewEditModeContent}>
                        <IssueEditContentForm {issueId} {defaultContent} bind:isFocused={isFocused} on:updateIssueContent={e => updateIssueContent(e.detail)} />
                    </div>
                    {/if}

                    <!-- 댓글 돌이 -->
                    <div class="content-box header mt-3 flex gap-2 justify-start items-center">
                        <!-- 프로필 아이콘 -->
                        <div class="size-9">
                            <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
                        </div>
                        <!-- 작성자 -->
                        <div class="grow">댓글돌이</div>
                    </div>
                    <!-- 댓글 내용 -->
                    <div class="content-box main">
                        <p>모든 이슈에 댓글돌이가 댓글을 달아줍니다 :) </p>
                    </div>

                    <!-- 댓글 리스트 -->
                    {#each issueData.comments as comment}
                        <div class="content-box header mt-3 flex gap-2 justify-start items-center">
                            <!-- 프로필 아이콘 -->
                            <div class="size-9">
                                <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
                            </div>
                            <!-- 작성자 -->
                            <div class="grow">{comment.memberId}</div>
                            <!-- 작성자 뱃지 -->
                            {#if comment.memberId === issueData.memberId}
                                <div class="writer-badge">작성자</div>
                            {/if}
                            <!-- 편집 버튼 -->
                            <div class="issue-edit-container">
                            <span class="pr-[3px]">
                                <i class="bi bi-pencil-square"></i>
                            </span>
                                <button type="button" on:click={onUpdateComment} data-comment-id={comment.id}>편집
                                </button>
                            </div>
                        </div>

                        <!-- 댓글 내용 -->
                        <div class="content-box main">
                            <p>{comment.content}</p>
                        </div>
                    {/each}

                    <!-- 댓글 입력 폼 -->
                    <div class="flex flex-col w-full w-min-[500px]">
                        <div class="comment-container">
                            <div class="flex flex-col p-1 w-full">
                                <div class="flex flex-col rounded-lg">
                                    <label for="comment-form" class="absolute translate-y-3 translate-x-3 text-sm text-gray-600">댓글로 의견을 공유해 보세요</label>
                                    <textarea id="comment-form"
                                              class="comment-add-textarea"
                                              bind:value={commentInput}></textarea>
                                    <button class="flex text-[14px] justify-start items-center bg-neutral-200/50 border border-neutral-200/50 rounded-md cursor-pointer"
                                            on:click={triggerFileUpload}>
                                        <span class="mx-2 my-1">
                                            <i class="bi bi-paperclip"></i>
                                        </span>
                                        파일 업로드
                                        <input type="file" id="file-upload" class="hidden" on:change={handleFileUpload}/>
                                        {#if selectedFile}
                                            <div class="flex gap-2 justify-start mx-4 text-[14px] text-gray-400 whitespace-nowrap">
                                                <p class="inline-flex"><span
                                                        class="text-gray-600 mx-1">파일명: </span> {selectedFile.name}</p>
                                                <p class="inline-flex"><span
                                                        class="text-gray-600 mx-1">파일 크기: </span> {selectedFile.size}
                                                    bytes</p>
                                            </div>
                                        {/if}
                                    </button>
                                </div>
                            </div>
                        </div>
                        <!-- 작성완료 버튼 -->
                        <div class="flex justify-end">
                            <button type="button" class="btn issue create submit-button" disabled={isSubmitLocked}
                                    on:click={onCreateComment}>+ 코멘트 작성
                            </button>
                        </div>
                    </div>

                </div>

                <!-- 우측 패널 -->
                <div class="flex flex-col items-end relative">
                    <!-- 담당자 & 레이블 & 마일스턴 지정 패널 -->
                    <SideBar />
                </div>
            </div>
        </div>
    </div>
</div>
