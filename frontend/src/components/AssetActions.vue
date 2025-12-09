<template>
  <div class="asset-actions">
    <button class="collect-button" @click="collectAssets">Collect Asset</button>
    <button class="edit-button" v-if="role !== 3" @click="editAsset">Edit Asset</button>
    <button class="delete-button" v-if="role !== 3" @click="deleteAsset">Delete Asset</button>
    <button class="feedback-button" @click="openFeedbackModal">Feedback</button>

    <!-- Feedback Modal -->
    <div v-if="showFeedbackModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeFeedbackModal">&times;</span>
        <h2>Feedback</h2>
        <textarea v-model="feedbackText" maxlength="200" placeholder="Enter your feedback here..."></textarea>
        <div class="modal-actions">
          <button class="cancel-button" @click="closeFeedbackModal">Cancel</button>
          <button class="submit-button" @click="submitFeedback">Submit</button>
        </div>
      </div>
    </div>

    <!-- Category Selection Modal -->
    <div v-if="showCategoryModal" class="modal">
      <div class="modal-content">
        <h3>Select a Category</h3>
        <select class="category-dropdown" v-model="selectedCategory">
          <option v-for="category in categories" :value="category.categoryId" :key="category.categoryId">
            {{ category.categoryName }}
          </option>
        </select>
        <div class="modal-buttons">
          <button class="confirm-button" @click="confirmCategorySelection">Confirm</button>
          <button class="cancel-button" @click="closeCategoryModal">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {decodeJwt} from "jose";


export default {
  name: 'AssetActions',
  props: {
    role: {
      required: true
    },
    assetId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      teacherId: 1,
      showCategoryModal: false,
      showFeedbackModal: false,
      selectedCategory: null,
      categories: [],
      feedbackText: '', // Feedback text data
    };
  },
  methods: {
    // Collect asset method
    collectAssets() {
      const teacherId = decodeJwt(localStorage.getItem('token')).role === 1 ? -1 : decodeJwt(localStorage.getItem('token')).id;
      axios.post('/api/asset/collect', null, {
        params: {
          teacherId: teacherId,
          assetId: this.assetId,
        },
        headers: {
          token: localStorage.getItem("token") // Fetch token from local storage
        },
      })
          .then(response => {
            if (response.data.code === 1) {
              alert('Asset collected successfully');
              this.fetchCategories();
              this.showCategoryModal = true;
            } else {
              alert('Error collecting asset: ' + response.data.msg);
            }
          })
          .catch(error => {
            console.error('Error collecting asset', error);
            alert('Error collecting asset');
          });
    },

    // Fetch categories for the teacher
    fetchCategories() {
      const teacherId = decodeJwt(localStorage.getItem('token')).role === 1 ? -1 : decodeJwt(localStorage.getItem('token')).id;
      axios.get('/api/category/teacher', {
        params: { teacherId: teacherId },
        headers: {
          token: localStorage.getItem("token") // Fetch token from local storage
        },
      })
          .then(response => {
            if (response.data.code === 1) {
              this.categories = response.data.data;
            } else {
              alert('Failed to fetch categories: ' + response.data.msg);
            }
          })
          .catch(error => {
            console.error('Error fetching categories', error);
          });
    },

    // Close the category modal
    closeCategoryModal() {
      this.showCategoryModal = false;
    },

    // Confirm category selection and classify asset
    confirmCategorySelection() {
      if (!this.selectedCategory) {
        alert('Please select a category');
        return;
      }

      axios.put('/api/category/asset/classify', null, {
        params: {
          categoryId: this.selectedCategory,
          assetId: this.assetId
        },
        headers: {
          token: localStorage.getItem("token") // Fetch token from local storage
        },
      })
          .then(response => {
            if (response.data.code === 1) {
              alert('Asset classified successfully');
            } else {
              alert('Error classifying asset: ' + response.data.msg);
            }
          })
          .catch(error => {
            console.error('Error classifying asset', error);
          })
          .finally(() => {
            this.closeCategoryModal();
          });
    },

    // Delete asset method
    deleteAsset() {
      const teacherId = decodeJwt(localStorage.getItem('token')).role === 1 ? -1 : decodeJwt(localStorage.getItem('token')).id;
      axios.delete(`/api/asset/remove/${this.assetId}`,{
        headers: {
          'token': localStorage.getItem("token"), // Add token to the request headers
        },
        params: {  // 使用 params 将 teacherId 添加到 URL 中
          teacherId: teacherId
        }
      })
          .then(response => {
            if (response.data.code === 1) {
              alert('Asset deleted successfully');
              this.$router.push('/AdminDashboard');
            } else {
              alert('Error deleting asset: ' + response.data.msg);
            }
          })
          .catch(error => {
            alert('Error deleting asset: you can not delete this asset'+ error);

          });
    },

    // Open the feedback modal
    openFeedbackModal() {
      this.showFeedbackModal = true;
      this.feedbackText = '';
    },

    // Close the feedback modal
    closeFeedbackModal() {
      this.showFeedbackModal = false;
    },

    // Submit feedback method
    submitFeedback() {
      // Validate feedback input
      if (this.feedbackText.trim() === '') {
        alert('Please enter your feedback.');
        return;
      }

      // Prepare the feedback data for the query parameters
      const feedbackData = {
        teacherId: this.teacherId,
        assetId: this.assetId,
        description: this.feedbackText,
      };

      // Adding the token from localStorage
      const config = {
        headers: {
          token: localStorage.getItem("token")
        },
        // Pass the feedbackData as query parameters (like in the Postman request)
        params: feedbackData
      };

      // Make the POST request with the feedbackData in the query parameters
      axios.post('/api/feedback/addFeedback', null, config)
          .then(response => {
            if (response.data.code === 1) {
              alert('Feedback submitted successfully');
              this.closeFeedbackModal(); // Close modal on success
            } else {
              alert('Error submitting feedback: ' + response.data.msg);
            }
          })
          .catch(error => {
            console.error('Error submitting feedback', error);
          });
    },

    editAsset() {
       this.$router.push({ name: 'assetUpdate', params: { id:this.assetId } });
      },
  }
