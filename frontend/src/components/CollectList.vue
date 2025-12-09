<template>
  <div class="category-container">
    <!-- Iterate over the fetched categories and display them as folders -->
    <div
        v-for="category in categories"
        :key="category.categoryId"
        class="category-folder"
        @click="goToCategoryAssets(category.categoryId)"
    >
    <img src="@/assets/files.png" class="folder-icon" />
    <div class="category-info">
      <h3>{{ category.categoryName }}</h3>
      <p>{{ category.total }} files</p>
    </div>
  </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['searchQuery', 'filters'],
  data() {
    return {
      categories: [], // Store the fetched categories here
      teacherId: 1 // Replace with the actual teacher ID
    };
  },
  mounted() {
    this.getCategories();
  },
  methods: {
    // Fetch teacher's categories
    getCategories() {
      axios.get('/api/category/teacher', {
        params: {
          teacherId: this.teacherId
        },headers: {
          token: localStorage.getItem("token")
          // token: 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoyLCJpZCI6IjEiLCJlbWFpbCI6InRlYWNoZXJAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6InRlYWNoZXJVc2VyIiwiZXhwIjoxNzI3ODExOTUyfQ.YOBjMdTZ97g6DRtsPlbg6Ustx5kyIdp8Pyzf0CVFQng',
        },
      })
          .then(response => {
            if (response.data.code === 1) {
              this.categories = response.data.data; // Assign categories from API response
            } else {
              console.error('Error fetching categories:', response.data.msg);
            }
          })
          .catch(error => {
            console.error('Error fetching categories:', error);
          });
    },
    // Navigate to the category's asset page
    goToCategoryAssets(categoryId) {
      this.$router.push(`/categoryAssets/${categoryId}`); // Route to the asset list of the category
    }
  }
};
</script>

<style scoped>
/* Styling for the category container and folders */
.category-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 40px;
}

.category-folder {
  width: 200px;
  height: 200px;
  text-align: center;
  cursor: pointer;
}

.folder-icon {
  width: 100px;
  height: 100px;
}

.category-info h3 {
  font-size: 18px;
  margin-top: 10px;
}

.category-info p {
  color: #888;
  font-size: 14px;
}
</style>
