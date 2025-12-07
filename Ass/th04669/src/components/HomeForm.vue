<template>
  <div class="home-container">
    <h1>Chào mừng bạn đến với hệ thống</h1>

    <div class="card">
      <p>Đây là trang Home đơn giản.</p>
      <p>Chúc bạn code vui vẻ!</p>
    </div>
<!-- 
    <button @click="logout" class="logout-btn">Đăng xuất</button> -->
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("user");
  router.push("/login");
};
</script>

<style>
.home-container {
  width: 600px;
  margin: 60px auto;
  text-align: center;
}

.card {
  padding: 20px;
  margin-top: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fafafa;
}

/* .logout-btn {
  margin-top: 25px;
  padding: 10px 20px;
  background: #e74c3c;
  border: none;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}
.logout-btn:hover {
  background: #c0392b;
} */
</style>
