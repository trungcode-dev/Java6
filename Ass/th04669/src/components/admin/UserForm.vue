<template>
  <div class="container mt-4">
    <h2 class="mb-4">Quản lý người dùng</h2>

    <!-- Form thêm/sửa -->
    <div class="card mb-4">
      <div class="card-body">
        <form @submit.prevent="save">
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">Tên đăng nhập <span class="text-danger">*</span></label>
              <input v-model="form.username" type="text" class="form-control" placeholder="example" required />
            </div>

            <div class="col-md-6 mb-3">
              <label class="form-label">Họ tên</label>
              <input v-model="form.fullName" type="text" class="form-control" placeholder="Nguyen Van A" />
            </div>
          </div>

          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">Email</label>
              <input v-model="form.email" type="email" class="form-control" placeholder="example@gmail.com" />
            </div>

            <div class="col-md-6 mb-3">
              <label class="form-label">Số điện thoại</label>
              <input v-model="form.phone" type="text" class="form-control" placeholder="0912345678" />
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label">Địa chỉ</label>
            <input v-model="form.address" type="text" class="form-control" placeholder="Hanoi, Vietnam" />
          </div>

          <div class="mb-3">
            <label class="form-label">Vai trò</label>
            <select v-model="form.role" class="form-select">
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </div>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary" :disabled="loading">
              <span v-if="loading" class="spinner-border spinner-border-sm"></span>
              {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
            </button>
            <button type="button" @click="resetForm" class="btn btn-secondary">Làm mới</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Bảng người dùng -->
    <div class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Danh sách người dùng</h5>
        <span class="badge bg-primary">{{ users.length }} người dùng</span>
      </div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>Tên đăng nhập</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Địa chỉ</th>
                <th>Vai trò</th>
                <th class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="u in users" :key="u.id">
                <td>{{ u.id }}</td>
                <td><strong>{{ u.username }}</strong></td>
                <td>{{ u.fullName || '—' }}</td>
                <td>{{ u.email || '—' }}</td>
                <td>{{ u.phone || '—' }}</td>
                <td>{{ u.address || '—' }}</td>
                <td>
                  <span class="badge bg-info">{{ u.role }}</span>
                </td>
                <td class="text-center">
                  <button @click="edit(u)" class="btn btn-warning btn-sm me-1" title="Sửa">Sửa</button>
                  <button @click="remove(u.id)" class="btn btn-danger btn-sm" title="Xóa">Xóa</button>
                </td>
              </tr>
              <tr v-if="users.length === 0">
                <td colspan="8" class="text-center py-4 text-muted">Chưa có người dùng</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import userService from '@/services/admin/userService'

const form = ref({
  id: null,
  username: '',
  fullName: '',
  email: '',
  phone: '',
  address: '',
  role: 'USER'
})

const users = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    users.value = await userService.getAll()
  } catch (error) {
    console.error('Load failed:', error)
    alert('Không tải được dữ liệu!')
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.value = {
    id: null,
    username: '',
    fullName: '',
    email: '',
    phone: '',
    address: '',
    role: 'USER'
  }
}

const save = async () => {
  if (!form.value.username.trim()) {
    alert('Vui lòng nhập tên đăng nhập!')
    return
  }

  loading.value = true
  try {
    if (form.value.id) {
      await userService.update(form.value.id, form.value)
      alert('Cập nhật thành công!')
    } else {
      await userService.create(form.value)
      alert('Thêm mới thành công!')
    }
    resetForm()
    await loadData()
  } catch (error) {
    console.error('Save failed:', error)
    alert('Lỗi: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

const edit = (u) => {
  form.value = { ...u }
  window.scrollTo(0, 0)
}

const remove = async (id) => {
  if (!confirm('Xóa người dùng này?')) return
  try {
    await userService.delete(id)
    alert('Xóa thành công!')
    await loadData()
  } catch (error) {
    alert('Xóa thất bại!')
  }
}

onMounted(loadData)
</script>

<style scoped>
.spinner-border-sm { width: 1rem; height: 1rem; }
</style>
