import axios from 'axios'
const API = 'http://localhost:8080/api/users'
export default {
  getAll: () => axios.get(API).then(r => r.data),
  getByUsername: (username) => axios.get(`${API}/${username}`).then(r => r.data),
  create: (data) => axios.post(API, data).then(r => r.data),
  update: (username, data) => axios.put(`${API}/${username}`, data).then(r => r.data),
  remove: (username) => axios.delete(`${API}/${username}`).then(r => r.data)
}
