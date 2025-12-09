<template>
  <div class="category-asset-page">
    <button class="back" @click="goBack">Back</button>
    <h2>Assets in this category</h2>

    <!-- Asset List -->
    <div class="asset-list">
      <div v-for="(item, index) in assets" :key="index" class="asset-card">
        <div class="image-column">
          <img :src="getImageSrc(item.format)" alt="Asset Image" />
        </div>
        <div class="asset-info">
          <h3>{{ item.assetName }}</h3>
          <p><strong>Format:</strong> {{ item.format }}</p>
          <p><strong>Subject:</strong> {{ item.subjectNumber }}</p>
          <p><strong>Author:</strong> {{ item.author }}</p>
        </div>
        <div class="asset-info-right">
          <p><strong>Tag:</strong> {{ item.tag }}</p>
          <p><strong>Create Time:</strong> {{ item.createTime.substring(0, 10) }}</p>
          <p><strong>Course Type:</strong> {{ item.studentType }}</p>
        </div>
        <div class="asset-actions">
<!--          <button class="view-button" @click="viewAsset(item.canvasLink)">VIEW</button>-->
          <button class="move-button" @click="openMovePopup(item.assetId)">MOVE TO</button>
        </div>
      </div>
    </div>

    <!-- Pagination Component -->
    <PaginationComponent
        :current-page="currentPage"
        :total-pages="totalPages"
        @change-page="changePage"
    />

    <!-- Move To Category Pop-up -->
    <div v-if="showMovePopup" class="popup-overlay">
      <div class="popup-content">
        <h3>Select a Category</h3>
        <select v-model="selectedCategoryId">
          <option v-for="category in categories" :key="category.categoryId" :value="category.categoryId">
            {{ category.categoryName }}
          </option>
        </select>
        <div class="popup-buttons">
          <button @click="confirmMove">Confirm</button>
          <button @click="cancelMove">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import PaginationComponent from '@/components/PaginationComponent.vue';

export default {
  components: {
    PaginationComponent,
  },
  data() {
    return {
      categoryId: this.$route.params.categoryId,
      categoryName: '',
      currentPage: 1,
      itemsPerPage: 10,
      assets: [],
      totalAssets: 0,
      showMovePopup: false,
      selectedCategoryId: '',
      categories: [],
      currentAssetId: '',
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.totalAssets / this.itemsPerPage);
    },
  },
  methods: {
    // Helper method to get the token from localStorage and set the headers
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
        this.$router.push('/login'); // Adjust the route as needed
      }
    },

    fetchAssets() {
      axios
          .get('/api/category/asset', {
            params: {
              categoryId: this.categoryId,
              page: this.currentPage,
              pageSize: this.itemsPerPage,
            },
            ...this.getAuthHeader(),
          })
          .then((response) => {
            if (response.data.code === 1) {
              this.assets = response.data.data.rows;
              this.totalAssets = response.data.data.total;
            } else {
              alert('Failed to fetch assets: ' + response.data.msg);
            }
          })
          .catch((error) => {
            console.error('Error fetching assets:', error);
          });
    },
    fetchCategories() {
      axios
          .get('/api/category/teacher', {
            params: {teacherId: 1},
            ...this.getAuthHeader(),
          })
          .then((response) => {
            if (response.data.code === 1) {
              this.categories = response.data.data;
            } else {
              console.error('Error fetching categories:', response.data.msg);
            }
          })
          .catch((error) => {
            console.error('Error fetching categories:', error);
          });
    },
    changePage(page) {
      this.currentPage = page;
      this.fetchAssets();
    },
    goBack() {
      this.$router.push('/assetCollection');
    },
    openMovePopup(assetId) {
      this.currentAssetId = assetId;
      this.showMovePopup = true;
      this.fetchCategories();
    },
    confirmMove() {
      axios
          .put('/api/category/asset/classify', null, {
            params: {
              categoryId: this.selectedCategoryId,
              assetId: this.currentAssetId,
            },
            ...this.getAuthHeader(),
          })
          .then(() => {
            return axios.delete(`/api/category/asset/remove/${this.currentAssetId}`, {
              params: {categoryId: this.categoryId},
              ...this.getAuthHeader(),
            });
          })
          .then(() => {
            this.fetchAssets();
            this.showMovePopup = false;
          })
          .catch((error) => {
            console.error('Error moving asset:', error);
          });
    },
    cancelMove() {
      this.showMovePopup = false;
    },
    getImageSrc(format) {
      if (format === 'Image' || format === 'image') {
        return require('@/assets/icon.jpeg');
      } else if (format === 'Video' || format === 'video') {
        return require('@/assets/video.png');
      } else if (format === 'Document') {
        return require('@/assets/doc.png');
      } else {
        return require('@/assets/pdf.png');
      }
    },
  },
  mounted() {
    this.fetchAssets();
    this.fetchCategories();
  },
};
</script>

<style scoped>
.category-asset-page {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}

.asset-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.asset-card {
  display: flex;
  align-items: center;
  background-color: #f9f9f9;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 15px;
  width: 80%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.asset-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.image-column img {
  width: 100px;
  height: 80px;
  border-radius: 5px;
  margin-right: 20px;
}

.asset-info {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.asset-info-right {
  flex: 1;
  font-size: 14px;
}

.asset-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.view-button, .move-button {
  background-color: #094183;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 8px;
  font-size: 14px;
}

.view-button:hover, .move-button:hover {
  background-color: #16395B;
}

/* Popup styling */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.popup-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.popup-buttons button {
  padding: 10px 20px;
  background-color: #094183;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.popup-buttons button:hover {
  background-color: #16395B;
}

/* Back button */
.back {
  background-color: #094183;
  color: white;
  padding: 10px 15px;
  margin-bottom: 20px;
  border-radius: 8px;
  cursor: pointer;
}
</style>
