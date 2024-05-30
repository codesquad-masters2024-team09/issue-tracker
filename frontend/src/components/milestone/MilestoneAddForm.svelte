<script>
    import { milestones } from '../../stores/milestone.js'
    import { milestoneValidate } from "../../utils/validates.js";

    let milestone = {
        id: null,
        dueDate: null,
        description: null,
    }

    $: isSubmitLocked = milestone.id === null ? true : milestone.id.trim() === ''

    const parseDate = (str) => {
        return str ? new Date(str) : null;
    }

    const onAddMilestone = async () => {
        try {
            milestone.dueDate = parseDate(milestone.dueDate)
            await milestoneValidate.validate(milestone, {abortEarly: true})
            await milestones.registerMilestone(milestone)
            onCancelAddMilestone()
        } catch (err) {
            alert(err)
        }
    }

    const onCancelAddMilestone = () => {
        milestone.id = ''
        milestone.dueDate = null
        milestone.description = ''
        milestones.toggleAddModeMilestone()
    }

</script>

<div class="label-form-container animate-slidein">
    <div class="flex flex-col gap-2 justify-between">
        <div class="label-form-header">새로운 마일스톤 추가</div>
        <div class="flex items-center">
            <div class="label-form-input-container">
                <div class="flex flex-col gap-3" role="group">
                    <div class="flex gap-3 justify-between items-center">

                        <div class="label-form-input-box">
                            <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                                이름
                            </span>
                            <input id="labelId" class="px-[6rem]" type="text" bind:value={milestone.id}
                                   placeholder="마일스톤의 이름을 입력하세요" maxlength="30"/>
                        </div>

                        <div class="label-form-input-box">
                            <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                                완료일(선택)
                            </span>
                            <input id="labelId" class="px-[6rem]" type="text" bind:value={milestone.dueDate} placeholder="YYYY-MM-DD" maxlength="30" />
                        </div>
                    </div>

                    <div class="label-form-input-box">
                        <span class="absolute translate-y-2 ml-1 text-[14px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                            설명(선택)
                        </span>
                        <input id="description" class="px-[6rem]" type="text" bind:value={milestone.description}
                               placeholder="마일스톤에 대한 설명을 입력하세요" maxlength="50"/>
                    </div>
                </div>

            </div>
        </div>
        <div class="edit-title-container gap-3">
            <button class="edit-title-btn blue-border gap-2" on:click={onCancelAddMilestone}>
                <span>
                    <i class="bi bi-x-lg"></i>
                </span>
                취소
            </button>
            <button class="edit-title-btn apply gap-2" on:click={onAddMilestone} disabled={isSubmitLocked}>
                <span class="text-white">
                    <i class="bi bi-plus-lg"></i>
                </span>
                완료
            </button>
        </div>
    </div>
</div>
