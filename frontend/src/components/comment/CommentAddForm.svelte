<script>
    export let issueId; // long 타입
    export let issueData;

    import { issues } from '../../stores/issue';
    import { getApi, postApi } from "../../service/api";
    import { urlPrefix, MOCK_USER_ID } from "../../utils/constants";

    /* 댓글 입력 제어 */
    let commentInput = '';
    let isSubmitLocked = true;

    $:isSubmitLocked = commentInput.trim() === '';

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

    /* 파일 업로드 */
    let selectedFile;

    function handleFileUpload(event) {
        const files = event.target.files;
        if (files.length > 0) {
            selectedFile = files[0];
            console.log('Selected file:', selectedFile);
        }
    }

    function triggerFileUpload() {
        document.getElementById('file-upload').click();
    }
</script>

<!-- 댓글 입력 폼 -->
<div class="flex flex-col w-[960px] w-min-[500px]">
    <div class="comment-container">
        <div class="flex flex-col p-1 w-full">
            <div class="flex flex-col rounded-lg">
                <textarea placeholder="내용을 입력하세요" class="mb-1 w-full h-[250px] rounded-lg p-3 bg-neutral-200/50 focus:bg-white focus:outline outline-neutral-500" bind:value={commentInput}></textarea>
                <button class="flex text-[14px] justify-start items-center bg-neutral-200/50 border border-neutral-200/50 rounded-md cursor-pointer" on:click={triggerFileUpload}>
                    <span class="mx-2 my-1">
                        <i class="bi bi-paperclip"></i>
                    </span>
                    파일 업로드
                    <input type="file" id="file-upload" class="hidden" on:change={handleFileUpload} />
                    {#if selectedFile}
                        <div class="flex gap-2 justify-start mx-4 text-[14px] text-gray-400 whitespace-nowrap">
                            <p class="inline-flex"><span class="text-gray-600 mx-1">파일명: </span> {selectedFile.name}</p>
                            <p class="inline-flex"><span class="text-gray-600 mx-1">파일 크기: </span> {selectedFile.size} bytes</p>
                        </div>
                    {/if}
                </button>
            </div>
        </div>
    </div>
    <!-- 작성완료 버튼 -->
    <div class="flex justify-end">
        <button type="button" class="btn issue create submit-button" disabled={isSubmitLocked} on:click={onCreateComment}>+ 코멘트 작성</button>
    </div>
</div>
