<!-- src/views/Register.vue -->
<template>
  <div class="min-vh-100 d-flex align-items-center bg-light">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-5">

          <!-- Card đăng ký -->
          <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

              <h4 class="text-center mb-4 text-dark fw-semibold">Đăng ký tài khoản</h4>

              <form @submit.prevent="handleRegister">
                <!-- Username -->
                <div class="mb-3">
                  <input
                    v-model="form.username"
                    type="text"
                    class="form-control form-control-sm"
                    placeholder="Tên đăng nhập"
                    required
                    :disabled="loading"
                  />
                </div>

                <!-- Password -->
                <div class="mb-3">
                  <input
                    v-model="form.password"
                    type="password"
                    class="form-control form-control-sm"
                    placeholder="Mật khẩu"
                    required
                    :disabled="loading"
                  />
                </div>

                <!-- Confirm Password -->
                <div class="mb-3">
                  <input
                    v-model="form.confirmPassword"
                    type="password"
                    class="form-control form-control-sm"
                    placeholder="Xác nhận mật khẩu"
                    required
                    :disabled="loading"
                  />
                </div>

                <!-- Full Name -->
                <div class="mb-3">
                  <input
                    v-model="form.fullName"
                    type="text"
                    class="form-control form-control-sm"
                    placeholder="Họ và tên (không bắt buộc)"
                  />
                </div>

                <!-- Email -->
                <div class="mb-3">
                  <input
                    v-model="form.email"
                    type="email"
                    class="form-control form-control-sm"
                    placeholder="Email (không bắt buộc)"
                  />
                </div>

                <!-- Phone -->
                <div class="mb-3">
                  <input
                    v-model="form.phone"
                    type="text"
                    class="form-control form-control-sm"
                    placeholder="Số điện thoại (không bắt buộc)"
                  />
                </div>

                <!-- Address -->
                <div class="mb-3">
                  <input
                    v-model="form.address"
                    type="text"
                    class="form-control form-control-sm"
                    placeholder="Địa chỉ (không bắt buộc)"
                  />
                </div>

                <!-- Nút đăng ký -->
                <button
                  type="submit"
                  class="btn btn-success w-100 btn-sm"
                  :disabled="loading"
                >
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  {{ loading ? 'Đang xử lý...' : 'Đăng ký' }}
                </button>
              </form>

              <!-- Thông báo -->
              <div v-if="message" :class="['alert mt-3 py-2 small text-center', message.includes('thành công') ? 'alert-success' : 'alert-danger']">
                {{ message }}
              </div>

              <!-- Link đăng nhập -->
              <p class="text-center mt-3 mb-0 small text-muted">
                Đã có tài khoản?
                <router-link to="/login" class="text-primary text-decoration-none">
                  Đăng nhập
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
import api from '@/api'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  fullName: '',
  email: '',
  phone: '',
  address: ''
})

const message = ref('')
const loading = ref(false)

const handleRegister = async () => {
  // Reset
  message.value = ''
  loading.value = true

  // Kiểm tra mật khẩu
  if (form.value.password !== form.value.confirmPassword) {
    message.value = 'Mật khẩu xác nhận không khớp!'
    loading.value = false
    return
  }

  try {
    const payload = {
      username: form.value.username.trim(),
      password: form.value.password,
      fullName: form.value.fullName.trim() || null,
      email: form.value.email.trim() || null,
      phone: form.value.phone.trim() || null,
      address: form.value.address.trim() || null
    }

    await api.post('/api/auth/register', payload)

    message.value = 'Đăng ký thành công! Đang chuyển về đăng nhập...'
    
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (err) {
    message.value = err.response?.data?.message || 'Đăng ký thất bại! Vui lòng thử lại.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* Chữ nhỏ, đơn giản */
.form-control-sm,
.btn-sm {
  font-size: 0.875rem;
  padding: 0.375rem 0.75rem;
}

.btn-success {
  background-color: #38a169;
  border: none;
  font-weight: 500;
}

.btn-success:hover {
  background-color: #2f855a;
}

.card {
  border-radius: 10px;
}

small {
  font-size: 0.8rem;
}

.alert {
  font-size: 0.85rem;
  padding: 0.5rem 1rem;
}
</style>