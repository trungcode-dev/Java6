<template>
  <div class="container" v-if="product">
    <h2>{{ product.name }}</h2>
    <img :src="getImageUrl(product.image)" alt="Product" style="max-width: 300px;" />
    <p><strong>Giá:</strong> {{ formatPrice(product.price) }} VND</p>
    <p>{{ product.description }}</p>
    <button @click="addToCart" class="btn">Thêm vào giỏ hàng</button>
    <router-link to="/products">Quay lại</router-link>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return { product: null }
  },
  mounted() {
    this.loadProduct()
  },
  methods: {
    async loadProduct() {
      try {
        const res = await axios.get(`/api/products/${this.$route.params.id}`)
        this.product = res.data
      } catch (err) {
        alert('Không tìm thấy sản phẩm')
      }
    },
    addToCart() {
      let cart = JSON.parse(localStorage.getItem('cart') || '[]')
      cart.push(this.product)
      localStorage.setItem('cart', JSON.stringify(cart))
      alert('Đã thêm vào giỏ!')
    },
    formatPrice(value) {
      return new Intl.NumberFormat('vi-VN').format(value)
    },
    getImageUrl(image) {
      return image ? `http://localhost:8080/images/${image}` : 'https://via.placeholder.com/300'
    }
  }
}
</script>