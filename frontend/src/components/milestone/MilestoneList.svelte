<script>
    import {milestones} from "../../stores/milestone.js";
    import MilestonePreview from "./MilestonePreview.svelte";
</script>

<div class="milestone-list-box">
    <div class="milestone-list">
        <h3>{$milestones.milestones.length}개의 마일스톤</h3>
    </div>
    <div class="label-element">
        {#each $milestones.milestones as milestone}
            <MilestonePreview {milestone} />
        {/each}
    </div>
</div>

<style>
    h3 {
        font-size: 14px;
        line-height: 24px;
        margin: 0px;
    }
    .milestone-list-box {
        border: 1px solid #d9dbe9;
        border-radius: 16px;
    }
    .milestone-list {
        border-color: transparent;
        border-radius: 16px 16px 0 0;
        padding: 16px;
        background: #f7f7fc;
    }
</style>