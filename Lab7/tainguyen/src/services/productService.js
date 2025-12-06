import axios from 'axios'
const API = 'http://localhost:8080/api/products'
export default {
  getAll: () => axios.get(API).then(r => r.data),
  getById: (id) => axios.get(`${API}/${id}`).then(r => r.data),
  create: (data) => axios.post(API, data).then(r => r.data),
  update: (id, data) => axios.put(`${API}/${id}`, data).then(r => r.data),
  remove: (id) => axios.delete(`${API}/${id}`).then(r => r.data)
}
