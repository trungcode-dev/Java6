// src/services/cartService.js
import api from "@/api"

export default {
  getCart: () => api.get('/api/cart').then(r => r.data),
  addToCart: (productId, quantity) => 
    api.post('/api/cart/add', { productId, quantity }),
  removeFromCart: (productId) => 
    api.delete(`/api/cart/remove/${productId}`),
  clearCart: () => api.delete('/api/cart/clear')
}