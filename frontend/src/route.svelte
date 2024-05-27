<script>
    import { Route } from 'tinro';

    import Issues from './pages/issue/Issues.svelte';
    import IssueDetail from './pages/issue/IssueDetailPage.svelte';
    import IssueAdd from './pages/issue/IssueAddPage.svelte';
    import IssueListPage from './pages/issue/IssueListPage.svelte';
</script>

<Route path="/" redirect="/issues" />
<Route path="/issues"><Issues /></Route>
<Route path="/issues/*" firstmatch>
    <Route path="/add"><IssueAdd/></Route>
    <Route path="/tmp"><IssueListPage /></Route>
    <Route path="/:issueId"><IssueDetail/></Route>
</Route>
<Route path="/labels/*" />
<Route path="/milestones/*" />
<!-- <Route fallback><NotFound /></Route> -->
