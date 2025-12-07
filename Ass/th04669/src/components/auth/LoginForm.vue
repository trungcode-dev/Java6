<!-- src/views/Login.vue -->
<template>
  <div class="min-vh-100 d-flex align-items-center bg-light">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-4">

          <!-- Card đăng nhập -->
          <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

              <h4 class="text-center mb-4 text-dark fw-semibold">Đăng nhập</h4>

              <form @submit.prevent="handleLogin">
                <!-- Username -->
                <div class="mb-3">
                  <input
                    v-model="username"
                    type="text"
                    class="form-control form-control-sm"
                    placeholder="Tên đăng nhập"
                    required
                    :disabled="loading"
                    ref="usernameInput"
                  />
                </div>

                <!-- Password -->
                <div class="mb-3">
                  <input
                    v-model="password"
                    type="password"
                    class="form-control form-control-sm"
                    placeholder="Mật khẩu"
                    required
                    :disabled="loading"
                  />
                </div>

                <!-- Nút đăng nhập -->
                <button
                  type="submit"
                  class="btn btn-primary w-100 btn-sm"
                  :disabled="loading"
                >
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  {{ loading ? 'Đang vào...' : 'Đăng nhập' }}
                </button>
              </form>

              <!-- Lỗi -->
              <div v-if="error" class="alert alert-danger mt-3 py-2 small text-center">
                {{ error }}
              </div>

              <!-- Link đăng ký -->
              <p class="text-center mt-3 mb-0 small text-muted">
                Chưa có tài khoản?
                <router-link to="/register" class="text-primary text-decoration-none">
                  Đăng ký
                </router-link>
              </p>

            </div>
          </div>

          <!-- Footer nhỏ -->
          <p class="text-center mt-3 text-muted small">
            © 2025 ShopDB
          </p>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { login } from '@/services/authService'

const router = useRouter()
const auth = useAuthStore()

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)
const usernameInput = ref(null)

// TỰ ĐỘNG FOCUS Ô USERNAME KHI MỞ TRANG
onMounted(() => {
  nextTick(() => usernameInput.value?.focus())
})

const handleLogin = async () => {
  error.value = ''
  loading.value = true

  try {
    const res = await login({
      username: username.value.trim(),
      password: password.value
    })

    // DÙNG PINIA STORE ĐỂ LƯU TRẠNG THÁI
    auth.login({
      token: res.token,
      username: res.user.username || username.value
    })

    // Chuyển hướng
    router.push('/products')
  } catch (err) {
    error.value = err.response?.data?.message || 'Sai tài khoản hoặc mật khẩu!'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.form-control-sm,
.btn-sm {
  font-size: 0.875rem;
  padding: 0.375rem 0.75rem;
}

.btn-primary {
  background-color: #5a67d8;
  border: none;
  font-weight: 500;
}

.btn-primary:hover {
  background-color: #4c51bf;
}

.card {
  border-radius: 10px;
}

small {
  font-size: 0.8rem;
}
</style>