/*
    async editAsset() {
      const assetData = {
        assetId: this.assetId,
        teacherId: this.teacherId,
        assetName: this.assetName || null,
        format: this.format || null,
        author: this.author || null,
        description: this.description || null,
        tag: this.tag || null,
        canvasLink: this.canvasLink || null,
        studentType: this.studentType || null,
        subjectNumber: this.subjectNumber || null
      };

      try {
        const response = await axios.post('/api/asset/editAsset', assetData, {
          headers: {
            token: localStorage.getItem("token"),
            'Content-Type': 'application/json'
          }
        });

        if (response.data.code === 1) {
          alert('Asset updated successfully');
        } else {
          alert('Error updating asset: ' + response.data.msg);
        }
      } catch (error) {
        console.error('Error updating asset:', error);
        alert('Error occurred while updating the asset');
      }
    }
 */
};
</script>

<style scoped>
.asset-actions {
  width: 310px;
  display: flex;
  flex-direction: column;
  padding-top: 10px;
}

.collect-button, .edit-button, .delete-button, .feedback-button {
  background-color: #1E2A50;
  color: white;
  border: none;
  margin-top: 40px;
  margin-left: 65px;
  font-size: 16px;
  width: 150px;
  height: 40px;
  text-align: center;
}

.collect-button:hover, .edit-button:hover, .delete-button:hover, .feedback-button:hover {
  background-color: #16395B;
}

.modal {
  position: fixed;
  z-index: 10;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 10px;
  text-align: center;
  width: 400px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

textarea {
  width: 100%;
  height: 100px;
  margin-bottom: 20px;
  padding: 10px;
  border-radius: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
}

.cancel-button, .submit-button {
  background-color: #1E2A50;
  color: white;
  border: none;
  font-size: 16px;
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.cancel-button:hover, .submit-button:hover {
  background-color: #16395B;
}
</style>
