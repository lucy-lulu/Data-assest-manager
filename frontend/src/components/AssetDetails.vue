<template>
  <div class="asset-details">
    <div class="asset-left">
      <div class="image">
        <div v-if="asset.format.toLowerCase() === 'pdf'">
          <img src='@/assets/document.jpg' alt="file Image" width="300px" height="350px">
        </div>
        <div v-else-if="asset.format.toLowerCase() === 'audio'">
          <audio :src="asset.url" controls></audio>
        </div>
        <div v-else-if="asset.format.toLowerCase() === 'video'">
          <video :src="asset.url" controls width="100%" height="350px"></video>
        </div>
        <div v-else-if="asset.format.toLowerCase() === 'image'">
          <img src='@/assets/icon.jpeg' alt="Logo Image" width="300px" height="350px">
        </div>
        <div v-else-if="asset.format === ''">
          <img src='@/assets/icon.jpeg' alt="Logo Image" width="300px" height="350px">
        </div>
      </div>
      <div class="tags">
        <div v-if="asset.tag != ''" class="tag">
          <img src='@/assets/tag.png' class="icon">
          <h3><span class="text">{{  asset.tag  }} </span></h3>
        </div>
      </div>
  </div>
  <div class="asset-right">
    <div class="asset-info">
      <h2>{{  asset.assetName }}</h2>
      <p class="paragraph"><strong>Access Link:</strong> <br><a :href="asset.canvasLink">{{ asset.canvasLink }}</a></p>
    </div>
    <div class="file-info">
      <div class="title">
        <h3>Asset Information</h3>
      </div>
      <div class="info">
        <div class="info-left">
          <p class="info-text"><strong>Description</strong><br>{{ asset.description }}</p>
          <p class="info-text"><strong>Subject Number:</strong><br>{{asset.subjectNumber}}</p>
          <p class="info-text"><strong>File</strong><br><a :href="asset.url">{{ asset.url }}</a></p>
        </div>
        <div class="info-right">
          <p class="info-text"><strong>Authors:</strong><br>{{ asset.author }}</p>
          <p class="info-text"><strong>Student Type:</strong><br> {{asset.studentType}}</p>
          <p class="info-text"><strong>Format:</strong><br> {{ asset.format }}</p>
        </div>
      </div>
    </div>
  </div>
</div>
  </template>
  
  <script>
  import axios from 'axios';

  export default {
    name: 'AssetDetails',
    data() {
      return {
        asset: null,  // 存储资产详情
      };
    },
    mounted() {
      this.getAssetDetails();
    },
    methods: {
      getAssetDetails() {
        const assetId = Number(this.$route.params.id);
        axios.get(`/api/asset/${assetId}`,{
          headers: {
            token: localStorage.getItem("token")
          },
        })
            .then(response => {
              if (response.data.code === 1) {
                this.asset = response.data.data;
              }
            })
            .catch(error => {
              console.error("Error fetching asset details:", error);
            });
      }
    }
  };
  </script>

<style scoped>
.asset-view{
  display: flex;
}
.asset-details {
  display: flex;
  width: 1100px;
  height: 600px;
  background-color: rgb(253, 253, 253);
  overflow-y: hidden;
}

.asset-left {
  width:300px;
  height:100%;
  margin-top: 70px;
}
.image {
  width: 100%;
  height: 350px;
}

.tags{
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 100%;
  margin-top: 10px;
}
.tag{
  display: flex;
  align-items: flex-start;
  margin-top: 10px;
  margin-left: 15px;
}
.icon {
  margin-top: 20px;
  width: 35px;
  height: 25px;
  display: flex;
  margin-bottom: 5px;
}
.text {
  margin-left: 15px;
  margin-top: 5px;
}

.asset-right {
  display: flex;
  margin-top: 70px;
  margin-left: 20px;
  width:700px;
  height: 100%;
  flex-direction: column;
}
.asset-info {
  margin-left: 30px;
  height: 150px;
}
.asset-info h2 {
  font-size: 26px;
  margin-top: 20px;
}
.paragraph{
  font-size: 18px;
  margin-top: 30px;
  margin-bottom: 15px;
}

.file-info{
  height: 400px;
}
.title{
  height: 30px;
  margin-left: 30px;
  margin-top: 20px;
}
.info{
  display: flex;
  height: 300px;
}
.info-left{
  margin-left: 30px;
  width: 200px;
}
.info-text{
  width:auto;
  font-size: 18px;
  margin-top:20px;
  margin-bottom:30px;
}

</style>
