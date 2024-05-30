<script>
    import {milestones} from "../../stores/milestone.js";
    import {DateInput} from "date-picker-svelte";

    export let milestone;

    // 원본으로부터 생성
    let updateData = {...milestone}

    let isSubmitLocked = true;
    $: isSubmitLocked = updateData.id === null ? true : updateData.id.trim() === ''

    const parseDate = (str) => {
        return str ? new Date(str) : null;
    }

    $: updateData.dueDate = parseDate(updateData.dueDate)

    const onCloseEditModeMilestone = () => {
        updateData = {...milestone} // 원본으로부터 원복
        milestones.closeEditModeMilestone()
    }

    const onUpdateMilestone = () => {
        milestones.updateMilestone(milestone.id, updateData);
    }
</script>

<div class="label-form-edit-container animate-slidein">
    <div class="label-form-header">마일스톤 편집</div>
    <div class="flex items-center">
        <div class="label-form-input-container">
            <div class="label-form-input-box">
                <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                    이름
                </span>
                <input id="labelId" class="px-[6rem]" type="text" bind:value={updateData.id}
                       placeholder="마일스톤의 이름을 입력하세요" maxlength="30"/>
            </div>

            <div class="label-form-input-box">
                <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                    완료일(선택)
                </span>
                <input id="labelId" class="px-[6rem]" type="text" bind:value={updateData.dueDate}
                       placeholder="YYYY.MM.DD" maxlength="30"/>
                <DateInput class="w-full border-none" bind:value={updateData.dueDate} format="yyyy.MM.dd"
                           placeholder="YYYY.MM.DD"/>
            </div>

            <div class="label-form-input-box">
                <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                    설명(선택)
                </span>
                <input id="description" class="px-[6rem]" type="text" bind:value={updateData.description}
                       placeholder="마일스톤에 대한 설명을 입력하세요" maxlength="50"/>
            </div>
        </div>
    </div>

    <div class="edit-title-container gap-3">
        <button class="edit-title-btn blue-border gap-2" on:click={onCloseEditModeMilestone}>
            <span class="">
                <i class="bi bi-x-lg"></i>
            </span>
            취소
        </button>
        <button class="edit-title-btn apply gap-2" on:click={onUpdateMilestone} disabled={isSubmitLocked}>
            <span class="text-white">
                <i class="bi bi-plus-lg"></i>
            </span>
            완료
        </button>
    </div>
</div>