import api from "@/api"

export default {
  getAll: () => api.get('/api/users').then(r => r.data),
  getById: (id) => api.get(`/api/users/${id}`).then(r => r.data),
  create: (data) => api.post('/api/users', data).then(r => r.data),
  update: (id, data) => api.put(`/api/users/${id}`, data).then(r => r.data),
  delete: (id) => api.delete(`/api/users/${id}`)
}