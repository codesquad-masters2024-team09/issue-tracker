<script>
    export let issue
</script>

<div class="issue-table-row">
    <!-- 체크 박스 -->
    <div class="checkbox">
        <input type="checkbox" class="child-checkbox">
    </div>
    <!-- 제목 + 이슈 번호 + 작성자 + 마일스톤 정보 -->
    <div class="issue-content-container">
        <!-- 이슈 제목 -->
        <div class="issue-content-title">
            <span class="text-sm text-blue-700 pr-[6px]">
                <i class="bi bi-exclamation-circle"></i>
            </span>
                <a href="/issues/{issue.issueId}">{issue.title}</a>
                <!-- 레이블 뱃지 -->
                {#each issue.labelNames as label}
                    <div class="label-badge-container">
                        <div class="label-badge border border-gray-200"
                            style="background-color: {label.colorCode}; color: {label.textColor};">
                            {label.labelId}
                        </div>
                    </div>
                {/each}
            </div>
        <!-- 이슈 번호 + 작성자 및 타임스탬프 정보 + 마일스톤 -->
        <div class="issue-writer-timestamp">
            <!-- 이슈 번호 -->
            <div>
                #{issue.issueId}
            </div>
            <!-- 작성자 및 타임스탬프 -->
            <div>
                이 이슈가 {issue.createdAt}, {issue.memberId}님에 의해 작성되었습니다.
            </div>
            <!-- 마일스톤 -->
            {#if issue.milestoneName !== null}
                <div>
                    <span class="text-[16px]">
                        <i class="bi bi-signpost"></i>
                    </span>
                    {issue.milestoneName}
                </div>
            {:else}
            <div class="text-sm">
                <span class="text-[16px]">
                    <i class="bi bi-signpost"></i>
                </span>
                지정된 마일스톤이 없습니다.
            </div>
            {/if}
        </div>
    </div>
</div>
