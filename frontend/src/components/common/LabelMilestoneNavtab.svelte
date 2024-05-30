<script>
    import { router } from "tinro";
    import { onMount } from 'svelte';
    import { labels } from '../../stores/label'
    import { milestones } from '../../stores/milestone'

    let isLabelDisabled = false;
    let isMilestoneDisabled = false;
    let milestoneCounts = 0

    $: labelCounts = $labels.labels.length
    $: milestoneCounts = $milestones.milestones.length

    onMount(async () => {
        isLabelDisabled = window.location.pathname === '/labels';
        if (!isLabelDisabled) {
            await labels.fetchLabels();
        }

        isMilestoneDisabled = window.location.pathname === '/milestones';
        if (!isMilestoneDisabled) {
            await milestones.fetchMilestones();
        }
    });
</script>

<div class="flex mx-3 rounded-md min-w-[350px] min-h-[30px] max-h-[44px]" role="group">
    <button type="button" class="gray-btn label-milestone-panel rounded-s-2xl"
            on:click={() => router.goto("/labels")} disabled={isLabelDisabled}>
    <span class="text-[16px]">
        <i class="bi bi-tag"></i>
    </span>
        레이블({labelCounts})
    </button>
    <button type="button" class="gray-btn label-milestone-panel rounded-e-2xl"
            on:click={() => router.goto("/milestones")} disabled={isMilestoneDisabled}>
    <span class="text-[16px]">
        <i class="bi bi-signpost"></i>
    </span>
        마일스톤({milestoneCounts})
    </button>
</div>
