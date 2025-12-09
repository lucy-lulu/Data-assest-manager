<template>
    <div class="pagination">
      <button
        class="page-button"
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        Previous
      </button>
      <span v-for="page in totalPages" :key="page" class="page-number" :class="{ active: page === currentPage }" @click="changePage(page)">
        {{ page }}
      </span>
      <button
        class="page-button"
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        Next
      </button>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      currentPage: {
        type: Number,
        required: true,
      },
      totalPages: {
        type: Number,
        required: true,
      },
    },
    methods: {
      changePage(page) {
        this.$emit('change-page', page);
      }
    }
  };
  </script>
  
  <style scoped>
  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  
  .page-button {
    margin: 0 5px;
    padding: 5px 10px;
    cursor: pointer;
    border: none;
  }
  
  .page-number {
    margin: 0 5px;
    padding: 5px 10px;
    cursor: pointer;
  }
  
  .active {
    font-weight: bold;
    color: #2c3e50; /* 高亮显示当前页码 */
  }
  </style>