// src/services/admin/orderService.js
import api from '@/api'

export default {
  getAll: () => api.get('/api/orders').then(r => r.data),

  create: (payload) => api.post('/api/orders', payload).then(r => r.data),

  updateStatus: (id, status) =>
    api.put(`/api/orders/${id}/status`, `"${status}"`, {
      headers: { 'Content-Type': 'application/json' }
    }).then(r => r.data)
}