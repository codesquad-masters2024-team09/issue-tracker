<script>
    import {onDestroy, onMount} from "svelte";
    import { get } from 'svelte/store';
    import { issues, loadingIssue, issuesPageLock } from '../../stores/issue';
    import { handleCheckboxes } from "../../service/buttonSelection.js";
    import { filters } from "../../stores/filter";
    import { router } from "tinro";
    import FilterBar from "./FilterBar.svelte";
    import Issue from './IssueItem.svelte';
    import NoIssue from "./NoIssue.svelte";
    import IssueLoading from "./IssueLoading.svelte";
    import LabelMilestoneNavTab from "../common/LabelMilestoneNavtab.svelte";

    onMount(() => {
        // 필터 상태 초기화
        filters.resetSelectedItems()
        filters.resetCheckedState()

        // checkbox multi select 부분
        const parentCheckbox = document.querySelector(".parent-checkbox");
        const childCheckboxes = document.querySelectorAll(".child-checkbox");
        if (parentCheckbox && childCheckboxes.length > 0) {
            handleCheckboxes(parentCheckbox, childCheckboxes);
        }

        let initQ = document.getElementById('q').value;
        filters.reloadSearchCondition()
        console.log('adsad', $filters.searchCondition)
        document.getElementById('q').value = $filters.searchCondition + ' '

        // 이슈 리스트 가져오기
        issues.resetIssues();
        issues.fetchIssues().then(() => {
            console.log('리스트 컴포넌트에서 받은 이슈 목록: ', get(issues).issueList)
        });
    })

    let userInput = ''
    $: userInput

    $: openIssues = $issues.issueList.filter(issue => issue.open === true)
    $: closedIssues = $issues.issueList.filter(issue => issue.open === false)

    $: isOpenIssueView = true
    const onOpenIssueView = async () => {
        if (isOpenIssueView) {
            return
        }

        filters.changeViewMode();
        await filters.searchIssueByCondition()
        isOpenIssueView = true
    }

    const onClosedIssueView = async () => {
        if (!isOpenIssueView) {
            return
        }

        filters.changeViewMode();
        await filters.searchIssueByCondition()
        isOpenIssueView = false
    }

    const onEnterKey = async (e) => {
        if (e.key === 'Enter') {
            filters.resetSelectedItems()
            filters.resetCheckedState()

            let query = document.getElementById('q').value;
            console.log(query)
            filters.updateUserInput(query)
            await filters.searchIssueByUserInput()
        }
    }
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
                <input id="q" class="input-search-box" type="text" placeholder="Search..."
                       on:keyup={onEnterKey} value={userInput}>
            </button>
        </div>

        <!--    카테고리: 레이블/마일스톤 이동버튼 패널   -->
        <div class="flex justify-between items-center">
            <LabelMilestoneNavTab />
            <button type="button" class="btn issue create min-w-[165px] max-h-[44px] h-lvh" on:click={() => router.goto("/issues/add")}>
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
        <button class="openCloseTab" on:click={onOpenIssueView}>
            <span class="pr-[3px]">
                <i class="bi bi-exclamation-circle"></i>
            </span>
            <span class={isOpenIssueView ? 'font-bold text-black' : ''}>열린 이슈({$issues.openIssues})</span>
        </button>
        <button class="openCloseTab" on:click={onClosedIssueView}>
            <span class="pr-[3px]">
                <i class="bi bi-archive"></i>
            </span>
            <span class={!isOpenIssueView ? 'font-bold text-black' : ''}>닫힌 이슈({$issues.closedIssues})</span>
        </button>

        <!-- 테이블 헤더 필터 -->
        <FilterBar />
    </div>

    <!-- 이슈 리스트 생성 -->
    {#if isOpenIssueView}
        {#each openIssues as issue, index}
            <Issue {issue} />
        {/each}
        <!-- 이슈가 하나도 없을 때 이슈 없음을 나타내는 컴포넌트 -->
        {#if openIssues.length === 0}
            <NoIssue isOpen={true} />
        {/if}
    {:else}
        {#each closedIssues as issue, index}
            <Issue {issue} />
        {/each}
        <!-- 이슈가 하나도 없을 때 이슈 없음을 나타내는 컴포넌트 -->
        {#if closedIssues.length === 0}
            <NoIssue isOpen={false} />
        {/if}
    {/if}

    {#if $loadingIssue }
        <IssueLoading />
    {/if}
</div>
