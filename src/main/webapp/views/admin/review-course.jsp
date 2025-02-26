<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
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

    <style>
        #pagination {
            margin-top: 50px;
            display: flex;
            justify-content: center;
            list-style: none;
            padding: 0;
        }

        .d-flex {
            display: flex;
        }

        .align-items-center {
            align-items: center;
        }

        .justify-content-between {
            justify-content: space-between;
        }

        .mb-4 {
            margin-bottom: 1.5rem;
        }

        .form-control {
            height: 38px; /* Điều chỉnh chiều cao cho input và select */
        }

        .search-input {
            width: 200px; /* Điều chỉnh chiều rộng của input tìm kiếm */
        }

        .sort-select {
            width: 150px; /* Điều chỉnh chiều rộng của select */
        }

        .ml-2 {
            margin-left: 0.5rem; /* Khoảng cách giữa các phần tử */
        }

        .btn {
            height: 38px; /* Đảm bảo nút có cùng chiều cao với ô input */
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 0 15px; /* Khoảng cách trái phải của nút */
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-primary:hover {
            background-color: #0056b3;
        }

    </style>
</head>

<body>
<div class="container-xxl position-relative bg-white d-flex p-0">
    <!-- Spinner Start -->
    <div id="spinner"
         class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
        <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
    <!-- Spinner End -->


    <!-- Sidebar Start -->
    <%@include file="../../common/admin/menu.jsp" %>
    <!-- Sidebar End -->


    <!-- Content Start -->
    <div class="content">
        <%@include file="../../common/admin/header.jsp" %>

        <!-- Course Start -->
        <div class="container-fluid pt-4 px-4">
            <div class="bg-light text-center rounded p-4">
                <div class="d-flex align-items-center justify-content-between mb-4">
                    <h5>Khóa học chờ xét duyệt</h5>
                    <form class="d-flex align-items-center" onsubmit="handleSearch3(event)">
                        <input type="text" class="form-control search-input" placeholder="Tìm kiếm..."
                               name="search" value="${param.search}">
                        <select class="form-control sort-select ml-2" name="sort">
                            <option value="newest" ${param.sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="oldest" ${param.sort == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                            <option value="increase-price" ${param.sort == 'increase-price' ? 'selected' : ''}>Giá tăng
                                dần
                            </option>
                            <option value="decrease-price" ${param.sort == 'decrease-price' ? 'selected' : ''}>Giá giảm
                                dần
                            </option>
                        </select>
                        <button type="submit" class="btn btn-primary ml-2">Tìm kiếm</button>
                    </form>

                </div>


                <div class="table-responsive">
                    <table class="table text-start align-middle table-bordered table-hover mb-0">
                        <thead>
                        <tr class="text-dark">
                            <th scope="col">Ngày tạo</th>
                            <th scope="col">Tác giả</th>
                            <th scope="col">Thể Loại</th>
                            <th scope="col">Tên khóa học</th>
                            <th scope="col">Giá tiền</th>
                            <th scope="col">Chi tiết</th>
                        </tr>
                        </thead>
                        <tbody id="courses-table-body">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div id="pagination">
        </div>
        <!-- Course End -->


        <!-- Footer Start -->
        <%@include file="../../common/admin/footer.jsp" %>
        <!-- Footer End -->
    </div>
    <!-- Content End -->


    <!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
</div>

<script type="module">

    import {environment, avatarDefault, STORAGE_KEY} from "../../assets/config/env.js";
    import {apiRequestWithToken} from "../../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', async function () {
        const urlParams = new URLSearchParams(window.location.search);
        const page = urlParams.has('page') ? parseInt(urlParams.get('page')) : 1;
        const searchQuery = urlParams.has('search') ? urlParams.get('search') : '';
        const sort = urlParams.get('sort') || 'newest';

        function formatDate(dateString) {
            const date = new Date(dateString);
            return date.toLocaleDateString("vi-VN", {
                day: "2-digit",
                month: "2-digit",
                year: "numeric"
            });
        }


        window.updateCoursePage3 = function (page) {
            const urlParams = new URLSearchParams(window.location.search);
            const searchQuery = document.querySelector('input[name="search"]').value;
            const sort = document.querySelector('select[name="sort"]').value;

            urlParams.set('page', page);
            if (searchQuery) urlParams.set('search', searchQuery);
            if (sort) urlParams.set('sort', sort);

            window.history.pushState({}, '', '?' + urlParams.toString());
            loadCourses(page, searchQuery, sort);
        };

        window.handleSearch3 = function handleSearch(event) {
            event.preventDefault();
            const searchQuery = document.querySelector('input[name="search"]').value;
            const sort = document.querySelector('select[name="sort"]').value;
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('search', searchQuery);
            urlParams.set('sort', sort);
            window.history.pushState({}, '', '?' + urlParams.toString());
            loadCourses(1, searchQuery, sort);
        };

        async function loadCourses(page, searchQuery, sort) {
            try {
                let url = environment.apiUrl + "/api/review-course?page=" + page;
                if (searchQuery) {
                    url += '&search=' + searchQuery;
                }
                if (sort) {
                    url += '&sort=' + sort;
                }
                const response = await apiRequestWithToken(url, {
                    method: 'GET',
                });

                loadCoursesData(response.data);
                console.log(response);

                const pagination = document.querySelector('#pagination');
                let paginationHtml = "";

                if (response.page > 1) {
                    paginationHtml += "<li class='page-item'><a href='#' class='page-link' onclick='updateCoursePage3(" + (response.page - 1) + ")'>Trước</a></li>";
                }

                for (let i = 1; i <= response.totalPages; i++) {
                    const activeClass = (i === response.page) ? 'active' : '';
                    paginationHtml += "<li class='page-item " + activeClass + "'><a href='#' class='page-link' onclick='updateCoursePage3(" + i + ")'>" + i + "</a></li>";
                }

                if (response.page < response.totalPages) {
                    paginationHtml += "<li class='page-item'><a href='#' class='page-link' onclick='updateCoursePage3(" + (response.page + 1) + ")'>Sau</a></li>";
                }

                pagination.innerHTML = paginationHtml;

            } catch (error) {
                console.error('Error occurred:', error);
            }
        }

        function loadCoursesData(data) {
            const tableBody = document.getElementById('courses-table-body');
            tableBody.innerHTML = '';

            data.forEach(function (course) {
                const row = document.createElement('tr');
                console.log(course);
                console.log(course.categories);
                let categoryList = course.categories ? course.categories.map(category => category.name).join(', ') : '';

                console.log(categoryList);

                // Nối chuỗi cho nội dung từng dòng
                let rowContent = "<td>" + formatDate(course.createdAt) + "</td>";
                rowContent += "<td>" + course.accountResponse.email + "</td>";
                rowContent += "<td>" + categoryList + "</td>";
                rowContent += "<td>" + course.title + "</td>";
                rowContent += "<td>" + course.price.toLocaleString() + " VND</td>";

                let detailUrl = "/admin/review-course-detail/" + course.id;
                rowContent += "<td>" +
                    "<a class='btn btn-sm btn-primary' href='" + detailUrl + "'>Chi tiết</a>" +
                    "</td>";

                row.innerHTML = rowContent;
                tableBody.appendChild(row);
            });
        }


        await loadCourses(page, searchQuery, sort);

    });

</script>
<!-- JavaScript Libraries -->
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
