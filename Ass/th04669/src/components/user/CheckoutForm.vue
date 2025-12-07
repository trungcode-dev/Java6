<template>
  <div class="container mt-4">
    <h2 class="mb-4">Thanh toán</h2>

    <!-- Thông tin người đặt -->
    <div class="card mb-4">
      <div class="card-header bg-info text-white">
        Thông tin người đặt
      </div>
      <div class="card-body">
        <p><strong>Khách hàng:</strong> {{ username || 'Đang tải...' }}</p>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="card mb-4">
      <div class="card-header bg-primary text-white">
        Sản phẩm trong đơn hàng
      </div>

      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-striped mb-0">
            <thead>
              <tr>
                <th>Sản phẩm</th>
                <th class="text-center">Số lượng</th>
                <th class="text-end">Đơn giá</th>
                <th class="text-end">Thành tiền</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in cartItems" :key="item.product.id">
                <td>{{ item.product.name }}</td>
                <td class="text-center">{{ item.quantity }}</td>
                <td class="text-end">{{ formatPrice(item.product.price) }}</td>
                <td class="text-end">{{ formatPrice(item.quantity * item.product.price) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-footer text-end">
        <strong class="fs-5">
          Tổng tiền: {{ formatPrice(totalAmount) }}
        </strong>
      </div>
    </div>

    <!-- Thông tin người nhận -->
    <div class="card mb-4">
      <div class="card-header bg-secondary text-white">
        Thông tin người nhận
      </div>

      <div class="card-body">
        <div class="mb-3">
          <label class="form-label">Tên người nhận</label>
          <input v-model="receiverName" class="form-control" placeholder="VD: Nguyễn Văn A" />
        </div>

        <div class="mb-3">
          <label class="form-label">Số điện thoại</label>
          <input v-model="phone" class="form-control" placeholder="VD: 0901234567" />
        </div>

        <div class="mb-3">
          <label class="form-label">Địa chỉ giao hàng</label>
          <textarea v-model="address" class="form-control" rows="2" placeholder="VD: 123 Lê Lợi, Hà Nội"></textarea>
        </div>
      </div>
    </div>

    <div class="d-flex gap-2">
      <button @click="goBack" class="btn btn-outline-secondary">Quay lại</button>
      <button @click="checkout" class="btn btn-success w-100 py-2 fs-5" :disabled="loading">
        <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
        Xác nhận thanh toán
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import orderService from "@/services/admin/orderService";

const router = useRouter();

// Form
const receiverName = ref("");
const phone = ref("");
const address = ref("");
const loading = ref(false);

// Dữ liệu từ Admin
const cartItems = ref([]);
const userId = ref(null);
const username = ref("");

// Tổng tiền
const totalAmount = computed(() =>
  cartItems.value.reduce((sum, item) => sum + item.quantity * item.product.price, 0)
);

const formatPrice = (value) => {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value);
};

// --- CHỐNG LỖI: Đọc localStorage AN TOÀN ---
onMounted(() => {
  // Dùng setTimeout để đảm bảo DOM đã sẵn sàng
  setTimeout(() => {
    const raw = localStorage.getItem("admin_checkout_data");
    console.log("Dữ liệu checkout từ localStorage:", raw); // DEBUG

    if (!raw) {
      alert("Không có dữ liệu thanh toán! Vui lòng quay lại tạo đơn.");
      return router.push("/oders");
    }

    try {
      const data = JSON.parse(raw);

      // Kiểm tra dữ liệu hợp lệ
      if (!data.userId || !data.items || data.items.length === 0) {
        throw new Error("Dữ liệu không hợp lệ");
      }

      userId.value = data.userId;
      username.value = data.username || "Khách";
      cartItems.value = data.items;

      // XÓA SAU KHI ĐỌC XONG
      localStorage.removeItem("admin_checkout_data");
    } catch (err) {
      console.error("Lỗi parse dữ liệu checkout:", err);
      alert("Dữ liệu thanh toán bị lỗi! Vui lòng thử lại.");
      router.push("/oders");
    }
  }, 0);
});

// Quay lại
const goBack = () => {
  router.push("/oders");
};

// Thanh toán
const checkout = async () => {
  if (!receiverName.value || !phone.value || !address.value) {
    alert("Vui lòng nhập đầy đủ thông tin người nhận.");
    return;
  }

  loading.value = true;

  const payload = {
    user: { id: userId.value },
    orderDetails: cartItems.value.map(item => ({
      product: { id: item.product.id },
      quantity: item.quantity
    }))
  };

  try {
    const order = await orderService.create(payload);
    alert(`Thanh toán thành công! Mã đơn: #${order.id}`);
    router.push(`/order-success/${order.id}`);
  } catch (error) {
    console.error("Lỗi tạo đơn:", error);
    alert("Thanh toán thất bại: " + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.table td, .table th {
  vertical-align: middle;
}
.spinner-border-sm {
  width: 1rem;
  height: 1rem;
}
</style>