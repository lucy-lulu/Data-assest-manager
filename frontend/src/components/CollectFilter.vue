<template>
  <div class="filter-bar">
    <div class="title-background">
      <h2 class="page-title">MSPACE CATALOG</h2>
    </div>

    <!-- Filter Group: Asset Type -->
    <div class="filter-group">
      <h3 class="filter-title">Folder</h3>
      <!-- Loop through categories and handle right-click for context menu -->
      <div v-for="(category, index) in categories" :key="index" class="filter-item" @click="filterByCategory(category)" @contextmenu.prevent="showContextMenu($event, category.categoryId)">
        <label v-if="editingCategoryId !== category.categoryId" @dblclick="enableEditCategory(category)">
          {{ category.categoryName }}
        </label>
        <input v-else v-model="newCategoryName" @blur="saveRename(category)" @keyup.enter="saveRename(category)" />
      </div>
    </div>

    <button @click="showAddFolderDialog">Add Folder</button>

    <!-- Context Menu for Delete -->
    <div v-if="showContextMenuFlag" :style="{top: contextMenuY + 'px', left: contextMenuX + 'px'}" class="context-menu">
      <button @click="confirmDelete">Delete</button>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="modal">
      <div class="modal-content">
        <h3>Are you sure you want to delete this category?</h3>
        <button @click="deleteCategory">Delete</button>
        <button @click="cancelDelete">Cancel</button>
      </div>
    </div>

    <!-- Add Folder Modal -->
    <div v-if="showAddFolderModal" class="modal">
      <div class="modal-content">
        <h3>Add New Folder</h3>
        <input v-model="newCategoryName" placeholder="Enter folder name" />
        <button @click="addCategory">Add</button>
        <button @click="closeModal">Cancel</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "CollectFilter",
  data() {
    return {
      categories: [], // Categories to display
      teacherId: 1, // Assuming teacherId is 1, update it as needed
      editingCategoryId: null, // ID of category being edited
      newCategoryName: "", // New category name for editing or adding
      showAddFolderModal: false, // Controls visibility of add folder modal
      showDeleteModal: false, // Controls visibility of delete confirmation modal
      showContextMenuFlag: false, // Controls visibility of context menu
      categoryToDelete: null, // Category ID for deletion
      contextMenuX: 0, // X coordinate for context menu
      contextMenuY: 0, // Y coordinate for context menu
    };
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    // Fetch the list of categories
    fetchCategories() {
      axios
          .get(`/api/category/teacher?teacherId=${this.teacherId}`,{
            headers: {
              token: localStorage.getItem("token")
              // token: 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoyLCJpZCI6IjEiLCJlbWFpbCI6InRlYWNoZXJAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6InRlYWNoZXJVc2VyIiwiZXhwIjoxNzI3ODExOTUyfQ.YOBjMdTZ97g6DRtsPlbg6Ustx5kyIdp8Pyzf0CVFQng',
            },
          })
          .then((response) => {
            if (response.data.code === 1) {
              this.categories = response.data.data;
            } else {
              console.error("Error fetching categories:", response.data.msg);
            }
          })
          .catch((error) => {
            console.error("Error fetching categories:", error);
          });
    },

    // Filter by category, emit to the parent component
    filterByCategory(category) {
      this.$emit("filters-changed", category);
    },

    // Open the dialog to add a new folder
    showAddFolderDialog() {
      this.showAddFolderModal = true;
    },

    // Close the modal dialog
    closeModal() {
      this.showAddFolderModal = false;
      this.newCategoryName = ""; // Reset the input field
    },

    // Add a new category/folder
    async addCategory() {
      if (this.newCategoryName.trim() === "") {
        alert("Category name cannot be empty");
        return;
      }

      try {
        const response = await axios.post("/api/category/teacher/add", null, {
          params: {
            teacherId: this.teacherId,
            categoryName: this.newCategoryName,
          },
          headers: {
            token: localStorage.getItem("token")
          },
        });

        if (response.data.code === 1) {
          alert("Category added successfully");
          this.showAddFolderModal = false;
          this.newCategoryName = "";
          this.fetchCategories(); // Refresh the category list
        } else {
          alert("Failed to add category: " + response.data.msg);
        }
      } catch (error) {
        console.error("Error adding category:", error);
        alert("Error occurred while adding category");
      }
    },

    // Double-click to enable category rename
    enableEditCategory(category) {
      this.editingCategoryId = category.categoryId;
      this.newCategoryName = category.categoryName;
    },

    // Save the renamed category
    async saveRename(category) {
      if (this.newCategoryName.trim() === "") {
        alert("Category name cannot be empty");
        this.newCategoryName = category.categoryName;
        this.editingCategoryId = null;
        return;
      }

      // Check for duplicate category names
      const isDuplicate = this.categories.some(
          (existingCategory) =>
              existingCategory.categoryName === this.newCategoryName &&
              existingCategory.categoryId !== category.categoryId
      );

      if (isDuplicate) {
        alert("Category name already exists");
        this.newCategoryName = category.categoryName;
        this.editingCategoryId = null;
        return;
      }

      try {
        const response = await axios.put("/api/category/teacher/rename", null, {
          params: {
            teacherId: this.teacherId,
            categoryId: category.categoryId,
            newName: this.newCategoryName,
          },
          headers: {
            token: localStorage.getItem("token")
          },
        });

        if (response.data.code === 1) {
          alert("Category renamed successfully");
          category.categoryName = this.newCategoryName;
          this.editingCategoryId = null;
          this.fetchCategories(); // Refresh the category list
        } else {
          alert("Failed to rename category: " + response.data.msg);
        }
      } catch (error) {
        console.error("Error renaming category:", error);
        alert("Error occurred while renaming category");
      }
    },

    // Show the right-click context menu for delete
    showContextMenu(event, categoryId) {
      this.categoryToDelete = categoryId;
      this.contextMenuX = event.clientX;
      this.contextMenuY = event.clientY;
      this.showContextMenuFlag = true;

      // Close context menu when clicking elsewhere
      document.addEventListener("click", this.handleClickOutside);
    },

    // Handle click outside to close the context menu
    handleClickOutside(event) {
      const contextMenu = document.querySelector(".context-menu");
      if (contextMenu && !contextMenu.contains(event.target)) {
        this.showContextMenuFlag = false;
        document.removeEventListener("click", this.handleClickOutside);
      }
    },

    // Confirm delete operation
    confirmDelete() {
      this.showContextMenuFlag = false;
      this.showDeleteModal = true;
    },

    // Delete a category
    async deleteCategory() {
      try {
        const response = await axios.delete(`/api/category/teacher/delete/${this.categoryToDelete}`, {
          headers: {
            token: localStorage.getItem("token") // Adding the token
          }
        });
        if (response.data.code === 1) {
          alert("Category deleted successfully");
          this.fetchCategories(); // Refresh the category list
          this.showDeleteModal = false;
        } else {
          alert("Failed to delete category: " + response.data.msg);
        }
      } catch (error) {
        console.error("Error deleting category:", error);
        alert("Error occurred while deleting category");
      }
    },

    // Cancel the delete action
    cancelDelete() {
      this.showDeleteModal = false;
      this.categoryToDelete = null;
    },
  },
};
</script>

<style scoped>
/* Style for the filter bar */
.filter-bar {
  width: 250px;
  background-color: white;
  height: 1100px;
  box-shadow: 10px 0px 20px #f4f4f4;
  position: fixed;
  left: 0;
}

.title-background {
  background-color: #000f46;
  line-height: 60px;
  width: 250px;
  text-align: center;
}

.page-title {
  color: white;
  font-size: 18px;
  margin: 0;
}

.filter-title {
  color: white;
  background-color: #094183;
  padding: 7px;
  margin: 7px;
  border-radius: 8px;
  font-size: 15px;
  cursor: pointer;
}

.filter-item {
  margin: 25px;
  font-size: 15px;
  color: gray;
}

button {
  background-color: #094183;
  color: white;
  width: 230px;
  height: 35px;
  margin: 10px;
  border-radius: 8px;
  font-weight: bold;
  font-size: 14px;
}

button:hover {
  background-color: #0a4a99;
  cursor: pointer;
}

.modal {
  position: fixed;
}

</style>