<script>
  import LabelPreview from "./LabelPreview.svelte";

  let labels = [
    {
      labelId: "Label",
      description: "레이블에 대한 설명",
      textColor: "#000000",
      colorCode: "#FEFEFE",
    },
    {
      labelId: "documentation",
      description: "서비스에 대한 개선 사항 혹은 추가 사항",
      textColor: "#FFFFFF",
      colorCode: "#0025E6",
    },
    {
      labelId: "bug",
      description: "서비스에서 발생하는 오류들",
      textColor: "#FFFFFF",
      colorCode: "#FF3B30",
    },
  ];
</script>

<div class="label-list-box">
  <div class="label-list">
    <h3>{labels.length}개의 레이블</h3>
  </div>
  <div class="label-element">
    {#each labels as label}
      <LabelPreview {label} />
    {/each}
  </div>
</div>

<style>
  h3 {
    font-size: 14px;
    line-height: 24px;
    margin: 0px;
  }
  .label-list-box {
    border: 1px solid #d9dbe9;
    border-radius: 16px;
  }
  .label-list {
    border-color: transparent;
    border-radius: 16px 16px 0 0;
    padding: 16px;
    background: #f7f7fc;
  }
</style>
