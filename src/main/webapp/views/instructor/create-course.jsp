<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nhập tiêu đề</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8f6fc;
            margin: 0;
        }

        .container {
            text-align: center;
            width: 50%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        .header-create {
            margin-bottom: 30px;
        }

        h1 {
            font-size: 40px;
            font-weight: bold;
            color: #333;
        }

        p {
            color: #666;
        }

        #form-create-blog {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-width: 100%;
        }

        .input-container {
            margin-top: 20px;
            width: 90%;
            max-width: 600px;
            text-align: center;
            display: flex;
            justify-content: center;
        }

        input[type="text"], select, .btn {
            width: 100%;
            padding: 15px 10px;
            border: 2px solid #a783f9;
            border-radius: 6px;
            font-size: 16px;
            outline: none;
            box-sizing: border-box;
        }

        .btn {
            margin-top: 20px;
            background-color: #a783f9;
            color: white;
            border: none;
            cursor: pointer;
            width: 90%;
            max-width: 600px;
        }

        .btn:hover {
            background-color: #8b6be2;
        }

        .exit-btn {
            position: absolute;
            top: 15px;
            right: 20px;
            background: none;
            border: none;
            font-size: 20px;
            color: #888;
            cursor: pointer;
        }

        .exit-btn:hover {
            color: #333;
        }
    </style>
</head>
<body>

<button class="exit-btn" onclick="function exitPage() {
            window.location.replace('/instructor/course'); // Hoặc bất kỳ trang nào bạn muốn
}
exitPage()">
    <i class="fas fa-times"></i>
</button>

<div class="container">
    <header class="header-create">
        <h1>Vậy còn tiêu đề nội dung thì sao?</h1>
        <p>Đừng lo nếu bạn không nghĩ ra được một tiêu đề hay ngay bây giờ. Bạn có thể thay đổi sau.</p>
    </header>

    <form id="form-create-blog">
        <div class="input-container">
            <input type="text" name="title" placeholder="Ví dụ: Học Photoshop CS6 từ cơ bản">
        </div>

        <div class="input-container">
            <select id="categorySelect" name="categoryId">
                <option value="" disabled selected>Chọn thể loại</option>
            </select>
        </div>

        <button class="btn" type="submit">Hoàn thành</button>
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
            const isInstructor = user?.roles?.includes('INSTRUCTOR');

            if (!isInstructor) {
                window.location.replace('/403');
            }
        }
        async function loadCategory() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/category/no-parent');
                const selectElement = document.getElementById("categorySelect");
                selectElement.innerHTML = '<option value="" disabled selected>Chọn thể loại</option>';
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
        const form = document.getElementById('form-create-blog');
        form.addEventListener('submit', async function (event) {
            event.preventDefault();
            const title = form.title.value.trim();
            const categoriesId = form.categoryId.value;
            if (!title) {
                Swal.fire({
                    icon: "error",
                    title: "Tạo khóa học chưa thành công",
                    text: "Vui lòng nhập tiêu đề!",
                });
                return;
            }
            if (!categoriesId) {
                Swal.fire({
                    icon: "error",
                    title: "Tạo khóa học chưa thành công",
                    text: "Vui lòng chọn thể loại!",
                });
                return;
            }

            const requestData = {
                title,
                categoriesId: Number(categoriesId)
            };

            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/course', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(requestData)
                });

                if (response) {
                    Swal.fire({
                        title: "Tạo khóa học thành công!",
                        icon: "success",
                        draggable: true,
                        confirmButtonText: "OK"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/instructor/course"; // Chuyển hướng khi nhấn OK
                        }
                    });
                }
            } catch (error) {
                console.error('Lỗi khi gửi dữ liệu:', error);
                Swal.fire({
                    icon: "error",
                    title: "Lỗi",
                    text: "Có lỗi xảy ra, vui lòng thử lại!",
                });
            }
        });


    });

</script>

</body>
</html>

