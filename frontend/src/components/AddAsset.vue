<template>
  <form @submit.prevent="submitForm">
    <div class="asset-details">

      <div class="asset-left">
        <div class="image"></div>
        <div class="tag">
          <div class="tag-input">
            <img v-if="isTagInputVisible || tagAdded"  src='@/assets/tag.png' class="icon" />
            <p v-if= tagAdded>{{ asset.tag  }}</p>
            <input v-if="isTagInputVisible" type="text" v-model="newTag" id="tag" class="input-box" required />
          </div>
          <button v-if="!isTagInputVisible && !tagAdded" type="button" @click="showTagInput" class="tag-button">Add Tag</button>
          <div v-if="isTagInputVisible && !tagAdded" class="button-group">
            <button type="button" @click="addTag" class="tag-button">Submit Tag</button>
            <button type="button" @click="cancelTag" class="tag-button">Cancel</button>
          </div>
        </div>
      </div>
      <div class="asset-right">
        <div class="asset-info">
          <div class="form-text">
            <label for="assetName">*Asset Name</label>
            <input v-model="asset.assetName" @input="emitAssetUpdate" type="text" id="assetName" required />
          </div>
          <div class="form-text">
            <label for="canvasLink">*Access Link</label>
            <input v-model="asset.canvasLink" @input="emitAssetUpdate" type="text" id="canvasLink" required />
          </div>
        </div>

        <div class="file-info">
          <div class="title">
            <h3>Asset Information</h3>
          </div>
          <div class="info">
            <div class="info-left">
              <div class="form-group">
                <label for="description">*Description</label>
                <input v-model="asset.description" @input="emitAssetUpdate" type="text" id="description" required />
              </div>
              <div class="form-group">
                <label for="format">*Subject Number</label>
                <input v-model="asset.subjectNumber" @input="emitAssetUpdate" type="text" id="subejctNumber" required />
              </div>
              <div class="form-group">
                <label for="file">File</label>
                <input type="file" @change="handleFileChange" id="file" />
              </div>
              <div>
                <p v-if="message" :style="{color: messageColor}">{{ message }}</p>
              </div>
            </div>
            <div class="info-right">
              <div class="form-group">
                <label for="author">*Author</label>
                <input v-model="asset.author" @input="emitAssetUpdate" type="text" id="author" required />
              </div>
              <div>
                <label for="studentType">*Student Type:</label>
                <select id="studentType" class="select-box" v-model="asset.studentType" @input="emitAssetUpdate" required>
                  <option value="" disabled>Select the student type</option>
                  <option value="Postgraduate">Postgraduate</option>
                  <option value="Undergraduate">Undergraduate</option>
                  <option value="Diploma">Diploma</option>
                  <option value="Certificate">Certificate</option>
                </select>
              </div>
              <div class="form-group">
                <label for="format">Format</label>
                <h4>{{asset.format}}</h4>
              </div>
            </div>
          </div>
          <button type="submit">Create Asset</button>
        </div>
        <div class="tip-info">Fields marked with * are required </div>
      </div>
    </div>
  </form>
</template>

