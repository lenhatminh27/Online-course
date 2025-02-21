<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Thể loại</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<div class="container mt-4">
    <h2 class="mb-4">Tạo mới Thể loại</h2>

    <!-- Thông báo lỗi / thành công -->
    <div id="alert-box" class="alert d-none"></div>

    <form id="categoryForm">
        <!-- Tên thể loại -->
        <div class="mb-3">
            <label for="categoryName" class="form-label">Tên thể loại <span class="text-danger">*</span></label>
            <input type="text" class="form-control" id="categoryName" name="name" required>
        </div>

        <!-- Mô tả thể loại -->
        <div class="mb-3">
            <label for="categoryDescription" class="form-label">Mô tả</label>
            <textarea class="form-control" id="categoryDescription" name="description"></textarea>
        </div>

        <!-- Chọn thể loại cha -->
        <div class="mb-3">
            <select id="categorySelect" name="categoryId">
                <option value="" disabled selected>Chọn thể loại cha</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Tạo thể loại</button>
    </form>
</div>

<script type="module">

    import {STORAGE_KEY, environment} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';

    document.addEventListener('DOMContentLoaded', function () {
        const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
        if (!userCurrent) {
            window.location.replace('/403');
        } else {
            const user = JSON.parse(userCurrent);
            const isAdmin = user?.roles?.includes('ADMIN');

            if (!isAdmin) {
                window.location.replace('/403');
            }
        }

        async function loadCategory() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/category/no-parent');
                const selectElement = document.getElementById("categorySelect");
                selectElement.innerHTML = '<option value="" disabled selected>Chọn thể loại cha</option>';
                response.forEach(category => {
                    const option = document.createElement("option");
                    option.value = category.id; // Gán value bằng id
                    option.textContent = category.name; // Hiển thị tên danh mục
                    selectElement.appendChild(option);
                });
            } catch (error) {
                console.error("Lỗi khi lấy danh sách thể loại:", error);
            }
        }

        loadCategory();

        const form = document.getElementById('categoryForm');
        form.addEventListener('submit', async function (event) {
            event.preventDefault();
            const name = document.getElementById('categoryName').value.trim();
            const description = document.getElementById('categoryDescription').value.trim();
            const parentCategoryId = document.getElementById('categorySelect').value || null;

            if (!name) {
                Swal.fire({
                    icon: "error",
                    title: "Tạo thể loại chưa thành công",
                    text: "Vui lòng nhập tên thể loại!",
                });
                return;
            }

            // 🔍 Kiểm tra độ dài tên thể loại
            if (name.length < 3 || name.length > 100) {
                Swal.fire({
                    icon: "error",
                    title: "Lỗi",
                    text: "Tên thể loại phải từ 3-100 ký tự!",
                });
                return;
            }

            //🔍 Kiểm tra định dạng tên thể loại
            const validNameRegex = /^[a-zA-ZÀ-ỹ0-9_\-\s]+$/;
            if (!validNameRegex.test(name)) {
                Swal.fire({
                    icon: "error",
                    title: "Lỗi",
                    text: "Tên thể loại chỉ được chứa chữ cái, số, khoảng trắng, gạch dưới (_) hoặc dấu gạch ngang (-)!",
                });
                return;
            }

            const requestData = {
                name,
                description,
                parentCategoryId: parentCategoryId ? Number(parentCategoryId) : null
            };

            console.log("🔍 Dữ liệu gửi đi:", requestData);

            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/category', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(requestData)
                });

                if (response) {
                    Swal.fire({
                        title: "Tạo thể loại thành công!",
                        icon: "success",
                        draggable: true,
                        confirmButtonText: "OK"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/admin/category"; // Chuyển hướng sau khi tạo khóa học thành công
                        }
                    });
                }
            } catch (error) {
                console.error('Lỗi khi gửi dữ liệu:', error);
                Swal.fire({
                    icon: "error",
                    title: "Lỗi",
                    text: "Thể loại này đã tồn tại",
                });
            }
        });
    });

</script>

</body>
</html>
