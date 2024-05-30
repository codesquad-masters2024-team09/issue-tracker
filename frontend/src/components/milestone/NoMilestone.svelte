<script>
    export let isOpen
</script>

<div class="issue-table-row">
    <div class="issue-content-container justify-center items-center w-full">
        <div class="flex justify-center my-2 items-center w-full">
            {#if isOpen}
            <span class="text-center text-sm text-neutral-600/60 whitespace-nowrap">열려있는 마일스톤이 없습니다.</span>
            {:else}
            <span class="text-center text-sm text-neutral-600/60 whitespace-nowrap">닫힌 마일스톤이 없습니다.</span>
            {/if}
        </div>
    </div>
</div>
