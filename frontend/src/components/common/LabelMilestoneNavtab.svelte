<script>
    import { router } from "tinro";
    import { onMount } from 'svelte';

    let isLabelDisabled = false;
    let isMilestoneDisabled = false;

    onMount(() => {
        isLabelDisabled = window.location.pathname === '/labels';
        isMilestoneDisabled = window.location.pathname === '/milestones';
    });
</script>

<div class="flex mx-3 rounded-md min-w-[350px] min-h-[30px] max-h-[44px]" role="group">
    <button type="button" class="gray-btn label-milestone-panel rounded-s-2xl"
            on:click={() => router.goto("/labels")} disabled={isLabelDisabled}>
    <span class="text-[16px]">
        <i class="bi bi-tag"></i>
    </span>
        레이블(3)
    </button>
    <button type="button" class="gray-btn label-milestone-panel rounded-e-2xl"
            on:click={() => router.goto("/milestones")} disabled={isMilestoneDisabled}>
    <span class="text-[16px]">
        <i class="bi bi-signpost"></i>
    </span>
        마일스톤(1)
    </button>
</div>
