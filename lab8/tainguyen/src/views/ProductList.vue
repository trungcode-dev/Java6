<template>
  <div class="container">
    <h2>Danh sách sản phẩm</h2>
    <div v-if="loading">Đang tải...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <div v-for="product in products" :key="product.id" class="product">
        <h3>{{ product.name }}</h3>
        <p>{{ formatPrice(product.price) }} VND</p>
        <router-link :to="`/products/${product.id}`">Xem chi tiết</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      products: [],
      loading: true,
      error: ''
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    async loadProducts() {
      try {
        const res = await axios.get('/api/products') // ← Gọi API backend
        this.products = res.data.content || res.data // ← Spring Data trả .content
      } catch (err) {
        this.error = 'Không tải được sản phẩm'
        console.error(err)
      } finally {
        this.loading = false
      }
    },
    formatPrice(value) {
      return new Intl.NumberFormat('vi-VN').format(value)
    }
  }
}
</script>

<style scoped>
.container { max-width: 800px; margin: 2rem auto; padding: 1rem; }
.product {
  border: 1px solid #ddd;
  padding: 1rem;
  margin: 1rem 0;
  border-radius: 8px;
}
.error { color: red; }
</style>