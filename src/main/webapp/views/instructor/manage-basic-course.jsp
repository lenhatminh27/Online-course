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
            const curriculumIndex = pathSegments.indexOf("basics");
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
                    status: '',
                    categories: [],
                    categoriesId: null,
                },
                file: null,
                fileName: 'Không có file nào được chọn',
                errorMessage: '',
                categories: [],

                async getCourseCurrent(){
                    const  response = await apiRequestWithToken(environment.apiUrl +  '/api/course/detail/' + courseId, {
                        method: 'GET',
                    })
                    console.log(response);
                    this.course = response;
                    this.course.categoriesId = this.course.categories[0].id;
                },

                async getCategories(){
                    const response = await apiRequestWithToken(environment.apiUrl + '/api/category/no-parent', {
                        method: 'GET',
                    })
                    this.categories = response;
                },

                async updateCourse(){
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/course' , {
                            method: 'PUT',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ ...this.course, courseId: courseId, categoriesId: this.course.categoriesId })
                        });
                        console.log('Course updated: ', response);
                        Alpine.store('course').getCourseCurrent();
                        Swal.fire('Thành công', 'Khóa học đã được cập nhật', 'success');
                    } catch(error) {
                        console.error('Error updating course', error);
                        Swal.fire('Lỗi', 'Cập nhật khóa học thất bại', 'error');
                    }
                },

                handleFileUpload(event) {
                    this.errorMessage = ''; // Xóa lỗi cũ
                    const selectedFile = event.target.files[0];
                    if (!selectedFile) return;
                    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
                    if (!allowedTypes.includes(selectedFile.type)) {
                        this.errorMessage = 'Định dạng file không hợp lệ! Chỉ chấp nhận .jpg, .jpeg, .png, .gif';
                        return;
                    }
                    const maxSize = 10 * 1024 * 1024; // 10MB
                    if (selectedFile.size > maxSize) {
                        this.errorMessage = 'File quá lớn! Kích thước tối đa là 10MB.';
                        return;
                    }
                    this.file = selectedFile;
                    this.fileName = selectedFile.name;
                    const reader = new FileReader();
                    reader.onload = (e) => this.course.thumbnail = e.target.result;
                    reader.readAsDataURL(selectedFile);
                },

                async uploadImage() {
                    if (!this.file) {
                        Swal.fire('Lỗi', 'Vui lòng chọn một tệp trước khi tải lên!', 'error');
                        return;
                    }

                    if (this.errorMessage) {
                        Swal.fire('Lỗi', this.errorMessage, 'error');
                        return;
                    }
                    const formData = new FormData();
                    formData.append('file', this.file);
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/upload', {
                            method: 'POST',
                            body: formData
                        });
                        if (response) {
                            this.course.thumbnail = response.file ; // Cập nhật đường dẫn ảnh vào khóa học
                            await this.updateCourse(); // Tự động cập nhật khóa học sau khi upload ảnh
                            Swal.fire('Thành công', 'Hình ảnh đã được tải lên', 'success');
                        }
                    } catch (error) {
                        console.error('Lỗi khi tải ảnh:', error);
                        Swal.fire('Lỗi', 'Không thể tải lên hình ảnh', 'error');
                    }
                },
            });

            Alpine.store('course').getCourseCurrent();
            Alpine.store('course').getCategories();
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

        .course-status {
            font-weight: bold;
            margin-top: 15px;
            text-align: center;
            padding: 10px;
            border-top: 1px solid #ddd;
        }

        .status-badge {
            background: #ff9800;
            color: white;
            padding: 5px 10px;
            border-radius: 5px;
            font-size: 14px;
            display: inline-block;
            margin-top: 5px;
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
    <div class="sidebar" x-data>
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
            <input class="form-check-input" type="checkbox" onclick="updateUrlSegment('basics', 'curriculum')">
            <label class="form-check-label">Chương trình giảng dạy</label>
        </div>

        <h5 class="mt-4">Xuất bản khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" checked onclick="updateUrlSegment('basics', 'basics')">
            <label class="form-check-label">Trang tổng quan khóa học</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="updateUrlSegment('basics', 'price')">
            <label class="form-check-label">Định giá</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('/discount')">
            <label class="form-check-label">Khuyến mại</label>
        </div>

        <div class="course-status mt-4">
            Trạng thái khóa học: <span class="status-badge" x-text="$store.course.course.status"></span>
        </div>

        <!-- Ẩn nút "Gửi đi để xem xét" nếu trạng thái là PUBLIC -->
        <button class="btn btn-primary w-100 mt-3"
                x-show="$store.course.course.status && $store.course.course.status !== 'PUBLIC'"
                onclick="redirectToPage('review.html')">Gửi đi để xem xét</button>
    </div>
    <div class="content ms-4" x-data="Alpine.store('course')">
        <div class="form">
            <h2>Trang tổng quan khóa học</h2>
            <p>Trang tổng quan khóa học của bạn rất quan trọng đối với thành công trên Udemy.</p>

            <div class="mb-3">
                <label class="form-label">Tiêu đề khóa học</label>
                <input type="text" class="form-control" placeholder="Nhập tiêu đề (tối đa 80 ký tự)"
                       x-model="course.title" maxlength="80">
                <small class="text-muted" x-text="80 - course.title.length + ' ký tự còn lại'"></small>
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả khóa học</label>
                <textarea class="form-control" rows="4" x-model="course.description" placeholder="Chèn mô tả khóa học"></textarea>
                <small class="text-muted" x-text="1000 - (course.description ? course.description.length : 0) + ' ký tự còn lại'"></small>
            </div>

            <div class="form-group mt-3">
                <label for="categorySelect">Chọn thể loại</label>
                <select id="categorySelect" class="form-control" x-model="course.categoriesId">
                    <template x-for="category in categories" :key="category.id">
                        <option :value="category.id" x-text="category.name"></option>
                    </template>
                </select>
            </div>


            <button class="btn btn-primary mt-5" @click="updateCourse()">Lưu</button>
        </div>

        <div class="container mt-5">
            <h4>Thông tin cơ bản</h4>

            <h4 class="mt-4">Hình ảnh khóa học</h4>
            <div class="upload-container">
                <img :src="course.thumbnail || 'https://via.placeholder.com/250x150'" alt="Hình ảnh khóa học">
                <p>
                    Tải hình ảnh khóa học lên tại đây. Để được chấp nhận, hình ảnh phải đáp ứng
                    <a href="#">tiêu chuẩn chất lượng hình ảnh khóa học</a>.
                </p>
                <p>Hướng dẫn quan trọng: 750x422 pixel; .jpg, .jpeg, .gif, hoặc .png.</p>

                <div class="btn-upload">
                    <input type="file" class="custom-file-input" id="fileUpload" @change="handleFileUpload">
                    <label for="fileUpload" class="btn btn-outline-secondary" x-text="fileName">Không có file nào được chọn</label>
                    <button class="btn btn-primary" @click="uploadImage">Tải file lên</button>
                </div>

                <p class="text-danger" x-show="errorMessage" x-text="errorMessage"></p>
            </div>
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
