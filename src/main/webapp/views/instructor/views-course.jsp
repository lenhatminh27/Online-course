<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Instructor</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" rel="stylesheet">
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
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
        });
    </script>
    <script type="module">
        import { environment, STORAGE_KEY, avatarDefault } from '../../assets/config/env.js';
        import { apiRequestWithToken } from '../../assets/config/service.js';

        document.addEventListener('alpine:init', () => {
            Alpine.store('courseList', {
                courses: [],
                search: '',
                async loadCourse(searchTerm = '') {
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/course?search=' + searchTerm);
                        this.courses = response;
                    } catch (error) {
                        console.error('Lỗi khi tải khóa học:', error);
                    }
                }
            });
            // Gọi loadCourse ngay sau khi Alpine khởi tạo
            Alpine.store('courseList').loadCourse();
        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
    <style>
        .sidebar {
            width: 250px;
            background: #f44336;
            color: white;
            padding: 20px;
            min-height: 100vh;
        }
        .container-body {
            margin-left: 350px !important;
            margin-right: 50px !important;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .progress-bar {
            background-color: #6a0dad;
        }
        .course-card {
            display: flex;
            align-items: center;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-top: 10px;
            background-color: white;
        }
        .course-image {
            width: 80px;
            height: 80px;
            background-color: #f5f5f5;
            border-radius: 5px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 30px;
        }
        .course-info {
            margin-left: 15px;
            flex-grow: 1;
        }
    </style>

</head>
<body>

<!-- Sidebar/menu -->
<%@include file="../../common/instructor/menu.jsp" %>

<!-- !PAGE CONTENT! -->
<div class="w3-main">

    <%@include file="../../common/instructor/header.jsp" %>
    <div class="container-body" x-data x-init="$store.courseList.loadCourse()">
        <h2 class="mb-3">Khóa học</h2>
        <div class="d-flex mb-3 align-items-center">
            <input type="text" class="form-control w-50" placeholder="Tìm kiếm khóa học của bạn"
                   x-model="$store.courseList.search"
                   @input.debounce.500ms="$store.courseList.loadCourse($store.courseList.search)">
            <button class="btn btn-danger ms-2"><i class="fas fa-search"></i></button>
            <button class="btn btn-outline-danger ms-3">Mới nhất <i class="fas fa-chevron-down"></i></button>
            <!-- Đẩy phần tử này sang bên phải -->
            <div class="ms-auto d-flex">
                <button class="btn btn-success" onclick="location.href='/instructor/create-course'">Tạo khóa học</button>
            </div>
        </div>

        <template x-for="course in $store.courseList.courses" :key="course.id">
            <a :href="'/instructor/curriculum/' + course.id" class="text-decoration-none">
                <div class="course-card">
                    <div class="course-image"><i class="fas fa-book"></i></div>
                    <div class="course-info">
                        <h5 x-text="course.title"></h5>
                        <small><strong>Ngày tạo:</strong> <span x-text="course.createdAt"></span></small>
                        <div class="progress mt-2">
                            <div class="progress-bar" role="progressbar" style="width: 50%" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                        <small>Tiến trình khóa học</small>
                    </div>
                </div>
            </a>
        </template>

    </div>

</div>

</body>
</html>