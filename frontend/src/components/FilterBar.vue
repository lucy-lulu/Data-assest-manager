<template>
  <div class="filter-bar">
    <div class = "title-background">
    <h2 class = "page-title" >MSPACE CATEDLOG</h2>
    </div>
    <!-- Filter Group: Asset Type -->
    <div v-for="(group, index) in filterGroups" :key="index" class="filter-group">
      <h3 class="filter-title" @mouseover="hover = index" @mouseleave="hover = null" :class="{ 'hovered': hover === index }">
        {{ group.title }}
      </h3>
      <div v-for="(item, itemIndex) in group.items" :key="itemIndex" class="filter-item">
        <label>
          <input 
          type="checkbox" 
          v-model="selectedFilters[group.title]" 
          :value="item" 
          @change="onFilterChange"
          />
          {{ item }}
        </label>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  data() {
    return {
      selectedFilters: {
        Format: [],
        SubjectNumber: [],
        StudentType: []
      },
      filterGroups: [
        { title: 'Format', items: ['Image', 'Video', 'Document', "PDF"] },
        { title: 'SubjectNumber', items: ['SWEN90014', 'SWEN90006', 'SUBJ001', 'SUBJ002', 'SUBJ003', 'SUBJ004'] },
        { title: 'StudentType', items: ['Undergraduate', 'Postgraduate', 'Diploma', 'Certificate'] },
      ],
      hover:false,
      
    };
  },
  methods: {
    onFilterChange() {
      console.log("Filter emit", this.selectedFilters);
      this.$emit('filters-changed', this.selectedFilters);
    },
  }
};
</script>


  
  <style scoped>
  /* Style for the filter bar */
  .filter-bar {
    /*position:fixed;*/
    width: 250px;
    background-color: white;
    height: 1100px;
    box-shadow: 10px 0px 20px #f4f4f4;
    position:fixed;
    left: 0;
  }
  .title-background{
    background-color: #000f46;
    line-height: 60px;
    width:250px;
    text-align: center;
  }
  
  .page-title{
    color:white;
    font-size: 18px;
    text-align: center;
    margin:0;
    display: inline-block;
    vertical-align: middle;
  }
  
  /* Style for the filter group title */
  .filter-title {
    color: white;
    background-color: #094183;
    padding: 7px;
    margin: 7px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    text-align: left;
    border-radius: 8px;
    font-size: 15px;
  }
  
  /* Hover effect for filter group titles */
  .filter-title.hovered {
    background-color: #2970c5;
  }
  
  /* Style for the filter items */
  .filter-item {
    margin: 15px;
    text-align: left;
    font-size: 14px;
  }
  </style>