<template>
  <div class="asset-view">
    <PageHeader />
    <AssetActions :assetId=this.$route.params.id :role="role"/>
    <AssetDetails/>
    <SideBar :role="role"/>
  </div>
</template>

<script>
import axios from 'axios';
// import SideBar from "@/components/ToolBar.vue";
import SideBar from "@/components/SideBar.vue";
import PageHeader from "@/components/PageHeader.vue";
import AssetActions from "@/components/AssetActions.vue";
import AssetDetails from "@/components/AssetDetails.vue";
import {decodeJwt} from "jose";

export default {
  name: 'AssetInfo',
  components: {AssetDetails,AssetActions, SideBar,PageHeader},
  props: {
    assetId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      asset: null,
      role: null
    };
  },
  mounted() {
    const token = localStorage.getItem("token");
    console.log(token);
    if (token) {
      try {
        const decoded = decodeJwt(token);
        this.role = decoded.role; // 解析后从 token 中获取角色信息
        console.log(this.role);
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
    this.getAssetDetails();

  },
  methods: {
    getAssetDetails() {
      const assetId = Number(this.$route.params.id);
      console.log(assetId);
      axios.get(`/api/asset/${assetId}`,{
        headers: {
          // token: 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoyLCJpZCI6IjEiLCJlbWFpbCI6InRlYWNoZXJAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6InRlYWNoZXJVc2VyIiwiZXhwIjoxNzI3ODExOTUyfQ.YOBjMdTZ97g6DRtsPlbg6Ustx5kyIdp8Pyzf0CVFQng',
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
</style>
