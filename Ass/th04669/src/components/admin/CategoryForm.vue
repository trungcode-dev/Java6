<template>
  <div class="container mt-4">
    <h2 class="mb-4">Quản lý danh mục</h2>

    <!-- Form -->
    <div class="card mb-4">
      <div class="card-body">
        <form @submit.prevent="save">
          <div class="row">
            <div class="col-md-3" v-if="form.id">
              <label class="form-label">Mã</label>
              <input v-model.number="form.id" type="number" class="form-control" readonly />
            </div>
            <div class="col-md-9">
              <label class="form-label">Tên danh mục <span class="text-danger">*</span></label>
              <input v-model.trim="form.name" type="text" class="form-control" placeholder="Điện thoại" required />
            </div>
          </div>

          <div class="mt-3 d-flex gap-2">
            <button type="submit" class="btn btn-primary" :disabled="loading">
              <span v-if="loading" class="spinner-border spinner-border-sm"></span>
              {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
            </button>
            <button type="button" @click="reset" class="btn btn-secondary">Làm mới</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Bảng -->
    <div class="card">
      <div class="card-header">
        <h5 class="mb-0">Danh sách danh mục</h5>
      </div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>Tên danh mục</th>
                <th class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="c in categories" :key="c.id">
                <td>{{ c.id }}</td>
                <td><strong>{{ c.name }}</strong></td>
                <td class="text-center">
                  <button @click="edit(c)" class="btn btn-warning btn-sm me-1" title="Sửa">
                    Sửa
                  </button>
                  <button @click="remove(c.id)" class="btn btn-danger btn-sm" title="Xóa">
                    Xóa
                  </button>
                </td>
              </tr>
              <tr v-if="categories.length === 0">
                <td colspan="3" class="text-center py-4 text-muted">Chưa có danh mục</td>
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
import categoryService from '@/services/admin/categoryService'

const form = ref({ id: null, name: '' })
const categories = ref([])
const loading = ref(false)

const load = async () => {
  loading.value = true
  try {
    categories.value = await categoryService.getAll()
  } catch (error) {
    alert('Không tải được danh mục!')
  } finally {
    loading.value = false
  }
}

const reset = () => {
  form.value = { id: null, name: '' }
}

const save = async () => {
  if (!form.value.name.trim()) {
    alert('Tên danh mục không được để trống!')
    return
  }

  loading.value = true
  try {
    const payload = { name: form.value.name.trim() }

    if (form.value.id) {
      payload.id = form.value.id
      await categoryService.update(form.value.id, payload)
    } else {
      await categoryService.create(payload)
    }

    alert(form.value.id ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
    reset()
    await load()
  } catch (error) {
    const msg = error.response?.data?.message || error.message
    alert('Lỗi: ' + msg)
  } finally {
    loading.value = false
  }
}

const edit = (c) => {
  form.value = { id: c.id, name: c.name }
  window.scrollTo(0, 0)
}

const remove = async (id) => {
  if (!confirm('Xóa danh mục này? Các sản phẩm sẽ bị mất danh mục!')) return
  try {
    await categoryService.delete(id)
    alert('Xóa thành công!')
    await load()
  } catch (error) {
    alert('Xóa thất bại! Có thể danh mục đang được dùng.')
  }
}

onMounted(load)
</script>

<style scoped>
.spinner-border-sm { width: 1rem; height: 1rem; }
</style>