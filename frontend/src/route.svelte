script>
    import {Route} from 'tinro';
</script>

<Route path="/" redirect="/issues" />
<Route path="/issues/*" />
<Route path="/labels/*" />
<Route path="/milestones/*" />