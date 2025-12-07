import CheckoutForm from "@/components/user/CheckoutForm.vue";
import OrderSuccess from "@/components/user/OrderSuccess.vue";
import CategoryView from "@/views/admin/CategoryView.vue";
import OderView from "@/views/admin/OderView.vue";
import ProductView from "@/views/admin/ProductView.vue";
import UserView from "@/views/admin/UserView.vue";
import LoginView from "@/views/auth/LoginView.vue";
import RegisterView from "@/views/auth/RegisterView.vue";
import HomeView from "@/views/HomeView.vue";
import CartView from "@/views/user/CartView.vue";
import { createRouter, createWebHistory } from "vue-router";

const routes = [
  { path: "/", component: LoginView },
  { path: "/login", component: LoginView },
  { path: "/register", component: RegisterView},
  { path: '/categories', component: CategoryView },
  { path: '/products', component: ProductView },
  { path: '/users', component: UserView},
  { path: '/oders', component: OderView},
  { path: '/cart', component: CartView},
  { path: '/checkout', component: CheckoutForm},
  { path: '/order-success/:id', component: OrderSuccess}
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
