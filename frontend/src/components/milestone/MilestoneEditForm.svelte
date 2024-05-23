<script>
    import {milestones} from "../../stores/milestone.js";
    import {DateInput} from "date-picker-svelte";

    export let milestone;
    let originData = {...milestone}

    const onCloseEditModeMilestone = () => {
        milestone = {...originData}
        milestones.closeEditModeMilestone()
    }

    const onUpdateMilestone = () => {
        const changes = diff(originData, milestone);
        if(Object.keys(changes).length > 0) {
            // if(changes.id !== null && changes.id !== '') {
                milestones.updateMilestone(originData.id, changes);
            // }
        }
    }

    function diff(oldData, newData) {
        const changes = {};
        for (const key in newData) {
            if (oldData[key] !== newData[key]) {
                changes[key] = newData[key];
            }
        }
        return changes;
    }
</script>

<div class="milestone-form-container">
    <div>
        <div>
            <label for="milestoneId">이름</label>
            <input id="milestoneId" type="text" bind:value={milestone.id} placeholder="마일스톤의 이름을 입력하세요" maxlength="30" />
        </div>
        <div>
            <label for="dueDate">완료일(선택)</label>
            <DateInput bind:value={milestone.dueDate} format="yyyy.MM.dd" placeholder="YYYY.MM.DD"/>
        </div>
        <div>
            <label for="description">설명(선택)</label>
            <input id="description" type="text" bind:value={milestone.description} placeholder="마일스톤에 대한 설명을 입력하세요" maxlength="50" />
        </div>
    </div>

    <div>
        <button on:click={onCloseEditModeMilestone}>취소</button>
        <button on:click={onUpdateMilestone}>완료</button>
    </div>
</div>