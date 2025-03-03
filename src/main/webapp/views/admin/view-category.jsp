<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Blog</title>
    <!-- CKEditor CDN -->
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <title>DASHMIN - Bootstrap Admin Template</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">

    <!-- Favicon -->
    <link href="img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="../../assets/admin/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="../../assets/admin/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet"/>

    <!-- Customized Bootstrap Stylesheet -->
    <link href="../../assets/admin/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="../../assets/admin/css/style.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>

        {
            margin-top: 20px
        ;
        }
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f9;
        }

        .container {
            max-width: 600px;
            margin: auto;
            background: white;
            margin-top: 20px;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #007BFF;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }

        input, textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #f9f9f9;
        }

        .ck-editor__editable {
            min-height: 300px !important;
        }

        input:focus, textarea:focus {
            border-color: #007BFF;
            outline: none;
            background-color: #fff;
        }

        button {
            background-color: #007BFF;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }

        button:hover {
            background-color: #0056b3;
        }

        .form-section {
            margin-bottom: 20px;
        }

        #errorMessage {
            color: red;
            margin-bottom: 15px;
        }

        #successMessage {
            color: green;
            margin-bottom: 15px;
        }
    </style>

</head>

<body>

<!-- Sidebar Start -->
<%@include file="../../common/admin/menu.jsp" %>
<!-- Sidebar End -->


<!-- Content Start -->
<div class="content">
    <%@include file="../../common/admin/header.jsp" %>

    <div class="container mt-4">
        <h2 class="mb-4">Tạo mới Thể loại</h2>

        <!-- Thông báo lỗi / thành công -->
        <div id="alert-box" class="alert d-none"></div>

        <form id="categoryForm">
            <!-- Tên thể loại -->
            <div class="mb-3">
                <label for="categoryName" class="form-label">Tên thể loại <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="categoryName" name="name" placeholder="Tên thể loại ngắn gọn dễ hiểu và không chứa kí tự đặc biệt (bắt buộc)" required>
            </div>

            <!-- Mô tả thể loại -->
            <div class="mb-3">
                <label for="categoryDescription" class="form-label">Mô tả</label>
                <textarea class="form-control" id="categoryDescription" name="description" placeholder="Không bắt buộc"></textarea>
            </div>

            <!-- Chọn thể loại cha -->
<%--            <div class="mb-3">--%>
<%--                <select id="categorySelect" name="categoryId">--%>
<%--                    <option value="" disabled selected>Chọn thể loại cha</option>--%>
<%--                </select>--%>
<%--            </div>--%>

            <div class="mb-3">
                <label for="categorySelect" class="form-label">Chọn thể loại cha <span class="text-muted" style="font-size: 0.9em;">(không bắt buộc)</span></label>
                <select id="categorySelect" name="categoryId" class="form-control">
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
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../../assets/admin/lib/chart/chart.min.js"></script>
    <script src="../../assets/admin/lib/easing/easing.min.js"></script>
    <script src="../../assets/admin/lib/waypoints/waypoints.min.js"></script>
    <script src="../../assets/admin/lib/owlcarousel/owl.carousel.min.js"></script>
    <script src="../../assets/admin/lib/tempusdominus/js/moment.min.js"></script>
    <script src="../../assets/admin/lib/tempusdominus/js/moment-timezone.min.js"></script>
    <script src="../../assets/admin/lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

    <!-- Template Javascript -->
    <script src="../../assets/admin/js/main.js"></script>
</body>

</html>