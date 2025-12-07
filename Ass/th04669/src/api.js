// src/services/api.js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080" // ← TỰ ĐỘNG THÊM /api/...
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

// Xử lý lỗi 401/403 → tự động logout
api.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem("token");
      alert("Phiên hết hạn! Vui lòng đăng nhập lại.");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;