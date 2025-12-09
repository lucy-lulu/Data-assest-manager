<template>
  <div id = "app">
    <div class="main-container">
      <div class="filter-container">
        <FilterBar @filters-changed="updateFilters" />
      </div>
      <div class="content-container">
        <div class="search-box-container">
          <SearchBox @search="handleSearch" />
        </div>
        <div class="scrollable-content">
          <ContentList :search-query="searchQuery" :filters="filters" />
        </div>
      </div>
      <Sidebar :role="role"/>
    </div>
  </div>
</template>

<script>
import FilterBar from '@/components/FilterBar.vue';
import SearchBox from '@/components/SearchBox.vue';
import Sidebar from '@/components/ToolBar.vue';
import ContentList from '@/components/ContentList.vue';
import {decodeJwt} from "jose";


export default {
  name:'AdminDashboard',
  components: {
    FilterBar,
    SearchBox,
    Sidebar,
    ContentList
  },
  data() {
    return {
      searchQuery: '',
      filters: {},
      role: null
    };
  },
  mounted() {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = decodeJwt(token);
        this.role = decoded.role;
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
  },
  methods: {
    handleSearch(query) {
      console.log('Search query received in parent:', query);
      this.searchQuery = query;
    },
    updateFilters(newFilters) {
      console.log('Filter received in parent:', newFilters);
      this.filters = newFilters;
    }
  }
};
</script>

<style>
body, html, #app {
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
  padding: 0; 
  margin: 0; 
}


.filter-container {
  flex: 0 0 auto; 
  margin-right: 50px; 
}


.content-container {
  flex: 1; 
  /*display: fixed;*/
  display: flex;
  justify-content: center;
  flex-direction: column; 
  align-items: center; 
}

.search-box-container {
  width: 70%; 
  position: fixed; 
  top: 0; 
  left: 300px; 
  background-color: white; 
  padding: 10px 0; 
  z-index: 9999;
}

.sidebar-container {
  /*flex: 0 0 auto;*/
  margin-left: 20px; 
}

.scrollable-content {
  margin-top: 120px; 
  height: calc(100vh - 120px);
  /*height: calc 100vh;*/
  /*overflow-y: auto;*/
  width: 70%;
  display: flex;
  justify-content: center;
}

</style>


