<template>
  <nav class="navbar">
    <div class="container">
      <a href="/" class="brand">Shop</a>
      <div class="nav-right">
        <template v-if="!isLoggedIn">
          <a href="/login" class="nav-link">Đăng nhập</a>
          <a href="/register" class="nav-link">Đăng ký</a>
        </template>
        <button v-else @click="logout" class="btn-logout">Đăng xuất</button>
      </div>
    </div>
  </nav>
</template>

<script>
import axios from 'axios'

export default {
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('token')
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      delete axios.defaults.headers.common['Authorization']
      alert('Đã đăng xuất!')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.navbar {
  background: #333;
  color: white;
  padding: 1rem 0;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.brand {
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  text-decoration: none;
}
.nav-right a {
  color: white;
  margin-left: 1rem;
  text-decoration: none;
}
.btn-logout {
  background: #d9534f;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  cursor: pointer;
  border-radius: 4px;
}
</style>