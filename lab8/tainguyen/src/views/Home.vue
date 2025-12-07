<template>
  <div class="container">
    <h1>Chào mừng đến Shop!</h1>
    <p>Bạn đã đăng nhập thành công.</p>
    <button @click="logout">Đăng xuất</button>
  </div>
</template>

<script>
import axios from 'axios'

export default {
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
.container {
  max-width: 800px;
  margin: 3rem auto;
  text-align: center;
}
button {
  padding: 0.8rem 1.5rem;
  background: #d9534f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}
</style>