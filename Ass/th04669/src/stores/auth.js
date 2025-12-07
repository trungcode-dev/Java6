// src/stores/auth.js
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user') || 'null'), // Lưu toàn bộ user
    token: localStorage.getItem('token') || null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token && !!state.user,
    username: (state) => state.user?.username,
    userId: (state) => state.user?.id
  },

  actions: {
    login({ token, user }) {
      this.token = token
      this.user = user
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})