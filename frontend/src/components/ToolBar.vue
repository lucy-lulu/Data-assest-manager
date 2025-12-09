<template>
  <nav class="SideBar">
    <img src='@/assets/bar_1.png' class="clickable-image"/>
    <img src='@/assets/bar_3.png' class="clickable-image"/>
    <img src='@/assets/bar_2.png' class="clickable-image" @click="homeClick"/>
    <img src='@/assets/bar_4.png' class="clickable-image" @click="collectionClick"/>
    <img src='@/assets/bar_5.png' class="clickable-image" @click="toggleAddOptions"/>
    <img v-if="this.role === 1" src='../assets/download.png' class="clickable-image" @click="downloadReport"/>
    <img src='../assets/logout.png' class="clickable-image" @click="logout"/>
    <!-- Add options pop-up -->
    <div v-if="showOptions" class="add-options-popup">
      <ul>
        <li @click="localUploadClick">Local Upload</li>
        <li @click="fetchFromCanvasClick">Fetch from Canvas</li>
      </ul>
    </div>
  </nav>
</template>

<script>
import axios from "axios";
import {decodeJwt} from "jose";

export default {
  name: 'SideBar',
  data() {
    return {
      showOptions: false, // To control the visibility of the popup
    };
  },
  created() {
    this.role = decodeJwt(localStorage.getItem('token')).role;
    console.log(this.role);
  },
  methods: {
    homeClick() {
      this.$router.push('/');
    },
    collectionClick() {
      this.$router.push('/assetCollection');
    },
    toggleAddOptions() {
      // Toggle visibility of the add options pop-up
      this.showOptions = !this.showOptions;
    },
    localUploadClick() {
      // Navigate to assetAddition (Local Upload)
      this.$router.push('/assetAddition');
      this.showOptions = false; // Close pop-up after click
    },
    fetchFromCanvasClick() {
      // Navigate to assetFetch (Fetch from Canvas)
      this.$router.push('/assetFetch');
      this.showOptions = false; // Close pop-up after click
    },
    logout() {
      localStorage.clear(); // Clear local storage
      this.$router.push('/login'); // Redirect to login page
    },

    downloadReport() {
      axios.get('/api/asset/generateReport', {
        headers: {
          token: localStorage.getItem("token"),
        }
      })
          .then(response => {
            const filePath = response.data.data;  // Extract the download URL (e.g., "/reports/Report.xlsx")
            const downloadUrl = `api/${filePath}`; // Construct the full URL for the report

            // Use an anchor element to trigger the file download
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.setAttribute('download', 'Report.xlsx'); // Set the downloaded file's name
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);  // Clean up after triggering the download
          })
          .catch(error => {
            console.error('Error downloading report:', error);
          });
    }






  }
};
</script>

<style scoped>
.SideBar {
  position: fixed;
  top: 0;
  right: 0;
  display: flex;
  width: 100px;
  height: 100%;
  flex-direction: column;
  align-items: center;
  background-color: white;
}

.clickable-image {
  margin-right: 10px;
  margin-top: 10px;
  width: 50px;
  height: 60px;
  display: flex;
  margin-bottom: 5px;
}

.clickable-image:hover {
  transform: scale(1.25);
}

/* Pop-up style for the add options */
.add-options-popup {
  position: absolute;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2); /* Softer shadow for depth */
  padding: 10px 20px; /* Add padding for better spacing */
  top: 320px; /* Adjust positioning as needed */
  right: 20px; /* Adjust positioning as needed */
  min-width: 150px; /* Set a minimum width for consistency */
  z-index: 10;
}

.add-options-popup ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.add-options-popup li {
  padding: 10px;
  cursor: pointer;
  font-size: 16px; /* Increased font size for readability */
  color: #094183;
  font-weight: bold;
  border-bottom: 1px solid #eee; /* Add separator between options */
}

.add-options-popup li:last-child {
  border-bottom: none; /* Remove bottom border from the last item */
}

.add-options-popup li:hover {
  background-color: #f0f0f5;
  color: #16395b;
  border-radius: 4px;
}
</style>
