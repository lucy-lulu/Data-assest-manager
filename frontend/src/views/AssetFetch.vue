<template>
  <div class="asset-view">
    <PageHeader />
    <div class="left-view"></div>
    <div class="right-view">
      <form @submit.prevent="fetchAssets">
      <div class="search-bar">
        <h3>Please enter your token first.</h3>
        <input v-model="token" class="token-input" placeholder="Enter your Canvas token…" required/>
        <h3>You can add up to 10 assets in a batch.</h3>
        <div class="search-box" v-for="(url, index) in urls" :key="index">
          <input v-model="urls[index]" placeholder="Enter the asset URL…" required/>
<!--          <input v-model="tokens[index]" placeholder="Enter your Canvas token…" required/>-->
        </div>
        <div class="search-button">
          <button type="button" @click="addInput" v-if="urls.length < 10">Add More</button>
          <button type="submit">FETCH</button>
        </div>
      </div>
      </form>
    </div>
    <SideBar/>
  </div>
</template>

<script>
import axios from "axios";
import SideBar from '@/components/ToolBar.vue';
import PageHeader from '@/components/PageHeader.vue';

export default {
  name: 'AssetFetch',
  components: {
    SideBar,
    PageHeader
  },
  data() {
    return {
      urls: [""],
      // tokens: [""],
      token: ""
    }
  },
  methods: {
    addInput() {
      if (this.urls.length < 10) {
        this.urls.push("");
        // this.tokens.push("");
      }
    },
    async fetchAssets() {
      const assetResponses = [];
      const invalidAssetResponses = [];
      for (let i = 0; i < this.urls.length; i++) {
        try {
          const response = await axios.get('/api/asset/fetchAsset', {
            params: {
              url: this.urls[i],
              token: this.token
            },
            headers: {'token': localStorage.getItem('token')}
          });
          if (response.data.code === 1) {
            assetResponses.push(JSON.parse(response.data.data));
          }
        } catch (error) {
          console.error('Error fetching asset:', error);
          invalidAssetResponses.push({url: this.urls[i], valid: false, data: null});
        }
      }
      if(invalidAssetResponses.length == this.urls.length){
        alert("All the urls you have entered are invalid!");
        this.resetForm();
      }
      else{
        const urlCount = assetResponses.length;
        const urlFetch = this.urls.length;
        await this.$router.push({
          name: 'assetAddition',
          query: {
            assets: JSON.stringify(assetResponses),
            urlFetch: urlFetch,
            urlCount: urlCount
          },
        });
      }
    },
    resetForm() {
      this.urls = [''];
      // this.tokens = [''];
    }
  }
    /* 添加单一url和token
    async fetchAsset() {
      try {
        const response = await axios.get('/api/asset/fetchAsset', {
          params:{
            url: this.url,
            token: this.token
          },
          headers:{'token' : localStorage.getItem('token') }
        })
        if (response.data.code === 1) {
          const assetData = JSON.parse(response.data.data);

          await this.$router.push({
            name: 'assetAddition',
            query: {
              assetName: assetData.assetName,
              description: assetData.description,
              url: assetData.url,
              contentType: assetData['content-type'],
              thumbnailUrl: assetData.thumbnail_url
            },
          });
        }
      } catch (error) {
        console.error('Error fetching asset:', error);
      }
    },
  */
};
</script>

<style scoped>
.asset-view{
  height: auto;
  display: flex;
  overflow-y: hidden;
}
.left-view{
  background-color: rgba(251,241,251,0.27);
  width:310px;
  height:100%
}
.right-view{
  width:1100px;
  height:100%;
}
.search-bar{
  background-color: #1E2A50;
  margin-top: 100px;
  margin-left: 50px ;
  display: flex;
  flex-direction: column;
  height:100%;
  width: 950px;
  border-radius: 8px;
  text-align: center;
  color: white;
}
.search-box{
  width: 750px;
  display: flex;
  flex-direction: column;
}
.search-box input {
  font-size: 16px;
  border: 2px solid #1c1f4f;
  border-right: none;
  border-radius: 8px;
  width: 850px;
  margin-left: 40px;
  margin-top: 12px;
  height:32px;
}

.search-button{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 15px;
  margin-bottom: 10px;
}
.search-button button{
  width: 175px;
  height: 50px;
  background-color: #1E2A50;
  color: white;
  font-size: 18px;
  border: 2px solid white;
  border-radius: 10px;
  margin-right: 10px;
}
.search-button button:hover{
  background-color: #0056b3;
}
.warning-message {
  color: red;
  font-weight: bold;
  text-align: center;
}

.token-input {
  font-size: 16px;
  border: 2px solid #1c1f4f;
  border-right: none;
  border-radius: 8px;
  width: 850px;
  margin-left: 40px;
  margin-top: 12px;
  height: 32px;
}

</style>