<template>
  <div class="form-container">
    <h2>Đăng nhập</h2>
    <form @submit.prevent="login">
      <input v-model="username" placeholder="Tên đăng nhập" required />
      <input v-model="password" type="password" placeholder="Mật khẩu" required />
      <button type="submit" :disabled="loading">
        {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
      </button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p>Chưa có tài khoản? <a href="/register">Đăng ký</a></p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      username: '',
      password: '',
      error: '',
      loading: false
    }
  },
  methods: {
    async login() {
      this.loading = true
      this.error = ''
      try {
        const res = await axios.post('/api/auth/login', {
          username: this.username,
          password: this.password
        })
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('user', JSON.stringify(res.data.user))
        axios.defaults.headers.common['Authorization'] = `Bearer ${res.data.token}`
        alert('Đăng nhập thành công!')
        this.$router.push('/')
      } catch (err) {
        this.error = err.response?.data?.message || 'Sai tên đăng nhập hoặc mật khẩu'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.form-container {
  max-width: 400px;
  margin: 3rem auto;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  text-align: center;
}
input, button {
  width: 100%;
  padding: 0.8rem;
  margin: 0.5rem 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}
button {
  background: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}
button:disabled {
  background: #aaa;
}
.error {
  color: red;
  margin-top: 1rem;
}
a {
  color: #007bff;
  text-decoration: none;
}
</style>