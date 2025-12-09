<template>
  <div class="asset-viewer">
    <PageHeader :toDashboard="pageName"/>
    <form @submit.prevent="submitForm">
    <div class="asset-details">
      <div class="asset-left">
      <div class="image">
        <div v-if="asset.format.toLowerCase() === 'pdf'">
          <img src='@/assets/document.jpg' alt="file Image" width="300px" height="350px">
        </div>
        <div v-else-if="asset.format.toLowerCase() === 'audio'">
          <audio :src="asset.url" controls></audio>
        </div>
        <div v-else-if="asset.format.toLowerCase() === 'video'">
          <video :src="asset.url" controls width="100%" height="350px"></video>
        </div>
        <div v-else-if="asset.format.toLowerCase() === 'image'">
          <img src='@/assets/icon.jpeg' alt="Logo Image" width="300px" height="350px">
        </div>
        <div v-else-if="asset.format === ''">
          <img src='@/assets/icon.jpeg' alt="Logo Image" width="300px" height="350px">
        </div>
      </div>
      <div class="tags">
        <div v-if="asset.tag != ''">
          <div class="tag-group">
            <div class="tag-txt">
              <img src='@/assets/tag.png' class="icon">
              <h3>{{  asset.tag  }}</h3>
            </div>
            <button type="button" @click="changeTag" class="tag-button">Change Tag</button>
          </div>
        </div>
        <div class="tag-input">
          <img v-if="isTagInputVisible"  src='@/assets/tag.png' class="icon" />
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
            <input v-model="asset.assetName" type="text" id="assetName" required />
          </div>
          <div class="form-text">
            <label for="canvasLink">*Access Link</label>
            <input v-model="asset.canvasLink" type="text" id="canvasLink" required />
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
                <input v-model="asset.description" type="text" id="description" required />
              </div>
              <div class="form-group">
                <label for="subjectNumber">*Subject Number</label>
                <input v-model="asset.subjectNumber" type="text" id="subjectNumber" required />
              </div>
              <div v-if="resetFile" class="info-text">
                <strong>File</strong><br><a :href="asset.url">{{ asset.url }}</a>
                <button @click="updateFile" class="change-button">Change the File</button>
              </div>
              <div v-if ="!resetFile" class="form-group">
                <label for="file">File</label>
                <input type="file" @change="handleFileChange" id="file" />
              </div>
            </div>
            <div class="info-right">
              <div class="form-group">
                <label for="author">*Author</label>
                <input v-model="asset.author" type="text" id="author" required />
              </div>
              <div>
                <label for="studentType">*Student Type:</label>
                <select id="studentType" class="select-box" v-model="asset.studentType"  required>
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
        </div>
        <button type="submit">Update Asset</button>
      </div>
    </div>
    </form>
    <SideBar/>
  </div>
</template>

<script>
import SideBar from '@/components/ToolBar.vue';
import PageHeader from '@/components/PageHeader.vue';
import axios from "axios";
import {decodeJwt} from "jose";

export default {
  name: 'assetUpdate',
  components: {
    SideBar,
    PageHeader
  },
  data() {
    return {
      asset: {
        pageName:'assetUpdate',
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
      newTag: '',
      resetFile: true,
      selectedFile: null,
      message: '',
      messageColor: 'red',
      isTagInputVisible: false,
    }
  },
  computed: {
    tagAdded() {
      return this.asset.tag.trim() !== '';
    }
  },
  mounted() {
    this.getAssetDetails();
  },
  methods: {
    getAssetDetails() {
      const assetId = Number(this.$route.params.id);
      console.log(localStorage.getItem("token"))
      axios.get(`/api/asset/${assetId}`,{
        headers: {
          token: localStorage.getItem("token")
        },
      })
          .then(response => {
            if (response.data.code === 1) {
              this.asset = response.data.data;
              console.log("fetching:",this.asset)
            }
          })
          .catch(error => {
            console.error("Error fetching asset details:", error);
          });
    },
    showTagInput() {
      this.isTagInputVisible = true;
    },
    changeTag() {
      this.asset.tag = "";
      this.isTagInputVisible = true;
      this.tagAdded = false;
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
    updateFile() {
      this.resetFile = false;
      this.asset.format = "";
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
    async submitForm() {
      let formData = new FormData();
      console.log(this.asset.assetName);
      formData.append('assetId', this.asset.assetId);
      formData.append('assetName', this.asset.assetName);
      formData.append('tag', this.asset.tag);
      formData.append('format', this.asset.format);
      formData.append('author', this.asset.author);
      formData.append('description', this.asset.description);
      formData.append('canvasLink', this.asset.canvasLink);
      formData.append('subjectNumber', this.asset.subjectNumber);
      formData.append('studentType', this.asset.studentType);
      formData.append('file', this.selectedFile);
      const userRole = localStorage.getItem("userRole")
      if(userRole === '1') {
        formData.append('teacherId','-1')
      } else{
        formData.append('teacherId',decodeJwt(localStorage.getItem('token')).id)
      }

      try{
        const response = await axios.post('/api/asset/editAsset', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'token': localStorage.getItem("token")
          }
        })

        if (response.data.code === 1) {
          await this.$router.push({ name: 'AssetInfo', params: { id:this.asset.assetId } });
          alert('Update asset successfully');
          this.resetForm();
        } else {
          console.error('Error update asset:', response.data);
        }

      }catch (error) {
        console.error('Error add asset unsuccessfully:', error);
      }
    },
  }
};
</script>

<style scoped>
.asset-details {
  display: flex;
  width: 1500px;
  height: auto;
  background-color: rgb(253, 253, 253);
  overflow-y: hidden;
}

.asset-left {
  width:300px;
  margin-left: 100px;
  height: 100%;
  margin-top: 80px;
}
.tag-group{
  display: flex;
  flex-direction: column;
  align-items: center;
}
.tag-txt{
  display: flex;
  flex-direction: row;
}
.tag-input{
  width:100%;
  height:80px;
  display: flex;
}
.input-box{
  margin-top: 10px;
  height:40px;
}
.tag-button{
  margin-top: 0px;
  margin-left:20px;
}
.button-group {
  display: flex;
  flex-direction: column;
  gap: 10px; /* Space between buttons */
}
.icon {
  margin-top: 20px;
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

button {
  margin-left: 430px;
  margin-top: 35px;
  font-size: 16px;
  width:160px;
  height:50px;
  background-color: #1E2A50;
  color: white;
  border: none;
  cursor: pointer;
}

.change-button {
  margin-left: 80px;
  margin-top: 45px;
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