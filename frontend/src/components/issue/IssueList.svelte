<script>
  import {onMount} from "svelte";
  import {handleCheckboxes} from "../../service/buttonSelection.js";
  import {router} from "tinro";
  import IssueFilterOptions from "./FilterOptions.svelte";
  import {optionsStore} from "../../stores/filter.js";

  let issuess = []

  const MOCK_ISSUE_LIST_DATA = {
      openedIssueCount: 4,
      closedIssueCount: 5,
      labelCount: 1,
      milestoneCount: 2,
      issues: [
          {
              issueId: 1,
              memberId: "testUser1",
              title: "예시 이슈 1번",
              isOpen: true,
              labels: [
                  {
                      labelId: "fix",
                      colorCode: "#FF3B30",
                      textColor: "#FEFEFE",
                  },
                  {
                      labelId: "feature",
                      colorCode: "#0025E6",
                      textColor: "#FEFEFE"
                  }
                  ],
              milestoneName: "마일스톤 1번",
              dueDate: "3초 전",
              createdAt: "3초",
          },
          {
              issueId: 2,
              memberId: "testUser2",
              title: "예시 이슈 2번",
              isOpen: true,
              labels: [
                  {
                      labelId: "bug",
                      colorCode: "#FF3B30",
                      textColor: "#FEFEFE",
                  },
                  {
                      labelId: "feature",
                      colorCode: "#0025E6",
                      textColor: "#FEFEFE",
                  }
              ],
              milestoneName: "마일스톤 2번",
              createdAt: "1일",
          },
      ]
  }

  onMount(() => {
      const parentCheckbox = document.querySelector(".parent-checkbox");
      const childCheckboxes = document.querySelectorAll(".child-checkbox");
      if (parentCheckbox && childCheckboxes.length > 0) {
          handleCheckboxes(parentCheckbox, childCheckboxes);
      }
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
      <div class="issue-filter open on:click={() => optionsStore.toggleIsOpenOption("isOpen")}">
          <span class="pr-[3px]">
              <i class="bi bi-exclamation-circle"></i>
          </span>
          열린 이슈(2)
      </div>
      <div class="issue-filter closed on:click={() => optionsStore.toggleIsOpenOption("isClosed")}">
          <span class="pr-[3px]">
              <i class="bi bi-archive"></i>
          </span>
          닫힌 이슈(0)
      </div>
      <!--     드롭다운 버튼       -->

      <!-- 테이블 헤더 필터 -->
      <IssueFilterOptions />
  </div>

  <!-- 테이블 콘텐츠 (중간요소) -->
  {#if issuess}
    {#each issuess as issue}
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
                  <a href="{issue.issueId}">{issue.title}</a>
                  <!-- 레이블 뱃지 -->
                  {#each issue.labels as label}
                    <div class="label-badge-container">
                        <div class="label-badge border border-gray-200 bg-[{label.colorCode}] text-[{label.textColor}]">
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
              <div>
                  <span class="text-[16px]">
                      <i class="bi bi-signpost"></i>
                  </span>
                  {issue.memberId}
              </div>
          </div>
      </div>
    </div>
    {/each}
  {/if}

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
                <a href="#">이슈 제목</a>
                <div class="label-badge-container">
                    <div class="label-badge border border-gray-200 text-gray-500">
                        Label
                    </div>
                </div>
                <div class="label-badge-container">
                  <div class="label-badge border border-gray-200 bg-blue-700 text-[12px] text-white">
                    documentation
                </div>
              </div>
            </div>
        <!-- 이슈 번호 + 작성자 및 타임스탬프 정보 + 마일스톤 -->
        <div class="issue-writer-timestamp">
            <div>
                #이슈번호
            </div>
            <div>
                작성자 및 타임스탬프 정보
            </div>
            <div>
                <span class="text-[16px]">
                    <i class="bi bi-signpost"></i>
                </span>
                마일스톤
            </div>
        </div>
    </div>
    <div class="flex gap-5 ml-auto justify-center items-center">
      <a href="#" class="size-6">
          <img src="../../../../public/assets/profile_icon_duck.svg" alt="User Profile Icon" class="user-profile-icon"/>
      </a>
    </div>
  </div>

  <!-- 테이블 콘텐츠 (마지막 요소) -->
  <div class="issue-table-row">
      <div class="checkbox">
          <input type="checkbox" class="child-checkbox">
      </div>
      <!-- 제목 + 이슈 번호 + 작성자 + 마일스톤 정보 -->
      <div class="issue-content-container">
          <div class="issue-content-title">
              <span class="text-sm text-blue-700 pr-[6px]">
                  <i class="bi bi-exclamation-circle"></i>
              </span>
              <a href="#">FE 이슈트래커 개발</a>
              <div class="label-badge-container">
                  <div class="label-badge border border-gray-200 bg-blue-700 text-[12px] text-white">
                      documentation
                  </div>
              </div>
          </div>
          <div class="issue-writer-timestamp">
              <div>
                  #1
              </div>
              <div>
                  이 이슈가 8분 전, yelly님에 의해 작성되었습니다
              </div>
              <div>
                  <span class="text-[16px]">
                      <i class="bi bi-signpost"></i>
                  </span>
                  그룹프로젝트: 이슈트래커
              </div>
          </div>
      </div>
      <div class="filter-parent-items ml-auto">
        <!-- <div class="filter-btn-container">
          yelly
        </div>
        <div class="filter-btn-container">
          Label
        </div>
        <div class="filter-btn-container">
          그룹프로젝트: 이슈트래커
        </div> -->
        <div class="filter-btn-container flex gap-5 ml-auto justify-end items-center">
          <a href="#" class="size-6">
              <img src="../../../../public/assets/profile_icon.svg" alt="User Profile Icon" class="user-profile-icon"/>
          </a>
        </div>
      </div>
  </div>
</div>
