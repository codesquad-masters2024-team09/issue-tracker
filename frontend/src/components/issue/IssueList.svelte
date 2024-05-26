<script>
    import { onMount } from "svelte";
    import { get } from 'svelte/store';
    import { issues, loadingIssue, issuesPageLock } from '../../stores/issue';
    import { handleCheckboxes } from "../../service/buttonSelection.js";
    import { router } from "tinro";
    import { optionsStore } from "../../stores/filter.js";
    import IssueFilterOptions from "./FilterOptions.svelte";
    import Issue from './IssueItem.svelte';
    import NoIssue from "./NoIssue.svelte";
    import IssueLoading from "./IssueLoading.svelte";
    import LabelMilestoneNavTab from "../common/LabelMilestoneNavtab.svelte";

    onMount(() => {
        // checkbox multi select 부분
        const parentCheckbox = document.querySelector(".parent-checkbox");
        const childCheckboxes = document.querySelectorAll(".child-checkbox");
        if (parentCheckbox && childCheckboxes.length > 0) {
            handleCheckboxes(parentCheckbox, childCheckboxes);
        }

        // 이슈 리스트 가져오기
        issues.resetIssues()
        issues.fetchIssues().then(() => {
            console.log('리스트 컴포넌트에서 받은 이슈 목록: ', get(issues).issueList)
        });
    })
</script>

<div class="flex-col">
    <div class="flex my-[3rem] justify-between items-center w-full min-w-[1020px]">
        <!--    필터 + 검색 패널    -->
        <div class="filter-container" role="group">
            <button class="gray-btn flex-2 px-[2rem] border border-gray-300/60 rounded-s-md text-gray-600 align-middle whitespace-nowrap">
                필터
            </button>
            <button type="button" class="border border-gray-300/60 rounded-e-md relative">
                <span class="absolute text-[16px] text-gray-500 left-[10px] top-[6px] pointer-events-none">
                    <i class="bi bi-search"></i>
                </span>
                <input class="input-search-box" type="text" placeholder="Search...">
            </button>
        </div>

        <!--    카테고리: 레이블/마일스톤 이동버튼 패널   -->
        <div class="flex justify-between items-center">
            <LabelMilestoneNavTab />
            <button type="button" class="btn issue create max-h-[44px] h-lvh" on:click={() => router.goto("/issues/add")}>
                <span>
                    <i class="bi bi-plus-lg"></i>
                </span>
                이슈 작성
            </button>
        </div>
    </div>
</div>


<!--  이슈 테이블  -->
<div class="flex flex-col [&>div:first-child] w-full min-w-[1020px]">

    <!-- 테이블 헤더 -->
    <div class="issue-table-header">
        <div class="checkbox">
            <input type="checkbox" class="parent-checkbox">
        </div>
        <div class="issue-filter open on:click={() => optionsStore.toggleIsOpenOption('isOpen')}">
            <span class="pr-[3px]">
                <i class="bi bi-exclamation-circle"></i>
            </span>
            열린 이슈(2)
        </div>
        <div class="issue-filter closed on:click={() => optionsStore.toggleIsOpenOption('isClosed')}">
            <span class="pr-[3px]">
                <i class="bi bi-archive"></i>
            </span>
            닫힌 이슈(0)
        </div>
        <!--     드롭다운 버튼       -->

        <!-- 테이블 헤더 필터 -->
        <IssueFilterOptions />
    </div>

    <!-- 이슈 리스트 생성 -->
    {#each $issues.issueList as issue, index}
        <Issue {issue} />
    {/each}

    <!-- 이슈가 하나도 없을 때 이슈 없음을 나타내는 컴포넌트 -->
    {#if $issues.issueList.length === 0}
        <NoIssue />
    {/if}

    {#if $loadingIssue }
        <IssueLoading />
    {/if}

</div>
