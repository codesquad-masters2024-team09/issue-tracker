<script>
    import { milestones } from "../../stores/milestone.js";
    import MilestoneEditForm from "./MilestoneEditForm.svelte";

    export let milestone;

    const formatDateStr = (isoString) => {
        const date = new Date(isoString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    $: formatDueDate = milestone.dueDate ? formatDateStr(milestone.dueDate) : milestone.dueDate;

    const onCloseMilestone = (id) => {
        if(confirm('이 마일스톤을 닫겠습니까?')) {
            const isOpen = false
            milestones.patchMilestone(id, isOpen)
        }
    }

    const onEditModeMilestone = (id) => {
        milestones.openEditModeMilestone(id)
    }

    const onDeleteMilestone = (id) => {
        if(confirm('삭제하시겠습니까?')) {
            milestones.deleteMilestone(id)
        }
    }
</script>

<div class="issue-table-row">
    {#if $milestones.editMode === milestone.id}
        <MilestoneEditForm {milestone} />
    {:else}
        <div class="flex flex-col items-start">
            <div class="flex justify-start items-center min-w-[170px]">
                <div class="flex items-center mx-4 text-[14px] font-bold text-gray-800">
                <span class="text-[16px] mr-1">
                    <i class="bi bi-signpost"></i>
                </span>
                    {milestone.id}
                </div>
                <div>
                    <div class="flex gap-2 items-center text-[12px] text-gray-500">
                        {#if formatDueDate}
                        <span>
                            <i class="bi bi-calendar2"></i>
                        </span>
                            {formatDueDate}
                        {:else}
                        <span>
                            <i class="bi bi-calendar2"></i>
                        </span>
                            지정된 완료일이 없습니다.
                        {/if}
                    </div>
                </div>
            </div>
            <div class="ml-4 mt-2 text-[14px] text-gray-500">
                {#if milestone.description}
                    {milestone.description}
                {:else}
                    마일스톤에 대한 설명이 없습니다.
                {/if}
            </div>
        </div>

        <div class="flex flex-col gap-2 ml-auto justify-between items-center">
            <!--      편집 버튼      -->
            <div class="label-btn-container ml-auto flex gap-5 mx-4 my-1 items-center text-sm whitespace-nowrap">
                <button class="text-gray-800" on:click={() => onCloseMilestone(milestone.id)}>
                    <span>
                        <i class="bi bi-archive"></i>
                    </span>
                    닫기
                </button>
                <button class="text-gray-800" on:click={() => onEditModeMilestone(milestone.id)}>
                    <span>
                        <i class="bi bi-pencil-square"></i>
                    </span>
                    편집
                </button>
                <button class="text-red-500" on:click={() => onDeleteMilestone(milestone.id)}>
                    <span>
                        <i class="bi bi-trash"></i>
                    </span>
                    삭제
                </button>
            </div>
            <div class="flex flex-col text-[12px]">
                <div class="flex w-[220px] h-2 bg-gray-300/80 rounded-full overflow-hidden " role="progressbar" aria-valuenow={milestone.progress} aria-valuemin="0" aria-valuemax="100">
                    <div class="flex flex-col justify-center rounded-full overflow-hidden bg-gray-800/70 text-xs text-white text-center whitespace-nowrap transition duration-500" style="width: {milestone.progress}%"></div>
                </div>
                <div class="flex mt-2 justify-between items-center text-gray-600/90">
                    <span>{milestone.progress}%</span>
                    <div class="flex gap-1">
                        <span>열린 이슈 {milestone.openIssues}</span>
                        <span>닫힌 이슈 {milestone.closeIssues}</span>
                    </div>
                </div>
            </div>
        </div>
    {/if}
</div>
