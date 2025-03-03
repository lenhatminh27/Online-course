<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang tổng quan khóa học</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <style>
        .upload-container {
            margin-top: 20px;
            border: 2px dashed #ccc;
            padding: 20px;
            text-align: center;
            border-radius: 10px;
            background-color: #f9f9f9;
        }
        .upload-container img {
            max-width: 200px;
            margin-bottom: 10px;
        }
        .custom-file-input {
            display: none;
        }


        .upload-container {
            border: 2px dashed #ccc;
            padding: 20px;
            text-align: center;
            border-radius: 10px;
            background-color: #fff;
        }

        .upload-container img {
            max-width: 250px;
            margin-bottom: 10px;
        }

        .upload-container p {
            font-size: 14px;
            color: #555;
            margin-bottom: 5px;
        }

        .upload-container a {
            color: #007bff;
            text-decoration: none;
            font-weight: bold;
        }

        .upload-container a:hover {
            text-decoration: underline;
        }

        .custom-file-input {
            display: none;
        }

        .btn-upload {
            display: flex;
            gap: 10px;
            justify-content: center;
            align-items: center;
            margin-top: 10px;
        }

        .btn-upload .btn {
            font-size: 14px;
            padding: 8px 12px;
        }
    </style>
    <script>
        function updateUrlSegment(oldSegment, newSegment) {
            let urlParts = window.location.pathname.split('/');
            let index = urlParts.indexOf(oldSegment);
            if (index !== -1 && urlParts[index + 1]) {
                urlParts[index] = newSegment;
                let newUrl = window.location.origin + urlParts.join('/');
                window.location.href = newUrl;
            } else {
                console.error(`Không tìm thấy segment: ` + oldSegment);
            }
        }

    </script>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';
        document.addEventListener('alpine:init', () => {
            const pathSegments = window.location.pathname.split('/');
            const curriculumIndex = pathSegments.indexOf("price");
            let courseId = null;
            if (curriculumIndex !== -1 && curriculumIndex + 1 < pathSegments.length) {
                courseId = pathSegments[curriculumIndex + 1];
                console.log("Course ID:", courseId);
            } else {
                console.error("Không tìm thấy courseId trong URL");
            }

            Alpine.store('course', {
                course: {
                    title: '', // khởi tạo title với chuỗi rỗng
                    description: '',
                    thumbnail: '',
                    price: 0,
                },
                priceOptions: [
                    { value: 0, label: 'Miễn phí' },
                    { value: 399000, label: '399.000 point' +
                            ' (tháng 1)' },
                    { value: 449000, label: '449.000 point' +
                            ' (tháng 2)' },
                    { value: 499000, label: '499.000 point' +
                            ' (tháng 3)' },
                    { value: 549000, label: '549.000 point' +
                            ' (tháng 4)' },
                    { value: 599000, label: '599.000 point' +
                            ' (tháng 5)' },
                    { value: 649000, label: '649.000 point' +
                            ' (tháng 6)' },
                    { value: 699000, label: '699.000 point' +
                            ' (tháng 7)' },
                    { value: 749000, label: '749.000 point' +
                            ' (tháng 8)' },
                    { value: 799000, label: '799.000 point' +
                            ' (tháng 9)' },
                    { value: 849000, label: '849.000 point' +
                            ' (tháng 10)' },
                    { value: 899000, label: '899.000 point' +
                            ' (tháng 11)' },
                    { value: 949000, label: '949.000 point' +
                            ' (tháng 12)' },
                    { value: 999000, label: '999.000 point' +
                            ' (tháng 13)' },
                    { value: 1049000, label: '1.049.000 point' +
                            ' (tháng 14)' },
                    { value: 1099000, label: '1.099.000 point' +
                            ' (tháng 15)' }
                ],



                async getCourseCurrent(){
                    const  response = await apiRequestWithToken(environment.apiUrl +  '/api/course/detail/' + courseId, {
                        method: 'GET',
                    })
                    console.log(response);
                    this.course = response;
                },

                async updateCourse(){
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/course' , {
                            method: 'PUT',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ price: this.course.price, courseId: courseId })
                        });
                        console.log('Course updated: ', response);
                        await this.getCourseCurrent();
                        Swal.fire('Thành công', 'Khóa học đã được cập nhật', 'success');
                    } catch(error) {
                        console.error('Error updating course', error);
                        Swal.fire('Lỗi', 'Cập nhật khóa học thất bại', 'error');
                    }
                },

            });

            Alpine.store('course').getCourseCurrent();
        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
    <style>
        body {
            background-color: #f9f9f9;
        }
        .sidebar {
            width: 300px;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .content {
            flex: 1;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<nav class="navbar navbar-dark bg-dark p-3">
    <a class="navbar-brand" href="#">Chương trình giảng dạy</a>
    <div>
        <button class="btn btn-outline-light">Xem trước</button>
        <a href="/instructor/course/" class="btn btn-light ms-2">
            <i class="fas fa-times"></i>
        </a>
    </div>
</nav>
<div class="container d-flex mt-4">
    <div class="sidebar">
        <h5 class="mb-3">Lên kế hoạch cho khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('target.html')">
            <label class="form-check-label">Học viên mục tiêu</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('/basic')">
            <label class="form-check-label">Cấu trúc khóa học</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('studio.html')">
            <label class="form-check-label">Thiết lập studio và tạo video thử nghiệm</label>
        </div>

        <h5 class="mt-4">Tạo nội dung của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="updateUrlSegment('price', 'curriculum')">
            <label class="form-check-label">Chương trình giảng dạy</label>
        </div>

        <h5 class="mt-4">Xuất bản khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="updateUrlSegment('price', 'basics')">
            <label class="form-check-label">Trang tổng quan khóa học</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" checked onclick="updateUrlSegment('price', 'price')">
            <label class="form-check-label">Định giá</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('/discount')">
            <label class="form-check-label">Khuyến mại</label>
        </div>
    </div>
    <div class="content ms-4" x-data="Alpine.store('course')">
        <div class="form">
            <h2>Định giá</h2>
            <div class="row mb-3 align-items-center">
                <label class="col-3 form-label">Chọn giá khóa học</label>
                <div class="col-6">
                    <select class="form-control" x-model="course.price">
                        <option value="0" disabled>Chọn</option>
                        <template x-for="(price, index) in priceOptions" :key="index">
                            <option :value="price.value" x-text="price.label"></option>
                        </template>
                    </select>
                </div>
            </div>


            <button class="btn btn-primary" @click="updateCourse()">Lưu</button>
        </div>
    </div>

</div>
</body>
<script>
    function redirectToPage(path) {
        let baseUrl = window.location.origin + window.location.pathname.split('/').slice(0, -1).join('/');
        window.location.href = baseUrl + path;
    }
</script>
</html>
