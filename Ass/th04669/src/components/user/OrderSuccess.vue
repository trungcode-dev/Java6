<template>
  <div class="container mt-5 text-center">
    <h1 class="text-success mb-4">Thanh toán thành công!</h1>
    <p class="fs-4">Mã đơn hàng: <strong>#{{ orderId }}</strong></p>
    <button @click="$router.push('/oders')" class="btn btn-primary mt-3">
      Quay lại quản lý đơn hàng
    </button>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";
const route = useRoute();
const orderId = route.params.id;
</script>