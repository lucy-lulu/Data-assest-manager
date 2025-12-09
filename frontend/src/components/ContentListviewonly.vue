<template>
    <div class="content-list">
  
      <!-- Sort Dropdown at the top-right corner -->
    <div class="sort-dropdown">
      <select v-model="selectedSort" @change="handleSortChange" class="sort-select">
        <option value="">Sort by: Relevance</option>
        <option value="Date">Sort by: Date</option>
        <option value="visitCount">Sort by: Views</option>
      </select>
    </div>
  
  
    <!-- 列表内容 -->
    <div class="list-item" v-for="(item, index) in paginatedData" :key="index">
  
      <div class="content-box">
          <!-- New div for image + view -->
          <div class="view-image-box">
            <img :src="require(`@/assets/view.png`)" alt="View Image" />
            <span>{{ item.visitCount }}</span>
          </div>
  
        <!-- 第一列：正方形图片 -->
        <div class="image-column">
          <img :src="getImageSrc(item.format)" alt="Asset Image" />
        </div>
        <!-- 第二列：Asset信息 -->
        <div class="info-column">
          <div>AssetName: {{ item.assetName }}</div>
          <div :class="{'highlight': isMatch(item.format, 'Format')}">Format: {{ item.format }}</div>
          <div :class="{'highlight': isMatch(item.subjectNumber, 'SubjectNumber')}">Subject: {{ item.subjectNumber }}</div>
          <div>Author: {{ item.author }}</div>
  
        </div>
        <!-- 第三列：作者信息 -->
        <div class="info-column">
          <div>Tag: {{ item.tag }}</div>
          <div>CreateTime: {{ item.createTime.substring(0, 10) }}</div>
          <div :class="{'highlight': isMatch(item.studentType, 'StudentType')}">Course Type: {{ item.studentType }}</div>
        </div>
        <!-- 第四列：查看按钮 -->
        <div class="button-column">
          <button @click="viewAsset(item.assetId)">VIEW</button>
        </div>
      </div>
    </div>
  
  
      <!-- 分页组件 -->
      <PaginationComponent
        :current-page="currentPage"
        :total-pages="totalPages"
        @change-page="changePage"
      />
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import PaginationComponent from './PaginationComponent.vue';
  
  export default {
    components: {
      PaginationComponent,
    },
    props: {
      searchQuery: String,
      filters: Object,
    },
    data() {
      return {
        currentPage: 1,
        itemsPerPage: 10,
        data: [],
        selectedSort: ""
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.data.length / this.itemsPerPage);
      },
      paginatedData() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
  
        //数据为空的情况下default展示
        return this.data.slice(start, start + this.itemsPerPage).map(item => ({
          ...item,
          subjectNumber: item.subjectNumber || 'SWEN90014',
          studentType: item.studentType || 'Undergraduate'
          }));
        },
      },
    methods: {
      viewAsset(assetId) {
        //this.$router.push({ name: 'AssetInfo', params: { id:assetId } });
        // const paramValue = 'subteacher'; //传递角色参数到asset details页面
        // this.$router.push({ name: 'AssetInfo', params: { id:assetId, auth: paramValue} });
        this.$router.push({ name: 'AssetInfo', params: { id:assetId } });
      },
      async fetchData() {
      try {
        console.log('Fetching data with query:', this.searchQuery);
        let queryParams = ''; 
  
  
        if (this.searchQuery) {
  
          // Determine if the query is for tag or author
          queryParams = this.isAuthorSearch(this.searchQuery)
            ? `?author=${this.searchQuery}`
            : `?keyword=${this.searchQuery}`;
        }
  
        // Add sortBy parameter if selected
        if (this.selectedSort) {
          queryParams += `${queryParams ? '&' : '?'}order=desc&sortBy=${this.selectedSort}`;
        }
  
        // Add filters to query params
        for (const [key, value] of Object.entries(this.filters)) {
            if (value.length > 0) {
              if (key === 'Format') {
              queryParams += `${queryParams ? '&' : '?'}format=${value.join(',')}`;
            } else if (key === 'StudentType') {
              queryParams += `${queryParams ? '&' : '?'}studentType=${value.join(',')}`;
            } else if (key === 'SubjectNumber') {
              queryParams += `${queryParams ? '&' : '?'}subjectNumber=${value.join(',')}`;
            } else {
              // Default behavior for other filters
              queryParams += `${queryParams ? '&' : '?'}${key.toLowerCase()}=${value.join(',')}`;
            }
              //queryParams += `${queryParams ? '&' : '?'}${key.toLowerCase()}=${value.join(',')}`;
            }
          }
  
        // const response = await axios.get(`api/asset/search${queryParams}`);
  
        // 添加 token 到请求头
        const response = await axios.get(`api/asset/search${queryParams}`, {
            headers: {
              token: localStorage.getItem("token")
              // token: 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoyLCJpZCI6IjEiLCJlbWFpbCI6InRlYWNoZXJAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6InRlYWNoZXJVc2VyIiwiZXhwIjoxNzI3ODExOTUyfQ.YOBjMdTZ97g6DRtsPlbg6Ustx5kyIdp8Pyzf0CVFQng',
            },
          });
  
        this.data = response.data.data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
  
      //console.log('Fetching data with query:', this.searchQuery);
    },
      handleSortChange() {
        this.fetchData(); // Refetch data with the selected sort option
    },
      changePage(page) {
        this.currentPage = page;
        this.fetchData();
      },
      isAuthorSearch(query) {
      // Simple heuristic: if it's a word starting with "tag", treat it as a tag search
      // 定义一个包含需要匹配的关键词的数组
        const keywords = ['john', 'lily', 'andrew', 'merly', 'james', 'mark'];
  
        return keywords.some(keyword => query.includes(keyword));
       //return query.startsWith('desi');
      },
      isMatch(value, filterType) {
        // Check if the value matches any selected filter
        return this.filters[filterType] && this.filters[filterType].includes(value);
      },
      getImageSrc(format) {
        if (format === 'Image'|| format === 'image') {
          return require('@/assets/icon.jpeg');
        } else if (format === 'Video' || format === 'video') {
          return require('@/assets/video.png');
        } else if (format === 'Document') {
          return require('@/assets/doc.png');
        } else {
          return require('@/assets/pdf.png'); 
        }
      }
    },
  watch: {
    searchQuery(){
      console.log('QUERY WATCH:', this.searchQuery);
      this.fetchData();
    },
    filters: {
        deep: true,
        handler() {
          console.log('FILTER WATCH:', this.filters);
          this.fetchData();
        }
      }
    },
    created() {
      this.fetchData();
  }
  };
  </script>
    
    <style scoped>
  
  .sort-dropdown {
    text-align: right;
    margin-bottom: 20px;
  }
  
  .sort-select {
    padding-bottom: 5px;
    background-color: white;
    border: 1px solid #ccc;
    border-radius: 5px;
    cursor: pointer;
    height:30px;
  }
  
    .content-list {
      margin-top: 50px; 
      flex:1;
      justify-content: center;
    }
    
    .list-item {
      display: flex;
      padding: 20px 0; 
      font-size: 16px;
    }
    
    .content-box {
    display: flex;
    justify-content: center;
    width: 800px;
    height: 150px;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    padding: 10px;
    margin-left: 200px;
  }
  
  .image-column {
    width: 100px;
    height: 100px;
    margin-right: 30px;
    margin-top: 30px;
  }
  
  .image-column img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 5px;
  }
  
  .info-column {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }
  
  .button-column {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .button-column button {
    padding: 10px 20px;
    background-color: #0056b3;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .button-column button:hover {
    background-color: #2970c5;
  }
  
  .highlight {
    font-weight: bold;
  }
  
  .view-image-box {
    position: absolute;
    top: 10px;
    right: 20px;
    display: flex;
    align-items: center;
    gap: 5px;
  }
  
  .view-image-box img {
    width: 25px;
    height: 25px;
    border-radius: 50%;
  }
  
  /* Other existing styles */
  .content-box {
    position: relative;
  }
  
  </style>
  