<template>
  <div class="sidebar">
    <ul class="icon-list">
      <li v-for="(icon, index) in icons" :key="index">
        <!-- For all icons except the logout icon, use normal link or routing -->
        <a v-if="icon.action !== 'logout'" @click.prevent="handleIconClick(icon)">
          <img :src="require(`@/assets/${icon.src}`)" :alt="'icon' + (index + 1)" class="icon" />
        </a>
        <!-- For the logout icon, bind it to the logout function -->
        <a v-else @click.prevent="logout">
          <img :src="require(`@/assets/${icon.src}`)" alt="Logout Icon" class="icon" />
        </a>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      icons: [
        { src: 'account.png', action: 'external', link: 'https://link1.com' },
        { src: 'settings.png', action: 'external', link: 'https://link2.com' },
        { src: 'home.png', action: 'route', link: '/home' },
        { src: 'logout.png', action: 'logout' }, // Logout icon
      ]
    };
  },
  methods: {
    handleIconClick(icon) {
      if (icon.action === 'external') {
        window.open(icon.link, '_blank'); // Open external link in a new tab
      } else if (icon.action === 'route') {
        this.$router.push(icon.link); // Navigate to the specified route
      }
    },
    logout() {
      // Clear localStorage and redirect to login page
      localStorage.clear(); // Clear all local storage data
      this.$router.push('/login'); // Redirect to login page
    }
  }
};
</script>

<style scoped>
.sidebar {
  background-color: white;
  padding: 10px;
  width: 110px;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: fixed;
  right: 0;
  height: 100vh; /* Adjust to viewport height */
}

.icon-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.icon {
  width: 75px;
  height: 68px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.icon:hover {
  transform: scale(1.1); /* Scale icon on hover */
}
</style>
