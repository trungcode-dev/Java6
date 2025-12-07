<template>
  <div class="container mt-4">
    <h2 class="mb-4 text-primary fw-bold">Quản lý đơn hàng</h2>

    <!-- FORM CHỌN USER + SẢN PHẨM -->
    <div class="card mb-4 shadow-sm border-0">
      <div class="card-body">

        <!-- Chọn User -->
        <div class="mb-3">
          <label class="form-label fw-bold">Người dùng <span class="text-danger">*</span></label>
          <select v-model="form.userId" class="form-select" :class="{ 'is-invalid': submitted && !form.userId }">
            <option :value="null" disabled>Chọn user</option>
            <option v-for="u in users" :key="u.id" :value="u.id">
              {{ u.username }} (ID: {{ u.id }})
            </option>
          </select>
          <div v-if="submitted && !form.userId" class="invalid-feedback">Vui lòng chọn người dùng</div>
        </div>

        <!-- CHỌN SẢN PHẨM -->
        <div class="mb-3">
          <label class="form-label fw-bold">Sản phẩm</label>
          <div class="d-flex gap-2">
            <select v-model="selectedProductId" class="form-select">
              <option :value="null" disabled>Chọn sản phẩm</option>
              <option v-for="p in products" :key="p.id" :value="p.id">
                {{ p.name }} — {{ formatPrice(p.price) }}
              </option>
            </select>

            <input
              v-model.number="selectedQuantity"
              type="number"
              min="1"
              class="form-control"
              style="width: 100px"
              placeholder="SL"
            />

            <button @click="addProduct" type="button" class="btn btn-success">
              Thêm
            </button>
          </div>
        </div>

        <!-- DANH SÁCH CHI TIẾT -->
        <div v-if="form.details.length > 0" class="table-responsive mb-3">
          <table class="table table-bordered table-sm align-middle">
            <thead class="table-light">
              <tr>
                <th width="40">#</th>
                <th>Sản phẩm</th>
                <th width="120">Giá</th>
                <th width="100">SL</th>
                <th width="130">Tổng</th>
                <th width="80"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(d, i) in form.details" :key="d.productId">
                <td>{{ i + 1 }}</td>
                <td><strong>{{ d.productName }}</strong></td>
                <td class="text-end">{{ formatPrice(d.unitPrice) }}</td>
                <td class="text-center">
                  <input
                    v-model.number="d.quantity"
                    type="number"
                    min="1"
                    class="form-control form-control-sm text-center"
                    style="width: 70px"
                  />
                </td>
                <td class="text-end fw-bold">{{ formatPrice(d.unitPrice * d.quantity) }}</td>
                <td class="text-center">
                  <button @click="removeDetail(d.productId)" class="btn btn-danger btn-sm">
                    Xóa
                  </button>
                </td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <td colspan="4" class="text-end fw-bold fs-5">Tổng tiền:</td>
                <td class="text-end fs-5 text-primary fw-bold">{{ formatPrice(totalAmount) }}</td>
                <td></td>
              </tr>
            </tfoot>
          </table>
        </div>

        <div v-else class="text-center text-muted py-4 border rounded bg-light">
          Chưa có sản phẩm nào
        </div>

        <!-- NÚT THANH TOÁN -->
        <div class="d-flex justify-content-end gap-2 mt-3">
          <button @click="resetForm" class="btn btn-outline-secondary">
            Hủy
          </button>
          <button
            @click="goToCheckout"
            class="btn btn-success"
            :disabled="!form.userId || form.details.length === 0"
          >
            Thanh toán
          </button>
        </div>
      </div>
    </div>

    <!-- DANH SÁCH ĐƠN HÀNG -->
    <div class="card shadow-sm border-0">
      <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Danh sách đơn hàng</h5>
        <span class="badge bg-light text-dark">{{ orders.length }} đơn</span>
      </div>

      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead class="table-light">
              <tr>
                <th>ID</th>
                <th>User</th>
                <th>Ngày đặt</th>
                <th class="text-end">Tổng tiền</th>
                <th class="text-center">Trạng thái</th>
                <th class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="o in orders" :key="o.id" class="align-middle">
                <td><strong>#{{ o.id }}</strong></td>
                <td>{{ o.username || '—' }}</td>
                <td>{{ formatDate(o.orderDate) }}</td>
                <td class="text-end fw-bold text-primary">{{ formatPrice(o.totalAmount) }}</td>
                <td class="text-center">
                  <span class="badge rounded-pill px-3" :class="statusClass(o.status)">
                    {{ statusText(o.status) }}
                  </span>
                </td>
                <td class="text-center">
                  <button @click="changeStatus(o)" class="btn btn-warning btn-sm">
                    Đổi trạng thái
                  </button>
                </td>
              </tr>

              <tr v-if="orders.length === 0">
                <td colspan="6" class="text-center py-5 text-muted">
                  Chưa có đơn hàng nào
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import orderService from "@/services/admin/orderService";
import productService from "@/services/admin/productService";
import userService from "@/services/admin/userService";

