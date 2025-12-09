<template>
    <div class="search-container">
      <div class="search-box">
        <!-- Text input for the search query -->
        <input type="text" v-model="searchQuery" class="search-input" placeholder="Type a keyword to start searching..." />
        <!-- Search button -->
        <button @click="performSearch" class="search-button">SEARCH</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchQuery: '' // Data property to hold the search query
      };
    },
    methods: {
      performSearch() {
        console.log('Search query emitted:', this.searchQuery);
        // Function to handle the search action
        this.$emit('search', this.searchQuery);
      }
    }
  };
  </script>
  
  <style scoped>
  /* Container to horizontally center the search box and add space below the navbar */
  .search-container {
    display: flex;
    justify-content: center;
    padding-top: 50px; /* Space below the navbar */
  }
  
  /* Search box styling */
  .search-box {
    display: flex;
    align-items: center;
    background-color: #094183;
    padding: 10px;
    border-radius: 13px; /* Increase border radius for more rounded corners */
    width: 550px; /* Reduce width for a smaller search box */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional: Add a subtle shadow for better visibility */
  }
  
  /* Styling for the input field */
  .search-input {
    width: 70%; /* Adjust width to fit within the smaller search box */
    padding: 8px;
    border: none;
    border-radius: 3px;
    margin-right: 10px;
  }
  
  /* Styling for the search button */
  .search-button {
    padding: 8px 16px;
    color: white;
    background-color: #094183;
    border: 1px solid white;
    border-radius: 3px;
    cursor: pointer;
    width: 130px;
    transition: background-color 0.3s ease;
  }
  
  /* Hover effect for the search button */
  .search-button:hover {
    background-color: #2970c5;
  }
  </style>
  