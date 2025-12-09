<template>
  <div class="login-container">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="username" required />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" required />
      </div>

      <div class="form-group">
        <label for="role">Role</label>
        <select id="role" v-model="role" required>
          <option value="1">Administrator</option>
          <option value="2">Super Teacher</option>
          <option value="3">Teacher</option>
        </select>
      </div>

      <div class="form-group">
        <button type="submit">Login</button>
      </div>

      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import { decodeJwt } from 'jose';


export default {
  data() {
    return {
      username: "",
      password: "",
      role: "",
      errorMessage: "",
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post(
            `/api/login?username=${this.username}&password=${this.password}&role=${this.role}`
        );

        if (response.data.code === 1) {
          // Login success, store token
          localStorage.setItem("token", response.data.data);
          console.log(localStorage)
          // Check role and redirect to the appropriate dashboard
          if (this.role === '1') {
            // Admin
            this.$router.push('/AdminDashboard');
          } else if (this.role === '2') {
            // Super Teacher
            this.$router.push('/SuperTeacherDashboard');
          } else if (this.role === '3') {
            // Teacher
            this.$router.push('/SubteacherDashboard');
          }
        } else {
          // Handle error messages from the API
          this.errorMessage = response.data.msg || "Login failed.";
        }
      } catch (error) {
        this.errorMessage = "An error occurred. Please try again.";
        console.error(error);
      }
    },
  },
  mounted() {
    // Check if the user is already logged in
    const token = localStorage.getItem("token");
    console.log("1"+token);

    if (token) {
      try {
        const decodedToken = decodeJwt(token); // Decode the JWT token
        const userRole = decodedToken.role; // Extract the role from the token
        const userId = decodedToken.id
        // Store user ID and role in local storage
        localStorage.setItem("userId", userId);
        localStorage.setItem("userRole", userRole);
        // Redirect based on user role
        if (userRole === 1) {
          this.$router.push('/AdminDashboard');
        } else if (userRole === 2) {
          this.$router.push('/SuperTeacherDashboard');
        } else if (userRole === 3) {
          this.$router.push('/SubteacherDashboard');
        }
      } catch (error) {
        console.error('Invalid token:', error);
        // If token is invalid, clear it and redirect to login page
        localStorage.removeItem("token");
        if (this.$route.path !== '/login') {
          this.$router.push('/login');
        }
      }
    }

  },
};
</script>

<style scoped>
.login-container {
  width: 300px;
  margin: 100px auto;
  padding: 20px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: #fff;
  text-align: center;
}

h2 {
  margin-bottom: 20px;
  color: #094183;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #094183;
}

input,
select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #094183;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #16395b;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>
