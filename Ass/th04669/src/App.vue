<template>
  <div id="app">

    <!-- CHƯA ĐĂNG NHẬP → CHỈ FORM -->
    <div v-if="!auth.isLoggedIn" class="min-vh-100 bg-light">
      <router-view></router-view>
    </div>

    <!-- ĐÃ ĐĂNG NHẬP → HIỆN MENU -->
    <div v-else>
      <nav class="navbar navbar-expand navbar-dark bg-dark px-3">
        <ul class="navbar-nav">
          <li class="nav-item">
            <router-link to="/products" class="nav-link">Sản phẩm</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/categories" class="nav-link">Danh mục</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/users" class="nav-link">Người dùng</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/oders" class="nav-link">Đơn hàng</router-link>
          </li>
        </ul>

        <ul class="navbar-nav ms-auto">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-capitalize" href="#" data-bs-toggle="dropdown">
              {{ auth.username }}
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><router-link class="dropdown-item" to="/cart">Giỏ hàng</router-link></li>
              <li><hr class="dropdown-divider" /></li>
              <li><button class="dropdown-item text-danger" @click="auth.logout(); router.push('/login')">
                Đăng xuất
              </button></li>
            </ul>
          </li>
        </ul>
      </nav>

      <div class="container mt-3">
        <router-view></router-view>
      </div>
    </div>

  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()
</script>

<style scoped>
.dropdown-item.text-danger:hover {
  background-color: #f8d7da !important;
  color: #dc3545 !important;
}
</style>