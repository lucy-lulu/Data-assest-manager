<template>
  <div class="asset-viewer">
    <PageHeader />
    <div class="asset-content">
      <AddAsset :key="currentIndex" :assets-data="assetsList[currentIndex]" @updateAsset="updateAssetInList" @createAsset="removeFromList"/>

      <div class="navigation-buttons">
        <button @click="prevAsset" :disabled="currentIndex === 0">Last one</button>
        <button @click="nextAsset" :disabled="currentIndex === assetsList.length - 1" >Next one</button>
      </div>
    </div>
    <SideBar/>
  </div>
</template>

<script>
import SideBar from '@/components/ToolBar.vue';
import PageHeader from '@/components/PageHeader.vue';
import AddAsset from "@/components/AddAsset.vue";

export default {
  name: 'AssetAddition',
  components: {
    AddAsset,
    SideBar,
    PageHeader
  },
  data() {
    return {
      assetsData: [],
      currentAsset:{},
      currentIndex:0,
      urlFetch: '',
      urlCount: 0,
      assetsList:[]
    };
  },
  created() {
    const { assets, urlFetch, urlCount } = this.$route.query;

    if (assets) {
      try {
        this.assetsData = JSON.parse(assets);
      } catch (error) {
        console.error('Error parsing assets data:', error);
      }
    }
    this.urlFetch = urlFetch || '';
    this.urlCount = urlCount || 0;

    console.log(this.assetsData[this.currentIndex]);
    for (let i = 0; i < this.assetsData.length; i++) {
      const newAsset = {
        description: this.assetsData[i].description,
        assetName: this.assetsData[i].assetName,
        canvasLink: this.assetsData[i].url,
        format: this.assetsData[i]["content-type"],
        tag: "",
        author: "",
        file: "",
        studentType: "",
        subjectNumber: "",
      };
      this.assetsList.push(newAsset);
    }

    console.log(this.assetsList);
  },

  methods: {
    updateAssetInList(updatedAsset) {
      console.log('Updated asset received from child:', updatedAsset);
      // this.assetsList.splice(this.currentIndex, 1, updatedAsset);
      this.$set(this.assetsList, this.currentIndex, updatedAsset);
    },
    removeFromList(newAsset) {
      console.log(newAsset);
      this.assetsList.splice(this.currentIndex,1);
      console.log(this.assetsList.length);
      if(this.assetsList.length === 0){
        alert("all assets have created successfully");
        this.$router.push('/login');
      }else{
        this.flash();
      }
    },
    flash(){
      this.currentIndex--;
      this.$nextTick(() => {
        this.currentIndex++;
      });
    },
    prevAsset() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
      }
    },
    nextAsset() {
      if (this.currentIndex < this.assetsList.length - 1) {
        this.currentIndex++;
      }
    }

  }
};
</script>

<style scoped>
.asset-viewer {
  display: flex;
  height: 900px;
  background-color:white;
}

.asset-content {
  height: 100%;
}

html, body {
  height: 100%;
  margin: 0;
  overflow-y: hidden;
}



</style>
