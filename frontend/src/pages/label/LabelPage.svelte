<script>
    import { labels } from "../../stores/label.js";
    import LabelList from "../../components/label/LabelList.svelte";
    import LabelAddForm from "../../components/label/LabelAddForm.svelte";
    import { onMount } from "svelte";

    let addMode = false;

    onMount(() => {
        labels.fetchLabels();
    });

    function toggleAddMode() {
        labels.toggleAddModeLabel()
    }
</script>

<div class="label-page">
    <h1>레이블</h1>
    <button on:click={toggleAddMode} class:addMode={$labels.addMode}>
        레이블 추가
    </button>

    {#if $labels.addMode}
        <LabelAddForm />
    {/if}

    <LabelList />
</div>

<style>
    .label-page {
        padding: 20px;
    }

    button {
        margin: 10px 0;
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: opacity 0.3s;
    }

    button:hover {
        background-color: #0056b3;
    }

    button.addMode {
        opacity: 0.5;
    }
</style>
