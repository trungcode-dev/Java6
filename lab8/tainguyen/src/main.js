import { createApp } from 'vue';
import App from './App.vue';
import router from './components/router/index.js';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

// Toastify cho Vue 3
import vue3Toastify from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

// Cấu hình axios
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.withCredentials = true;

// Load token từ localStorage
const token = localStorage.getItem('token');
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// Interceptor 401 → tự động logout
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete axios.defaults.headers.common['Authorization'];
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

// Tạo app
const app = createApp(App);

// Cài Toastify (có this.$toast)
app.use(vue3Toastify, {
  autoClose: 3000,
  position: 'top-right',
  transition: 'bounce',
  theme: 'colored', // 'light' | 'dark' | 'colored'
  limit: 3,
  pauseOnHover: true,
  draggable: true,
  closeOnClick: true,
});

// Cài router
app.use(router);

// Mount
app.mount('#app');