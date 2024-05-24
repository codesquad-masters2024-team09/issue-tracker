<script>
    import { writable } from 'svelte/store';

    import {onMount} from "svelte";
    import {getApi, postApi} from "../../service/api.js";
    import {Route, meta} from "tinro";
    import {urlPrefix, MOCK_USER_ID} from "../../utils/constants.js";

    const route = meta();
    const issueId = Number(route.params.issueId);

    let issueData = {
        issueId: issueId,
        memberId: MOCK_USER_ID,
        title: '',
        content: '',
        comments: [],
        createdAt: '',
    }

    let commentInput = '';
    let isSubmitLocked = true;
    $:isSubmitLocked = commentInput.trim() === '';

    const fetchIssue = async (issueID) => {
        try {
            const options = {
                path: `${urlPrefix}/issues/${issueID}`,
            }
            const responseData = await getApi(options);
            console.log('Fetched issue data:', responseData)
            return responseData;
        }
        catch (err) {
            console.error(err);
            alert("오류가 발생했습니다! 다시 시도해주세요!");
        }
    }

    const onCreateComment = async () => {
        try {
            const options = {
                path: `${urlPrefix}/comments`,
                data: {
                    memberId: MOCK_USER_ID,
                    issueId: issueId,
                    content: commentInput,
                }
            }
            await postApi(options);
            const newComments = [...issueData.comments, options.data]
            issueData.comments = newComments
            commentInput = '';
        }
        catch (err) {
            console.log(err);
            alert("코멘트 저장 중 오류가 발생했습니다! 다시 시도해주세요!")
        }
    }

    onMount(async () => {
        const responseData = await fetchIssue(issueId);
        if (responseData) {
            issueData = {...responseData}
        }
        console.log('responseData:', issueData)
    });

</script>

<div class="flex flex-col w-full">
    <div id="header-area">
        <div class="flex gap-3 m-2 p-1 justify-start items-center">
            <span id="title-text" class="inline-block text-4xl">{issueData.title}</span>
            <span class="inline-block text-2xl text-gray-400">#{issueId}</span>
        </div>
        <div class="flex gap-1 m-4 justify-start items-center">
            <div class="flex m-1 p-1 justify-center items-center bg-blue-500 text-white text-[11px] w-[80px] rounded-2xl">
                <span class="pr-[3px]">
                    <i class="bi bi-exclamation-circle"></i>
                    열린 이슈
                </span>
            </div>
            <p class="detail-info inline-block ">이 이슈는 {issueData.createdAt}에 {issueData.memberId}에 의해 열렸습니다</p>
        </div>
    </div>

    <div class="w-full mb-3">
        <hr>
    </div>

    <div id="main-area">
        <div id="content-area" class="flex flex-col items-start">
            <div class="left-section flex gap-4">

                <!-- 이슈와 댓글 -->
                <div class="content-box flex-col my-3 w-[960px] w-min-[500px]">
                    <!-- 이슈 -->
                    <div class="content-box header flex gap-2 justify-start items-center">
                        <!-- 프로필 아이콘 -->
                        <div class="size-9">
                            <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
                        </div>
                        <!-- 작성자 -->
                        <div class="grow">{issueData.memberId}</div>
                    </div>
                    <!-- 이슈 내용 -->
                    <div class="content-box main">
                        <p>{issueData.content}</p>
                    </div>

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

                    <!-- 댓글 -->
                    {#each issueData.comments as comment}
                    <div class="content-box header mt-3 flex gap-2 justify-start items-center">
                        <!-- 프로필 아이콘 -->
                        <div class="size-9">
                            <img src="/assets/profile_icon.svg" alt="Profile Icon" class="profile-icon">
                        </div>
                        <!-- 작성자 -->
                        <div class="grow">{comment.memberId}</div>
                    </div>
                    <!-- 댓글 내용 -->
                    <div class="content-box main">
                        <p>{comment.content}</p>
                    </div>
                    {/each}
                </div>

                <div class="flex-1">
                    <div id="additional-info-area" class="option-parent">
                        <div class="option-item">
                            <div class="option-item-container">
                                <span>담당자</span>
                                <button class="add-button">+</button>
                            </div>
                        </div>
                        <div class="option-item">
                            <div class="option-item-container">
                                <span>레이블</span>
                                <button class="add-button">+</button>
                            </div>
                        </div>
                        <div class="option-item">
                            <div class="option-item-container">
                                <span>마일스톤</span>
                                <button class="add-button">+</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 댓글 -->
            <div class="flex flex-col w-[960px] w-min-[500px]">
                <!-- 입력 폼 -->
                <div class="comment-container">
                    <div class="flex flex-col p-1 w-full">
                        <div class="flex flex-col content-input rounded-lg">
                            <textarea placeholder="내용을 입력하세요" class="mb-1 w-full h-[250px] rounded-lg p-3 focus:bg-white focus:outline outline-neutral-500" bind:value={commentInput}></textarea>
                            <div class="attachment flex justify-start items-center  bg-neutral-100 rounded-md cursor-pointer">
                                <span class="m-1">
                                    <i class="bi bi-paperclip"></i>
                                </span>
                                <span class="text-sm text-gray-600">파일 첨부하기</span>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 작성완료 버튼 -->
                <div class="flex justify-end">
                    <button type="button" class="btn issue create submit-button" disabled={isSubmitLocked} on:click={onCreateComment}>+ 코멘트 작성</button>
                </div>
            </div>

        </div>
    </div>
</div>
