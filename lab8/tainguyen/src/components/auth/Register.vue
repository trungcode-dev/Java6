<template>
  <div class="form-container">
    <h2>Đăng ký</h2>
    <form @submit.prevent="register">
      <input v-model="username" placeholder="Tên đăng nhập" required />
      <input v-model="password" type="password" placeholder="Mật khẩu" required />
      <input v-model="confirmPassword" type="password" placeholder="Xác nhận mật khẩu" required />
      <button type="submit" :disabled="loading">
        {{ loading ? 'Đang đăng ký...' : 'Đăng ký' }}
      </button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p>Đã có tài khoản? <a href="/login">Đăng nhập</a></p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: '',
      error: '',
      loading: false
    }
  },
  methods: {
    async register() {
      if (this.password !== this.confirmPassword) {
        this.error = 'Mật khẩu không khớp!'
        return
      }
      this.loading = true
      this.error = ''
      try {
        await axios.post('/api/auth/register', {
          username: this.username,
          password: this.password
        })
        alert('Đăng ký thành công! Vui lòng đăng nhập.')
        this.$router.push('/login')
      } catch (err) {
        this.error = err.response?.data?.message || 'Không thể đăng ký'
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
  background: #28a745;
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