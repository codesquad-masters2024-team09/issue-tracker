<script>
  import {Route} from "tinro";
  import IssueAddPage from "./pages/issue/IssueAddPage.svelte";
  import Header from "./components/common/Header.svelte";
  import IssueDetailPage from "./pages/issue/IssueDetailPage.svelte";
  import IssueListPage from "./pages/issue/IssueListPage.svelte";

</script>

<Header />
<div id="container">
  <Route path="/">
    <IssueListPage />
  </Route>
  <Route path="/issues">
    <IssueListPage />
  </Route>
  <Route path="/issues/*" firstmatch>
    <Route path="/add"><IssueAddPage/></Route>
    <Route path="/:issueId"><IssueDetailPage/></Route>
  </Route>
</div>