<script>
    import { Route } from 'tinro';

    import Issues from './pages/issue/Issues.svelte';
    import IssueDetail from './pages/issue/IssueDetailPage.svelte';
    import IssueAdd from './pages/issue/IssueAddPage.svelte';
    import LabelPage from './pages/label/LabelPage.svelte';
    import MilestonePage from './pages/milestone/MilestonePage.svelte';
    import LoginPage from './pages/auth/AuthPage.svelte';
    import NotFound from './pages/NotFound.svelte';

    // 여기에 정의된 경로 외에는 모두 NotFound 페이지로 연결됩니다.
    const validPaths = ['/login', '/', '/issues', '/issues/add', '/labels', '/milestones'];

    // 현재 경로가 유효한지 확인하는 함수
    function isValidPath(path) {
        return validPaths.includes(path);
    }
</script>

<Route path="/login"><LoginPage /></Route>
<Route path="/" redirect="/issues" />
<Route path="/issues"><Issues /></Route>
<Route path="/issues/:issueId" let:params>
    {#if /^\d+$/.test(params.issueId)}
        <IssueDetail />
    {:else if /add/.test(params.issueId)}
        <IssueAdd />
    {:else}
        <NotFound />
    {/if}
</Route>
<Route path="/labels"><LabelPage /></Route>
<Route path="/milestones"><MilestonePage /></Route>

{#if !isValidPath(window.location.pathname)}
    <NotFound />
{/if}
