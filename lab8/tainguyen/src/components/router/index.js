import { createRouter, createWebHistory } from "vue-router";
import Login from "@/components/auth/Login.vue";
import Register from "@/components/auth/Register.vue";
import Home from "@/views/Home.vue";
import ProductList from "@/views/ProductList.vue";
import ProductDetail from "@/views/ProductDetail.vue";

const routes = [
  { path: "/", component: Home },
  { path: "/login", component: Login },
  { path: "/register", component: Register },
  { path: "/products", component: ProductList},
  { path: "/products/:id", component: ProductDetail}
];

export default createRouter({
  history: createWebHistory(),
  routes,
});