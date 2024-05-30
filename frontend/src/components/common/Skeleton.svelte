<script>
    export let size
</script>


{#if size === 'xl' }
    <div class="flex animate-pulse">
        <div class="flex w-[800px] h-4 bg-gray-300 rounded-full overflow-hidden " />
    </div>
{:else if size === 'lg'}
    <div class="flex animate-pulse">
        <div class="flex w-[600px] h-2 bg-gray-300 rounded-full overflow-hidden " />
    </div>
{:else if size === 'md'}
    <div class="flex animate-pulse">
        <div class="flex w-[400px] h-2 bg-gray-300 rounded-full overflow-hidden " />
    </div>
{:else if size === 'sm'}
    <div class="flex animate-pulse">
        <div class="flex w-[200px] h-2 bg-gray-300 rounded-full overflow-hidden " />
    </div>
{:else if size === 'xs'}
    <div class="flex animate-pulse">
        <div class="flex w-[100px] h-2 bg-gray-300 rounded-full overflow-hidden " />
    </div>
{:else if size === 'xxs'}
    <div class="flex animate-pulse">
        <div class="flex w-[100px] h-1 bg-gray-300 rounded-full overflow-hidden " />
    </div>
{/if}
