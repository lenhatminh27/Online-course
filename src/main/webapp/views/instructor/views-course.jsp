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

        document.addEventListener('alpine:init', function () {
            Alpine.store('courseList', {
                courses: [],
                search: '',
                sort: 'newest',
                totalPages: 1,
                page: 1,
                size: 5,
                async loadCourse() {
                    try {
                        const response = await apiRequestWithToken(
                            environment.apiUrl + '/api/course?page=' + this.page + '&size=' + this.size + '&search=' + this.search + '&sort=' + this.sort
                        );
                        this.courses = response.data;
                        this.totalPages = response.totalPages;
                    } catch (error) {
                        console.error('Lỗi khi tải khóa học:', error);
                    }
                },
                nextPage() {
                    if (this.page < this.totalPages) {
                        this.page++;
                        this.loadCourse();
                    }
                },
                prevPage() {
                    if (this.page > 1) {
                        this.page--;
                        this.loadCourse();
                    }
                },
                goToPage(p) {
                    this.page = p;
                    this.loadCourse();
                },
                changeSize(newSize) {
                    this.size = newSize;
                    this.page = 1; // Reset về trang đầu tiên
                    this.loadCourse();
                },
                changeSort(newSort) {
                    this.sort = newSort;
                    this.page = 1; // Reset về trang đầu tiên
                    this.loadCourse();
                }
            });

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
        /* Container chính */
        .container-body {
            margin-left: 350px !important;
            margin-right: 50px !important;
            background: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            padding: 20px;
            transition: all 0.3s ease-in-out;
        }

        /* Tiêu đề */
        h2 {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }

        .search-container {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .search-container input {
            border-radius: 8px;
            padding: 10px;
            border: 2px solid #6a0dad;
            transition: all 0.3s ease-in-out;
        }

        .search-container input:focus {
            border-color: #6a0dad;
            box-shadow: 0 0 5px rgba(106, 13, 173, 0.5);
        }

        .search-container button {
            background: #6a0dad;
            border: none;
            padding: 10px 12px;
            border-radius: 8px;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: background 0.3s ease-in-out;
        }

        .search-container button:hover {
            background: #5a099c;
        }

        .search-container select {
            border-radius: 8px;
            padding: 10px;
            border: 2px solid #6a0dad;
            background: white;
            transition: all 0.3s ease-in-out;
        }

        .search-container select:hover,
        .search-container select:focus {
            border-color: #5a099c;
        }

        .filter-container{
            display: flex;
            gap: 10px;
        }

        /* Card khóa học */
        .course-card {
            display: flex;
            align-items: center;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-top: 10px;
            background-color: white;
            transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
        }

        .course-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        /* Hình ảnh khóa học */
        .course-image {
            width: 80px;
            height: 80px;
            background-color: #f5f5f5;
            border-radius: 5px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 30px;
            color: #6a0dad;
        }

        /* Thông tin khóa học */
        .course-info {
            margin-left: 15px;
            flex-grow: 1;
        }

        .course-info h5 {
            font-size: 18px;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }

        .course-info small {
            color: #666;
        }

        /* Thanh tiến trình */
        .progress {
            height: 8px;
            border-radius: 4px;
            background-color: #ddd;
            overflow: hidden;
        }

        .progress-bar {
            background-color: #6a0dad;
            transition: width 0.5s ease-in-out;
        }

        /* Phân trang */
        .d-flex.justify-content-center {
            margin-top: 20px;
        }

        .select-sort{
            min-width: 106px !important;
        }

        .btn-sm {
            padding: 5px 12px;
            border-radius: 6px;
            transition: all 0.3s ease-in-out;
        }

        .btn-primary {
            background-color: #6a0dad;
            border: none;
        }

        .btn-primary:hover {
            background-color: #5a099c;
        }

        .btn-secondary {
            background-color: #ddd;
            border: none;
            color: #333;
        }

        .btn-secondary:hover {
            background-color: #bbb;
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
        <div class="d-flex mb-3 align-items-center search-container">
            <!-- Ô tìm kiếm -->
            <input type="text" class="form-control w-25" placeholder="Tìm kiếm khóa học của bạn"
                   x-model="$store.courseList.search"
                   @input.debounce.500ms="$store.courseList.loadCourse($store.courseList.search)">

            <!-- Dropdown lọc -->
            <div class="filter-container">
                <select x-model="$store.courseList.sort" @change="$store.courseList.changeSort($event.target.value)" class="form-control select-sort">
                    <option value="newest">Mới nhất</option>
                    <option value="oldest">Cũ nhất</option>
                    <option value="A-Z">A - Z</option>
                    <option value="Z-A">Z - A</option>
                </select>
                <select x-model="$store.courseList.size" @change="$store.courseList.changeSize($event.target.value)" class="form-control w-50">
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="15">15</option>
                    <option value="20">20</option>
                    <option value="25">25</option>
                </select>
            </div>

            <!-- Nút tạo khóa học -->
            <div class="ms-auto">
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

        <div class="d-flex justify-content-center mt-3">
            <template x-for="p in $store.courseList.totalPages" :key="p">
                <button class="btn btn-sm mx-1" :class="p === $store.courseList.page ? 'btn-primary' : 'btn-secondary'" @click="$store.courseList.goToPage(p)">
                    <span x-text="p"></span>
                </button>
            </template>
        </div>

    </div>

</div>

</body>
</html>