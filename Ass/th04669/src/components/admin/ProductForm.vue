<template>
  <div class="container mt-4">
    <h2 class="mb-4">Quản lý sản phẩm</h2>

    <!-- Form thêm/sửa -->
    <div class="card mb-4">
      <div class="card-body">
        <form @submit.prevent="save">
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">Tên sản phẩm <span class="text-danger">*</span></label>
              <input v-model="form.name" type="text" class="form-control" placeholder="iPhone 15 Pro" required />
            </div>

            <div class="col-md-6 mb-3">
              <label class="form-label">Giá <span class="text-danger">*</span></label>
              <input v-model.number="form.price" type="number" step="0.01" class="form-control" placeholder="28990000" required />
            </div>
          </div>

          <!-- THÊM TRƯỜNG MÔ TẢ -->
          <div class="mb-3">
            <label class="form-label">Mô tả</label>
            <textarea v-model="form.description" class="form-control" rows="3" placeholder="Chip A17 Pro, camera 48MP..."></textarea>
          </div>

          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">Danh mục <span class="text-danger">*</span></label>
              <select v-model="form.category.id" class="form-select" required>
                <option :value="null" disabled>Chọn danh mục</option>
                <option v-for="c in categories" :key="c.id" :value="c.id">
                  {{ c.name }}
                </option>
              </select>
            </div>
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

    <!-- Bảng sản phẩm -->
    <div class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Danh sách sản phẩm</h5>
        <span class="badge bg-primary">{{ products.length }} sản phẩm</span>
      </div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Giá</th>
                <th>Mô tả</th> <!-- ĐÃ THAY ĐỔI -->
                <th>Danh mục</th>
                <th class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in products" :key="p.id">
                <td>{{ p.id }}</td>
                <td><strong>{{ p.name }}</strong></td>
                <td>{{ formatPrice(p.price) }}</td>
                <td>
                  <span :title="p.description" class="text-truncate d-inline-block" style="max-width: 200px;">
                    {{ p.description || '—' }}
                  </span>
                </td>
                <td>
                  <span class="badge bg-info">{{ p.category?.name || '—' }}</span>
                </td>
                <td class="text-center">
                  <button @click="edit(p)" class="btn btn-warning btn-sm me-1" title="Sửa">
                    Sửa
                  </button>
                  <button @click="remove(p.id)" class="btn btn-danger btn-sm" title="Xóa">
                    Xóa
                  </button>
                </td>
              </tr>
              <tr v-if="products.length === 0">
                <td colspan="6" class="text-center py-4 text-muted">Chưa có sản phẩm</td>
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
import productService from '@/services/admin/productService'
import categoryService from '@/services/admin/categoryService'

const form = ref({
  id: null,
  name: '',
  price: '',
  description: '', // ĐÃ THÊM
  category: { id: null }
})

const products = ref([])
const categories = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const [prods, cats] = await Promise.all([
      productService.getAll(),
      categoryService.getAll()
    ])
    products.value = prods
    categories.value = cats
  } catch (error) {
    console.error('Load failed:', error)
    alert('Không tải được dữ liệu. Kiểm tra backend và token!')
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.value = { 
    id: null, 
    name: '', 
    price: '', 
    description: '', // ĐÃ THÊM
    category: { id: null } 
  }
}

const save = async () => {
  if (!form.value.name.trim() || !form.value.category.id) {
    alert('Vui lòng nhập tên và chọn danh mục!')
    return
  }

  loading.value = true
  try {
    const payload = {
      name: form.value.name,
      price: form.value.price,
      description: form.value.description || null, // ĐÃ THÊM
      category: { id: form.value.category.id }
    }

    if (form.value.id) {
      await productService.update(form.value.id, payload)
    } else {
      await productService.create(payload)
    }

    alert(form.value.id ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
    resetForm()
    await loadData()
  } catch (error) {
    console.error('Save failed:', error)
    alert('Lỗi: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

const edit = (p) => {
  form.value = {
    id: p.id,
    name: p.name,
    price: p.price,
    description: p.description || '', // ĐÃ THÊM
    category: { id: p.category?.id }
  }
  window.scrollTo(0, 0)
}

const remove = async (id) => {
  if (!confirm('Xóa sản phẩm này?')) return
  try {
    await productService.delete(id)
    alert('Xóa thành công!')
    await loadData()
  } catch (error) {
    alert('Xóa thất bại!')
  }
}

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price)
}

onMounted(loadData)
</script>

<style scoped>
.spinner-border-sm { width: 1rem; height: 1rem; }

/* Cắt ngắn mô tả trong bảng */
.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>