const router = useRouter();

const form = ref({
  userId: null,
  details: []
});

const orders = ref([]);
const users = ref([]);
const products = ref([]);
const submitted = ref(false);

const selectedProductId = ref(null);
const selectedQuantity = ref(1);

const totalAmount = computed(() =>
  form.value.details.reduce((s, d) => s + (d.unitPrice || 0) * d.quantity, 0)
);

const loadData = async () => {
  try {
    const [o, u, p] = await Promise.all([
      orderService.getAll(),
      userService.getAll(),
      productService.getAll()
    ]);
    orders.value = o;
    users.value = u;
    products.value = p;
  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
    alert("Không tải được dữ liệu!");
  }
};

const addProduct = () => {
  if (!selectedProductId.value) return alert("Vui lòng chọn sản phẩm");

  const prod = products.value.find(p => p.id === selectedProductId.value);
  if (!prod) return;

  const exists = form.value.details.find(d => d.productId === prod.id);
  if (exists) {
    exists.quantity += selectedQuantity.value;
  } else {
    form.value.details.push({
      productId: prod.id,
      productName: prod.name,
      unitPrice: prod.price,
      quantity: selectedQuantity.value
    });
  }

  selectedProductId.value = null;
  selectedQuantity.value = 1;
};

const removeDetail = (productId) => {
  form.value.details = form.value.details.filter(d => d.productId !== productId);
};

const resetForm = () => {
  form.value = { userId: null, details: [] };
  selectedProductId.value = null;
  selectedQuantity.value = 1;
  submitted.value = false;
};

const goToCheckout = () => {
  if (!form.value.userId) return alert("Vui lòng chọn người dùng");
  if (form.value.details.length === 0) return alert("Vui lòng thêm sản phẩm");

  const user = users.value.find(u => u.id === form.value.userId);
  if (!user) return alert("Không tìm thấy người dùng!");

  const checkoutData = {
    userId: form.value.userId,
    username: user.username,
    items: form.value.details.map(d => ({
      product: {
        id: d.productId,
        name: d.productName,
        price: d.unitPrice
      },
      quantity: d.quantity
    }))
  };

  // LƯU VÀ CHUYỂN TRANG
  localStorage.setItem("admin_checkout_data", JSON.stringify(checkoutData));
  console.log("Đã lưu checkout data:", checkoutData); // DEBUG

  // DÙNG replace ĐỂ TRÁNH BACK LỖI
  router.replace("/checkout");
};

const changeStatus = async (order) => {
  const next = prompt("Nhập trạng thái mới (PENDING, CONFIRMED, SHIPPED, DELIVERED):", order.status);
  if (!next) return;

  const valid = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED'].includes(next.toUpperCase());
  if (!valid) return alert("Trạng thái không hợp lệ!");

  try {
    await orderService.updateStatus(order.id, next.toUpperCase());
    alert("Đã cập nhật trạng thái");
    await loadData();
  } catch (err) {
    alert("Lỗi cập nhật trạng thái");
  }
};

const formatDate = (dateString) => {
  if (!dateString) return "—";
  const date = new Date(dateString);
  return date.toLocaleString("vi-VN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });
};

const formatPrice = (n) => {
  if (n == null) return "—";
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(n);
};

const statusText = (s) => {
  const map = {
    PENDING: "Chờ xác nhận",
    CONFIRMED: "Đã xác nhận",
    SHIPPED: "Đang giao",
    DELIVERED: "Đã giao"
  };
  return map[s] || s;
};

const statusClass = (s) => {
  const map = {
    PENDING: "bg-warning text-dark",
    CONFIRMED: "bg-info text-white",
    SHIPPED: "bg-primary text-white",
    DELIVERED: "bg-success text-white"
  };
  return map[s] || "bg-secondary";
};

onMounted(loadData);
</script>

<style scoped>
.table th, .table td { vertical-align: middle; }
</style>