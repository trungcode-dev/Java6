// src/stores/cart.js
import { defineStore } from 'pinia'
import cartService from '@/services/user/cartService'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: []
  }),
  getters: {
    total: (state) => state.items.reduce((sum, i) => sum + i.price * i.quantity, 0)
  },
  actions: {
    async load() {
      this.items = await cartService.getCart()
    },
    async add(productId, quantity = 1) {
      await cartService.addToCart(productId, quantity)
      await this.load()
    },
    async remove(productId) {
      await cartService.removeFromCart(productId)
      await this.load()
    }
  }
})