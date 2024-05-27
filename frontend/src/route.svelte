<script>
    import { Route } from 'tinro';

    import Issues from './pages/issue/Issues.svelte';
    import IssueDetail from './pages/issue/IssueDetailPage.svelte';
    import IssueAdd from './pages/issue/IssueAddPage.svelte';
    import LabelPage from './pages/label/LabelPage.svelte';
</script>

<Route path="/" redirect="/issues" />
<Route path="/issues"><Issues /></Route>
<Route path="/issues/*" firstmatch>
    <Route path="/add"><IssueAdd/></Route>
    <Route path="/:issueId"><IssueDetail/></Route>
</Route>
<Route path="/labels/*" ><LabelPage /></Route>
<Route path="/milestones/*" />
<!-- <Route fallback><NotFound /></Route> -->
