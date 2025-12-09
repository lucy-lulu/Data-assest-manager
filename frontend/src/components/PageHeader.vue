<template>
  <div class = "header">
    <div class =logo-header>
      <h3 class="logo">MSPACE CATEDLOG</h3>
    </div>
    <div class="page-header">
      <button @click="goBack" class="back-button">BACK</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PageHeader',
  props:{
    toDashboard: {
      required: false
    },
  },
  methods: {
    goBack() {
      if(this.toDashboard === null){
        this.$router.go(-1);
        // go to last page.
      }
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.header {
  display: flex;
  position: fixed;
}

.logo-header{
  display: flex;
  justify-content: center;
  align-items: center;
  height:50px;
  width:310px;
  background-color: #1E2A50;
}
.logo{
  color: white;
}

.page-header {
  position: fixed;
  top: 0;
  margin-left: 310px;
  background-color:white;
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-button {
  background-color: #1E2A50;
  color: white;
  width: 90px;
  height: 35px;
  border: none;
  border-radius: 5px;
  margin-left: 30px;
  cursor: pointer;
  font-size: 14px;
}
.back-button:hover {
  background-color: #16395B;
}

</style>
