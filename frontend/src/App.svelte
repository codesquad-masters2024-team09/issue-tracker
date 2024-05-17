<script>
  import {Route} from "tinro";
  import IssueAddPage from "./pages/issue/IssueAddPage.svelte";
  import Header from "./components/common/Header.svelte";
  import IssueDetailPage from "./pages/issue/IssueDetailPage.svelte";

</script>

<Header />
<div id="container">
  <Route path="/issues/*" firstmatch>
    <Route path="/add"><IssueAddPage/></Route>
    <Route path="/:issueId"><IssueDetailPage/></Route>
  </Route>
</div>