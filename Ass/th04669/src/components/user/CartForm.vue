<!-- src/components/user/CartForm.vue -->
<template>
  <div class="container mt-4">
    <h3 class="mb-4 text-success fw-bold">Thêm vào giỏ hàng</h3>

    <!-- CHỌN + THÊM SẢN PHẨM -->
    <div class="card shadow-sm border-0 mb-4">
      <div class="card-body">

        <div class="d-flex gap-2 align-items-end">
          <div class="flex-grow-1">
            <label class="form-label fw-medium">Chọn sản phẩm</label>
            <select v-model="selectedProductId" class="form-select form-select-sm">
              <option :value="null" disabled>— Chọn sản phẩm —</option>
              <option v-for="p in products" :key="p.id" :value="p.id">
                {{ p.name }} — {{ formatPrice(p.price) }}
              </option>
            </select>
          </div>

          <div>
            <button
              @click="addToCart"
              class="btn btn-success btn-sm px-4"
              :disabled="!selectedProductId || loading"
            >
              <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
              Thêm
            </button>
          </div>
        </div>

        <!-- Thông báo -->
        <div v-if="message" class="alert mt-3 py-2" :class="message.type">
          {{ message.text }}
        </div>

      </div>
    </div>

    <!-- GIỎ HÀNG CHI TIẾT -->
    <div v-if="cartItems.length" class="card shadow-sm border-0">
      <div class="card-header bg-light d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Giỏ hàng ({{ cartItems.length }} món)</h5>
        <button @click="clearCart" class="btn btn-outline-danger btn-sm">
          Xóa tất cả
        </button>
      </div>

      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-sm table-hover mb-0 align-middle">
            <thead class="table-light">
              <tr>
                <th>Sản phẩm</th>
                <th class="text-end">Giá</th>
                <th class="text-center">SL</th>
                <th class="text-end">Thành tiền</th>
                <th class="text-center">Xóa</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in cartItems" :key="item.productId">
                <td><strong>{{ item.name }}</strong></td>
                <td class="text-end">{{ formatPrice(item.price) }}</td>
                <td class="text-center" style="width: 120px">
                  <div class="input-group input-group-sm">
                    <button
                      @click="updateQuantity(item.productId, item.quantity - 1)"
                      class="btn btn-outline-secondary"
                      :disabled="item.quantity <= 1"
                    >−</button>
                    <input
                      v-model.number="item.quantity"
                      @change="updateQuantity(item.productId, item.quantity)"
                      type="number"
                      min="1"
                      class="form-control text-center"
                      style="width: 50px"
                    />
                    <button
                      @click="updateQuantity(item.productId, item.quantity + 1)"
                      class="btn btn-outline-secondary"
                    >+</button>
                  </div>
                </td>
                <td class="text-end fw-bold">{{ formatPrice(item.price * item.quantity) }}</td>
                <td class="text-center">
                  <button
                    @click="removeFromCart(item.productId)"
                    class="btn btn-danger btn-sm"
                    title="Xóa"
                  >
                    X
                  </button>
                </td>
              </tr>
            </tbody>
            <tfoot>
              <tr class="table-success">
                <td colspan="3" class="text-end fw-bold fs-5">Tổng cộng:</td>
                <td class="text-end fs-5 fw-bold text-success">{{ formatPrice(cartTotal) }}</td>
                <td></td>
              </tr>
            </tfoot>
          </table>
        </div>
      </div>

      <div class="card-footer text-end">
        <router-link to="/checkout" class="btn btn-primary">
          Thanh toán
        </router-link>
      </div>
    </div>

    <!-- Không có sản phẩm -->
    <div v-else class="text-center py-5 text-muted">
      <p class="fs-4">Giỏ hàng trống</p>
      <p>Hãy chọn sản phẩm để thêm vào giỏ!</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import productService from '@/services/admin/productService'
import cartService from '@/services/user/cartService'

const router = useRouter()

const products = ref([])
const selectedProductId = ref(null)
const loading = ref(false)
const message = ref(null)

// Giỏ hàng
const cartItems = ref([])
const cartTotal = computed(() => {
  return cartItems.value.reduce((sum, i) => sum + i.price * i.quantity, 0)
})

// Tải sản phẩm
const loadProducts = async () => {
  try {
    products.value = await productService.getAll()
  } catch (err) {
    showMessage('Không tải được sản phẩm!', 'alert-danger')
  }
}

// Tải giỏ hàng
const loadCart = async () => {
  try {
    cartItems.value = await cartService.getCart()
  } catch (err) {
    cartItems.value = []
  }
}

// Thêm vào giỏ
const addToCart = async () => {
  if (!selectedProductId.value) return

  loading.value = true
  try {
    await cartService.addToCart(selectedProductId.value, 1)
    showMessage('Đã thêm vào giỏ!', 'alert-success')
    selectedProductId.value = null
    await loadCart()
  } catch (err) {
    showMessage('Thêm thất bại!', 'alert-danger')
  } finally {
    loading.value = false
  }
}

// Cập nhật số lượng
const updateQuantity = async (productId, newQty) => {
  if (newQty < 1) return
  try {
    await cartService.updateQuantity(productId, newQty)
    await loadCart()
  } catch (err) {
    showMessage('Cập nhật thất bại!', 'alert-danger')
  }
}

// Xóa 1 sản phẩm
const removeFromCart = async (productId) => {
  if (!confirm('Xóa sản phẩm này khỏi giỏ?')) return
  try {
    await cartService.removeFromCart(productId)
    showMessage('Đã xóa!', 'alert-success')
    await loadCart()
  } catch (err) {
    showMessage('Xóa thất bại!', 'alert-danger')
  }
}

// Xóa toàn bộ giỏ
const clearCart = async () => {
  if (!confirm('Xóa toàn bộ giỏ hàng?')) return
  try {
    await cartService.clearCart()
    showMessage('Đã xóa toàn bộ!', 'alert-success')
    await loadCart()
  } catch (err) {
    showMessage('Xóa thất bại!', 'alert-danger')
  }
}

// Hiển thị thông báo
const showMessage = (text, type) => {
  message.value = { text, type }
  setTimeout(() => { message.value = null }, 3000)
}

// Format tiền
const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price)
}

onMounted(() => {
  loadProducts()
  loadCart()
})
</script>

<style scoped>
.table th, .table td {
  vertical-align: middle;
}
.input-group-sm .form-control {
  height: 31px;
}
.btn-sm {
  font-size: 0.875rem;
}
.alert {
  font-size: 0.9rem;
  padding: 0.5rem 1rem;
}
</style>