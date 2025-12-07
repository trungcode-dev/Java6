import api from "@/api"

const API_URL = '/api/auth'

export async function login(data) {
  const res = await api.post(`${API_URL}/login`, data)
  return res.data
}

export async function register(data) {
  const res = await api.post(`${API_URL}/register`, data)
  return res.data
}