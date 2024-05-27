<script>
    import {milestones} from '../../stores/milestone.js'
    import {milestoneValidate} from "../../utils/validates.js";
    import { DateInput } from 'date-picker-svelte'

    let milestone = {
        id: null,
        dueDate: null,
        description: null,
    }

    const onAddMilestone = async () => {
        try {
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
    }

</script>

<div>
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
        <button on:click={onCancelAddMilestone}>취소</button>
        <button on:click={onAddMilestone}>완료</button>
    </div>
</div>

<style>

</style>