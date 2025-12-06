<template>
    <div>
        <form @submit.prevent="save">
            <div class="mb-2"><label>Username</label><input v-model="form.username" class="form-control" /></div>
            <div class="mb-2"><label>Password</label><input v-model="form.password" type="password"
                    class="form-control" /></div>
            <div class="mb-2"><label>Họ tên</label><input v-model="form.fullname" class="form-control" /></div>
            <div class="mb-2">
                <label>Trạng thái</label>
                <select v-model="form.enabled" class="form-control">
                    <option :value="true">Kích hoạt</option>
                    <option :value="false">Vô hiệu</option>
                </select>
            </div>
            <div class="mb-2">
                <label>Vai trò</label>
                <select v-model="form.role" class="form-control">
                    <option value="USER">USER</option>
                    <option value="ADMIN">ADMIN</option>
                </select>
            </div>
            <button class="btn btn-primary">Lưu</button>
        </form>

        <hr />
        <table class="table table-bordered mt-3">
            <thead>
                <tr>
                    <th>User</th>
                    <th>Họ tên</th>
                    <th>Trạng thái</th>
                    <th>Vai trò</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="u in users" :key="u.username">
                    <td>{{ u.username }}</td>
                    <td>{{ u.fullname }}</td>
                    <td>{{ u.enabled ? 'Bật' : 'Tắt' }}</td>
                    <td>{{ u.role }}</td>
                    <td>
                        <button @click="edit(u)" class="btn btn-warning btn-sm">Sửa</button>
                        <button @click="remove(u.username)" class="btn btn-danger btn-sm">Xóa</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import userService from '@/services/userService'

const form = ref({ username: '', password: '', fullname: '', enabled: true, role: 'USER' })
const users = ref([])

const load = async () => {
    users.value = await userService.getAll()
}

const save = async () => {
    try {
        if (form.value.id) {  // For categories/products: check id (number)
            await userService.update(form.value.id, form.value);  // Or productService, etc.
        } else {
            await userService.create(form.value);
        }
        // Reset form and reload
        form.value = { id: '', name: '' };  // Adjust per form
        load();
    } catch (error) {
        console.error('Save failed:', error);
        // Optionally show a user-friendly message (see below)
    }
}

const edit = (item) => {
    form.value = { ...item }
}

const remove = async (id) => {
    if (confirm('Bạn có chắc muốn xóa?')) {  // Add confirmation
        try {
            await userService.delete(id);
            load();
        } catch (error) {
            alert('Xóa thất bại: ' + error.message);
        }
    }
}


onMounted(load)
</script>
