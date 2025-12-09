<template>
  <div class="main-container">
    <div class="filter-container">
      <!-- CollectFilter component to manage folder operations -->
      <CollectFilter @refreshCategories="getCategories" :categories="categories" />
    </div>
    <div class="content-container">
      <button class="back" @click="goBack">Back</button>
      <div class="scrollable-content">
        <!-- CollectList to show filtered asset list -->
        <CollectList :search-query="searchQuery" :filters="filters" />
      </div>
    </div>
    <Sidebar :role="role"/>
  </div>
</template>

<script>
import Sidebar from '@/components/SideBar.vue';
import CollectList from '@/components/CollectList.vue';
import CollectFilter from '@/components/CollectFilter.vue';
import axios from 'axios';
import { decodeJwt } from 'jose';

export default {
  components: {
    CollectFilter,
    Sidebar,
    CollectList
  },
  data() {
    return {
      role:null,
      userID:null,
      searchQuery: '', // Search query to be passed to CollectList
      filters: {}, // Filters for category and other parameters
      categories: [] // Categories fetched from API
    };
  },
  mounted() {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = decodeJwt(token);
        this.role = decoded.role; // 解析后从 token 中获取角色信息
        this.userID = decoded.id;
        console.log(this.userID);
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
    this.getCategories();
  },
  methods: {
    getAuthHeader() {
      const token = localStorage.getItem('token');
      if (token) {
        return {
          headers: {
            token: `${token}`,
          },
        };
      } else {
        // If token is missing, redirect to login or handle error
        console.error("No token found, redirecting to login.");
        this.$router.push('/'); // Adjust the route as needed
      }
    },
    // Go back to the previous page
    // *******
    // goBack() {
    //   const currentPath = this.$route.path;
    //   const newPath = currentPath.replace('assetCollection', '');
    //
    //   if (currentPath !== newPath) {
    //     this.$router.push(newPath);
    //   }
    // },

    goBack() {
      this.$router.push('/SuperTeacherDashboard');
      // go to last page.
    },
    // Fetch the list of categories from the API
    async getCategories() {
      try {
        const response = await axios.get('/api/category/teacher', {
          params: { teacherId: this.userID }, // Assuming teacherId is 1, update if necessary
          headers: {
            token: localStorage.getItem("token")
          },
        });

        if (response.data.code === 1) {
          this.categories = response.data.data;
        } else {
          console.error('Error fetching categories(not exit):', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    }
  }
};
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  background-color: #ffffff;
}

.main-container {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}

.filter-container {
  flex: 0 0 auto;
  margin-right: 70px;
}

.content-container {
  flex: 1;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.scrollable-content {
  margin-top: 80px;
  height: calc(100vh);
  width: 70%;
  display: flex;
  justify-content: center;
}

.back {
  background-color: #094183;
  color: white;
  width: 80px;
  height: 35px;
  margin: 10px;
  border-radius: 8px;
  font-size: 14px;
  position: fixed;
  top: 15px;
  left: 280px;
}

</style>
