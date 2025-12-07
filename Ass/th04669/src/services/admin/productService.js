import api from "@/api"

const API = '/api/products'

export default {
  getAll: () => api.get(API).then(res => res.data),
  create: (data) => api.post(API, data).then(res => res.data),
  update: (id, data) => api.put(`${API}/${id}`, data).then(res => res.data),
  delete: (id) => api.delete(`${API}/${id}`).then(res => res.data)
}
