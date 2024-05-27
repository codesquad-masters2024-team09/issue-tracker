<script>
    import { router } from 'tinro';
</script>

<div class="flex mx-3 rounded-md min-w-[350px] min-h-[30px] max-h-[44px]" role="group">
    <button type="button" class="gray-btn label-milestone-pannel rounded-s-2xl" on:click={() => router.goto("/labels")}>
    <span class="text-[16px]">
        <i class="bi bi-tag"></i>
    </span>
        레이블(3)
    </button>
    <button type="button" class="gray-btn label-milestone-pannel rounded-e-2xl" on:click={() => router.goto("/milestones")}>
    <span class="text-[16px]">
        <i class="bi bi-signpost"></i>
    </span>
        마일스톤(1)
    </button>
</div>