<script>
import axios from 'axios';
import {decodeJwt} from "jose";
export default {
  name: 'AddAsset',
  props: {
    assetsData: {
      type: Object,
      required: false,
      default: () => ({})
    }
  },
  data() {
    return {
      asset: {
        assetName: "",
        tag: "",
        format: "",
        author: "",
        description: "",
        canvasLink: "",
        file: "",
        studentType: "",
        subjectNumber: "",
      },
      newTag:'',
      selectedFile: null,
      message: '',
      messageColor: 'red',
      validFormats: {
        'application/pdf': 'PDF',
        'audio/mpeg': 'Audio',
        'audio/wav': 'Audio',
        'video/mp4': 'Video',
        'video/quicktime': 'Video', // MOV files
        'video/x-msvideo': 'Video', // AVI files
        'image/jpeg': 'Image',
        'image/png': 'Image',
      },
      isTagInputVisible: false,
      tagAdded: false
    }
  },
  computed: {
    isValidFileType() {
      // Check if the selected file is valid
      return this.selectedFile && this.validFormats[this.selectedFile.type];
    }
  },
  created() {
    console.log('Received assetsData in AddAsset:', this.assetsData.assetName);

    if (this.assetsData) {
      this.asset.assetName = this.assetsData.assetName;
      this.asset.description = this.assetsData.description;
      this.asset.canvasLink = this.assetsData.canvasLink;
      this.asset.format = this.assetsData.format;
      this.asset.author = this.assetsData.author;
    }
  },
  methods: {
    emitAssetUpdate() {
      console.log('Updating asset:', this.asset);
      this.$emit('updateAsset', this.asset);
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const fileType = file.type;
        if (this.validFormats[fileType]) {
          this.asset.format = this.validFormats[fileType];
          this.selectedFile = file;
          this.message = 'Valid file type.';
          this.messageColor = 'green';
          this.asset.file = this.selectedFile; // Store the file name
        } else {
          this.asset.format = '';
          this.selectedFile = null;
          this.asset.file = ''; // Clear file name
          this.message = 'Invalid file type. Please upload a PDF, audio, video, or image file.';
          this.messageColor = 'red';
        }
      }
    },
    showTagInput() {
      this.isTagInputVisible = true;
    },
    addTag() {
      if (this.newTag.trim() !== '') {
        this.asset.tag = this.newTag.trim(); // Assign new tag to asset
        this.newTag = ''; // Clear input field
        this.isTagInputVisible = false; // Hide input field after adding tag
        this.tagAdded = true;
      } else {
        alert('Tag cannot be empty.');
      }
    },
    cancelTag() {
      this.newTag = ''; // Clear the input field
      this.isTagInputVisible = false; // Hide input field and cancel button
    },
    async submitForm() {
      let formData = new FormData();
      console.log(this.asset.assetName);
      formData.append('assetName', this.asset.assetName);
      formData.append('tag', this.asset.tag);
      formData.append('format', this.asset.format);
      formData.append('author', this.asset.author);
      formData.append('description', this.asset.description);
      formData.append('canvasLink', this.asset.canvasLink);
      formData.append('subjectNumber', this.asset.subjectNumber);
      formData.append('studentType', this.asset.studentType);
      formData.append('file', this.selectedFile);
      formData.append('teacherId',decodeJwt(localStorage.getItem('token')).id)
      console.log([...formData.entries()]);
      try{
        const response = await axios.post('/api/asset/addAsset/teacher', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'token': localStorage.getItem("token")
          }
        })

        if (response.data.code === 1) {
          alert('Asset created successfully');
          this.$emit('createAsset', this.asset);
          this.resetForm();
        } else {
          console.error('Error add asset unsuccessfully(not exit):', response.data);
        }

      }catch (error) {
        console.error('Error add asset unsuccessfully:', error);
      }
    },
    resetForm() {
      this.asset = {
        assetName: "",
        tag: "",
        format: "",
        author: "",
        description: "",
        canvasLink: "",
        file: "",
        studentType: "",
        subjectNumber: "",
      };
      this.selectedFile = null;
      this.message = '';
      this.messageColor = 'red';
    }
  }
}
</script>

<style scoped>
.asset-details {
  display: flex;
  width: 1500px;
  height: 750px;
  background-color: rgb(253, 253, 253);
  overflow-y: hidden;
}

.asset-left {
  width:300px;
  margin-left: 100px;
  height: 100%;
  margin-top: 80px;
}
.image {
  background-color: #ededed;
  width: 100%;
  height: 350px;
}
.tag{
  width:100%;
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center; /* Center items horizontally */
  margin-top: 20px;
}
.tag-input{
  width:100%;
  height:80px;
  display: flex;
  flex-direction: row;
}
.input-box{
  margin-top: 10px;
  height:40px;
}
.tag-button{
  margin-top: 0px;
  margin-left:0px;
}
.button-group {
  display: flex;
  flex-direction: column;
  gap: 10px; /* Space between buttons */
}
.icon {
  margin-top: 15px;
  width: 40px;
  height: 25px;
  margin-bottom: 5px;
}

.asset-right {
  display: flex;
  margin-top: 70px;
  margin-left: 100px;
  width:800px;
  height: 100%;
  flex-direction: column;
}
.asset-info {
  margin-left: 30px;
  height: 180px;
}
.asset-info h2 {
  margin-top: 20px;
}
.form-text {
  width:600px;
  margin-bottom: 15px;
}

.file-info{
  height: 340px;
}
.title{
  height: 30px;
  margin-left: 30px;
}
.info{
  display: flex;
}
.info-left{
  margin-left: 30px;
  width: 350px;
}

.form-group {
  width:250px;
  margin-top: 10px;
  margin-bottom: 15px;
}
.select-box{
  width:250px;
  height: 40px;
  font-size: 16px;
  margin-bottom: 10px;
}

label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  margin: 4px 0;
  box-sizing: border-box;
}

.tip-info{
  margin-left:400px;
  color: #cccccc;
}

button {
  margin-left: 250px;
  margin-top: 35px;
  font-size: 16px;
  width:160px;
  height:50px;
  background-color: #1E2A50;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #002766;
}

</style>

