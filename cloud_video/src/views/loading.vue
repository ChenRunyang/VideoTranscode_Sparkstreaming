<template>
  <div class="loading">
    <img
      src="../assets/loading.gif"
      width='40'
      height='40'
    >
    <p class="desc">{{title}}</p>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "正在载入"
    }
  }
};
</script>


<style scoped>
.loading {
  width: 100%;
  text-align: center;
}

.loading .desc {
  line-height: 20px;
  font-size: 20px;
  color: rgb(75, 28, 184);
}
</style>