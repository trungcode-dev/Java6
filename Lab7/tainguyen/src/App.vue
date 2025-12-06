<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark px-3">
      <a class="navbar-brand" href="#">J6Lab7</a>
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link to="/categories" class="nav-link">Loại hàng</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/products" class="nav-link">Sản phẩm</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/users" class="nav-link">Người dùng</router-link>
        </li>
      </ul>
    </nav>

    <!-- ⚠️ MẤU CHỐT NẰM Ở ĐÂY -->
    <div class="container mt-3">
      <router-view></router-view>
    </div>
  </div>
</template>
