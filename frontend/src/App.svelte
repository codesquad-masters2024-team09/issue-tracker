<script>
  import {Route} from "tinro";
  import IssueAddPage from "./pages/issue/IssueAddPage.svelte";
  import Header from "./components/common/Header.svelte";

</script>

<Header />
<div id="container">
  <Route path="/issues/add">
    <IssueAddPage />
  </Route>
</div>