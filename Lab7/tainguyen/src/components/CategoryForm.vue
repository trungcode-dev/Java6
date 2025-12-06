<template>
    <div>
        <form @submit.prevent="save">
            <div class="mb-3">
                <label>Mã loại</label>
                <input 
                    v-model="form.id" 
                    type="number" 
                    class="form-control"
                    placeholder="Nhập ID danh mục"
                />
            </div>

            <div class="mb-3">
                <label>Tên loại</label>
                <input 
                    v-model="form.name" 
                    type="text" 
                    class="form-control"
                    placeholder="Nhập tên danh mục"
                />
            </div>

            <button class="btn btn-primary mt-2">Lưu</button>
            <button type="button" @click="reset" class="btn btn-secondary mt-2 ms-2">Làm mới</button>
        </form>

        <hr />

        <table class="table table-bordered mt-3">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in categories" :key="item.id">
                    <td>{{ item.id }}</td>
                    <td>{{ item.name }}</td>
                    <td>
                        <button @click="edit(item)" class="btn btn-warning btn-sm">Sửa</button>
                        <button @click="remove(item.id)" class="btn btn-danger btn-sm ms-2">Xóa</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import categoryService from '@/services/categoryService'

const form = ref({ id: null, name: '' })
const categories = ref([])

const load = async () => {
    categories.value = await categoryService.getAll()
}

const reset = () => {
    form.value = { id: null, name: '' }
}

const save = async () => {
    try {
        if (!form.value.name) {
            alert("Tên danh mục không được để trống!")
            return
        }

        if (form.value.id === null) {
            alert("Vui lòng nhập ID khi thêm mới (vì database không tự tăng)!")
            return
        }

        const exists = categories.value.some(c => c.id == form.value.id)

        const payload = {
            id: form.value.id,
            name: form.value.name
        }

        if (exists) {
            // UPDATE
            await categoryService.update(form.value.id, payload)
        } else {
            // CREATE
            await categoryService.create(payload)
        }

        reset()
        await load()

    } catch (error) {
        console.error("Save failed:", error.response?.data || error)
        alert("Lỗi khi lưu danh mục!")
    }
}

const edit = (item) => {
    form.value = { id: item.id, name: item.name }
}

const remove = async (id) => {
    if (confirm("Bạn có chắc muốn xóa không?")) {
        try {
            await categoryService.delete(id)
            await load()
        } catch (error) {
            alert("Xóa thất bại!")
        }
    }
}

onMounted(load)
</script>

<style>
/* nếu cần */
</style>
