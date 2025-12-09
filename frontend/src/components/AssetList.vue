<template>
    <div class="content-list">
      <!-- 标题行 -->
      <div class="list-header">
        <div class="header-item">ID</div>
        <div class="header-item">Title</div>
        <div class="header-item">Format</div>
        <div class="header-item">Author</div>
        <div class="header-item">Create Time</div>
        <div class="header-item">Tag</div>
        <div class="header-item">Actions</div>
      </div>
  
      <!-- 列表内容 -->
      <div class="list-item" v-for="(item, index) in paginatedData" :key="index">
        <div class="item-detail">{{ item.id }}</div>
        <div class="item-detail">
          <a :href="item.link" target="_blank">{{ item.title }}</a>
        </div>
        <div class="item-detail">{{ item.format }}</div>
        <div class="item-detail">{{ item.author }}</div>
        <div class="item-detail">{{ item.time }}</div>
        <div class="item-detail">{{ item.tag }}</div>
        <div class="item-detail"> <!-- 按钮列 -->
          <button class="edit-button">Edit</button>
          <button class="delete-button">Delete</button>
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
  import PaginationComponent from './PaginationComponent.vue';
  
  export default {
    components: {
      PaginationComponent,
    },
    data() {
      return {
        currentPage: 1,
        itemsPerPage: 10,
        data: [
          { id:'1', title: 'Campus Map', format: 'Image', author: 'John', time: '2023-01-01', tag:'Design', link: 'https://example.com/item1'},
          { id:'4', title: 'Lecture Notes - Introduction to Psychology', format: 'Document', author: 'Samuel', time: '2023-02-01', tag:'Interactive'},
          { id:'15', title: 'University Branding Guidelines',format: 'Document', author: 'William', time: '2023-03-01', tag:'Illustration'},
          { id:'21', title: 'Student Handbook', format: 'Document', author: 'Andrew', time: '2023-04-01', tag:'Concept Art'},
          { id:'33', title: 'Course Syllabus - Advanced Mathematics', format: 'Video', author: 'Tom', time: '2023-05-01', tag:'Data Visualization'},
          { id:'40', title: 'Library eBook Collection', format: 'Document', author: 'David', time: '2023-06-01', tag:'Graphic Design'},
          { id:'44', title: 'Research Paper - Climate Change Impact', format: 'Document', author: 'Samuel', time: '2023-07-01', tag:'Typography'},
          { id:'68', title: 'Faculty Contact Directory', format: 'Document', author: 'Robert', time: '2023-08-01', tag:'Prototype'},
          { id:'99', title: 'Event Flyer - Annual Science Fair', format: 'Document', author: 'Mark', time: '2023-09-01', tag:'Mockup'},
          { id:'101', title: 'Virtual Tour - University Campus', format: 'Video', author: 'Andrew', time: '2023-10-01', tag:'3D Modeling'},
          { id:'127', title: 'IT Services User Manual', format: 'Video', author: 'Michael', time: '2023-11-01', tag:'Media'},
          { id:'151', title: 'Public Lecture Recordings', format: 'Audio', author: 'Robert', time: '2023-12-01', tag:'Infographic'},
        ]
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.data.length / this.itemsPerPage);
      },
      paginatedData() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        return this.data.slice(start, start + this.itemsPerPage);
      }
    },
    methods: {
      changePage(page) {
        this.currentPage = page;
      }
    }
  };
  </script>
  
  <style scoped>
  .content-list {
    margin-top: 20px; /* 与搜索框的距离 */
    width: 100%; /* 使列表宽度占据整个父容器 */
  }
  
  .list-header {
    display: flex;
    background-color: #e5e5e5; /* 蓝色背景 */
    color: black; /* 白色字体 */
    padding: 10px 0; /* 适当的上下内边距 */
    font-weight: bold;
    border-radius: 5px;
    font-size: 14px;
  }
  
  .header-item {
    flex: 1;
    text-align: center;
  }
  
  .list-item {
    display: flex;
    padding: 15px 0; /* 列表项的上下内边距 */
    font-size: 14px;
  }
  
  .item-detail {
    flex: 1;
    text-align: center;
  }

  .edit-button {
  background-color: #3498db; /* 蓝色 */
  color: white;
  border: none;
  padding: 5px 10px;
  margin-right: 5px;
  cursor: pointer;
  border-radius: 4px;
}

.delete-button {
  background-color: #e74c3c; /* 红色 */
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 4px;
}

</style>