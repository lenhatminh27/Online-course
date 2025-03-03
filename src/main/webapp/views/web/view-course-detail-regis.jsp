<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Courses | Education</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="assets/img/favicon.ico">

    <!-- CSS here -->
    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="../../assets/css/slicknav.css">
    <link rel="stylesheet" href="../../assets/css/animate.min.css">
    <link rel="stylesheet" href="../../assets/css/hamburgers.min.css">
    <link rel="stylesheet" href="../../assets/css/magnific-popup.css">
    <link rel="stylesheet" href="../../assets/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="../../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../../assets/css/slick.css">
    <link rel="stylesheet" href="../../assets/css/nice-select.css">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <link rel="stylesheet" href="../../assets/css/responsive.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>


    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Mulish', 'Josefin Sans', sans-serif, 'Arial', 'Helvetica', 'Times New Roman', 'serif' !important;
        }

        .blog-info-link li {
            font-size: 15px; /* Tăng kích thước chữ */
            font-weight: bold; /* Làm chữ đậm */
            padding: 12px 18px; /* Tăng khoảng cách cho từng mục */
            border-radius: 8px; /* Bo góc nhẹ */
            background-color: #f8f9fa; /* Màu nền nhẹ */
            display: inline-block;
            cursor: pointer;
            transition: all 0.3s ease-in-out;
        }

        /* Hiệu ứng khi hover */
        .blog-info-link li:hover {
            background-color: #e9ecef; /* Màu nền khi hover */
            transform: scale(1.05); /* Hiệu ứng phóng to nhẹ */
        }

        /* Tăng kích thước icon */
        .blog-info-link li i {
            font-size: 24px;
            margin-right: 8px;
        }


        .like-info {
            cursor: pointer;
            transition: transform 0.2s ease-in-out;
        }

        .like-info:hover {
            transform: scale(1.2); /* Slight zoom effect */
        }

        .like-info i {
            transition: color 0.2s ease-in-out;
        }

        .like-info:hover i {
            color: red; /* Changes color on hover */
        }

        .my-btn {
            border: none;
            background-color: transparent;
        }

        .my-btn i {
            color: black !important;
        }

        .comment {
            overflow-wrap: break-word;
            padding: 0px 10px;
        }

        .dropdown {
            display: flex;
            align-items: center;
        }

        .dropdown-menu {
            padding-left: 10px;
        }

        .dropdown-item-update {
            border-bottom: 1px solid #70777f;
            padding-bottom: 2px;
        }

        .dropdown-item-delete {
            color: red !important;
        }

        .fa-ellipsis-vertical {
            padding-right: 10px;
        }

        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }

        .right-section {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .price-box h2 {
            font-size: 2rem;
            color: #28a745;
        }

        .price-box .text-muted {
            font-size: 1rem;
        }

        .price-box .discount {
            font-size: 1.2rem;
            font-weight: bold;
            color: #ff5733;
        }

        .price-box .time-left {
            font-size: 1.2rem;
            color: #dc3545;
        }

        .price-box button {
            margin-bottom: 10px;
        }

        .course-details h4 {
            font-size: 1.2rem;
            color: #343a40;
            margin-bottom: 15px;
        }

        .course-details ul {
            list-style-type: none;
            padding-left: 0;
        }

        .course-details ul li {
            font-size: 1rem;
            margin-bottom: 10px;
        }

        .coupon-section {
            margin-top: 20px;
        }

        .coupon-section h5 {
            font-size: 1.2rem;
            color: #343a40;
        }

        .coupon-code input {
            margin-bottom: 10px;
        }

        .share-section button {
            margin-top: 10px;
        }

        .review-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
        }

        .review-header {
            font-size: 1.5rem;
            margin-bottom: 15px;
        }

        .review-card {
            background-color: #fff;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 15px;
        }

        .review-card-header {
            font-weight: bold;
        }

        .review-card-body {
            font-size: 1rem;
        }

        .show-all-btn {
            color: #007bff;
            background: none;
            border: none;
            cursor: pointer;
            font-weight: bold;
            margin-top: 20px;
        }

        .show-all-btn:hover {
            text-decoration: underline;
        }

        .modal-dialog {
            margin-left: 15vw;
            width: 70vw;
        }

        .modal-content {
            width: 70vw;
            left: 10px;
        }

        .fa-star {
            color: gold;
        }

        /* Màu sao rỗng là xám */
        .fa-star-o {
            color: #d3d3d3; /* Màu xám cho sao rỗng */
        }

        .star {
            font-size: 30px;
            color: #d3d3d3; /* Màu xám cho sao chưa chọn */
            cursor: pointer;
            transition: color 0.2s ease;
        }

        /* Hiển thị sao vàng khi hover */
        .star:hover,
        .star:hover ~ .star {
            color: gold;
        }

        /* Nếu sao đã được chọn, sao sẽ hiển thị màu vàng */
        .star.selected {
            color: gold;
        }
    </style>
</head>

<script type="module">
    import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';

    document.addEventListener('alpine:init', () => {

        const pathSegments = window.location.pathname.split('/');
        const menuSectionIndex = pathSegments.indexOf("course");
        let courseId = null;
        if (menuSectionIndex !== -1 && menuSectionIndex + 1 < pathSegments.length) {
            courseId = pathSegments[menuSectionIndex + 1];
            console.log("Course ID:", courseId);
        } else {
            console.error("Không tìm thấy courseId trong URL");
        }

        Alpine.store('courseDetail', {
            course: null,
            sections: [],
            lessons: [],
            currentLesson: null,
            ratings: [],
            topReviews: [],
            parentRating: [],

            async loadCourse() {
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/course/detail/" + courseId)
                    console.log(response);
                    this.course = response;
                } catch (e) {
                    console.log(e)
                }
            },

            async loadSections() {
                if (!courseId) {
                    console.error("Không có courseId, không thể gọi API!");
                    return;
                }
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + '/api/public-menu-section/' + courseId);
                    this.sections = response;
                    console.log(response);
                } catch (error) {
                    console.error("Lỗi khi gọi API:", error);
                }
            },

            async loadLesson(lessonId) {
                try {
                    const lesson = await apiRequestWithToken(environment.apiUrl + '/api/public-lesson/' + lessonId);
                    this.currentLesson = lesson;
                    console.log(this.currentLesson);
                } catch (error) {
                    console.error("Lỗi khi gọi API:", error);
                }
            },
            async loadTop2Reviews() {
                if (!courseId) {
                    console.error("Không có courseId, không thể gọi API!");
                    return;
                }
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/rating/top2/" + courseId);
                    this.topReviews = response; // Save reviews to Alpine store
                } catch (error) {
                    console.error("Lỗi khi gọi API:", error);
                }
            },
            async loadAllRating() {
                if (!courseId) {
                    console.error("Không có courseId, không thể gọi API!");
                    return;
                }
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/rating/" + courseId);
                    this.ratings = response; // Save reviews to Alpine store
                } catch (error) {
                    console.error("Lỗi khi gọi API:", error);
                }
            },
            async loadParentRating() {
                if (!courseId) {
                    console.error("Không có courseId, không thể gọi API!");
                    return;
                }
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/rating/parentRating/" + courseId);
                    this.parentRating = response; // Save reviews to Alpine store
                } catch (error) {
                    console.error("Lỗi khi gọi API:", error);
                }
            }
        });

        Alpine.store('courseDetail').loadCourse();
        Alpine.store('courseDetail').loadSections();
        Alpine.store('courseDetail').loadLesson();
        Alpine.store('courseDetail').loadTop2Reviews();
        Alpine.store('courseDetail').loadAllRating();
        Alpine.store('courseDetail').loadParentRating();
    })
    ;
</script>

<script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

<body>
<!--? Preloader Start -->
<div id="preloader-active">
    <div class="preloader d-flex align-items-center justify-content-center">
        <div class="preloader-inner position-relative">
            <div class="preloader-circle"></div>
            <div class="preloader-img pere-text">
                <img src="../../assets/img/logo/loder.png" alt="">
            </div>
        </div>
    </div>
</div>
<!-- Preloader Start -->
<!-- Header Start -->
<%@include file="../../common/web/header.jsp" %>
<!-- Header End -->
<main>
    <!--? slider Area Start-->
    <!--? Khu vực Slider Bắt đầu-->
    <section class="slider-area slider-area2">
        <div class="slider-active">
            <!-- Single Slider -->
            <div class="single-slider slider-height2">
                <div class="container">
                    <div class="row">
                        <div class="col-xl-8 col-lg-11 col-md-12">
                            <div class="hero__caption hero__caption2">
                                <h1 data-animation="bounceIn" data-delay="0.2s">Thông tin chi tiết khóa học</h1>
                                <!-- breadcrumb Bắt đầu-->
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="/home">Trang chủ</a></li>
                                        <li class="breadcrumb-item"><a href="#">Khóa học</a></li>
                                    </ol>
                                </nav>
                                <!-- breadcrumb Kết thúc -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--? Khu vực Slider Kết thúc-->


    <!--? Course detail Area Start-->
    <div class="container mt-5">
        <div class="row" x-data>
            <!-- Left Section -->
            <div class="col-md-8">
                <div class="course-title">
                    <h1 x-text="$store.courseDetail.course.title"></h1>
                    <p class="lead">4.6 sao (10000 xếp hạng) 500 học viên</p>
                    <p x-text="'Được tạo bởi: ' + $store.courseDetail.course.accountResponse.email" class="lead"></p>
                    <p x-text="'Sửa lần cuối vào: ' + new Intl.DateTimeFormat('vi-VN').format(new Date($store.courseDetail.course.updatedAt))"
                       class="lead"></p>
                </div>
                <div class="course-details card mt-5 ">
                    <h3 style="margin-top: 10px; margin-left:5px">Mục tiêu khóa học</h3>
                    <div class="row">
                        <!-- Left Column -->
                        <div class="col-md-6">
                            <ul>
                                <template
                                        x-for="(section, index) in $store.courseDetail.sections.slice(0, Math.ceil($store.courseDetail.sections.length / 2))"
                                        :key="section.id">
                                    <li class="ms-2" x-text="section.target">
                                    </li>
                                </template>
                            </ul>
                        </div>
                        <!-- Right Column -->
                        <div class="col-md-6">
                            <ul>
                                <template
                                        x-for="(section, index) in $store.courseDetail.sections.slice(Math.ceil($store.courseDetail.sections.length / 2))"
                                        :key="section.id">
                                    <li class="ms-2" x-text="section.target">
                                    </li>
                                </template>
                            </ul>
                        </div>
                    </div>
                </div>


                <div class="accordion mt-5" id="courseContentAccordion">
                    <h2>Nội dung khoá học</h2>
                    <template x-for="(section, index) in $store.courseDetail.sections" :key="index">
                        <div class="accordion" id="courseContent">
                            <div class="accordion-item">
                                <h2 class="accordion-header d-flex align-items-center" id="headingOne">
                                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                            :data-bs-target="'#collapseOne' + index" aria-expanded="false"
                                            aria-controls="collapseOne"
                                            x-text="section.title">
                                    </button>
                                </h2>

                                <div :id="'collapseOne' + index" class="accordion-collapse collapse"
                                     data-bs-parent="#courseContent">
                                    <template x-for="(lesson, lessonIndex) in section.menuLessons">
                                        <div class="d-flex align-items-center">
                                            <template x-if="lesson.isTrial">
                                                <div class="col-md-12">
                                                    <div style="color:cornflowerblue; display: inline-block;"
                                                         type="button"
                                                         class="accordion-body me-2" x-text="lesson.title"
                                                         @click="$store.courseDetail.loadLesson(lesson.id)"
                                                         data-bs-toggle="modal"
                                                         data-bs-target="#modal">
                                                    </div>

                                                    <p type="button"
                                                       style=" color: cornflowerblue ;display:flex;float: right; margin-top:13px"
                                                       @click="$store.courseDetail.loadLesson(lesson.id)"
                                                       data-bs-toggle="modal"
                                                       data-bs-target="#modal">

                                                        Xem trước
                                                    </p>
                                                </div>

                                            </template>
                                            <template x-if=" !lesson.isTrial">
                                                <div class="col-md-12">
                                                    <div type="button" class="accordion-body me-2"
                                                         x-text="lesson.title">
                                                    </div>
                                                </div>
                                            </template>
                                        </div>
                                    </template>
                                </div>
                            </div>
                        </div>
                    </template>
                </div>

                <div class="modal fade" id="modal" tabindex="-1">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="lesson-tittle" x-text="$store.courseDetail.currentLesson.title"></h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <template x-if="$store.courseDetail.currentLesson.videoUrl">
                                    <div>
                                        <video width="100%" controls preload="metadata">
                                            <source :src="$store.courseDetail.currentLesson.videoUrl" type="video/mp4">
                                            Trình duyệt của bạn không hỗ trợ video.
                                        </video>
                                    </div>
                                </template>
                                <template x-if="$store.courseDetail.currentLesson.article">
                                    <div class="article">
                                        <div x-html="$store.courseDetail.currentLesson.article"></div>
                                    </div>
                                </template>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="course-description mt-5">
                    <h3>Mô tả</h3>
                    <strong x-text="$store.courseDetail.course.description"></strong>
                </div>

                <div class="course-description mt-5">
                    <h3>Đánh giá</h3>
                    <!-- Form nhập bình luận -->
                    <div class="comment-form">

                        <div class="rating-stars mt-3">
                            <span class="star" data-value="1">&#9733;</span>
                            <span class="star" data-value="2">&#9733;</span>
                            <span class="star" data-value="3">&#9733;</span>
                            <span class="star" data-value="4">&#9733;</span>
                            <span class="star" data-value="5">&#9733;</span>
                        </div>
                        <!-- Hiển thị giá trị sao đã chọn -->
                        <div id="selectedRating" class="mt-2"></div>

                        <textarea class="form-control" id="commentInput" rows="3" placeholder="Nhập đánh giá của bạn..."
                                  required></textarea>

                        <button class="btn btn-primary mt-3" id="submitComment">Gửi đánh giá</button>
                    </div>
                </div>


                <div class="review-section mb-5">
                    <div class="review-header">
                        <span><strong>Các đánh giá trước</strong> </span>
                    </div>
                    <template x-for="(topReview, index) in $store.courseDetail.topReviews" :key="index">
                        <div class="review-card">
                            <div class="review-card-header">
                                <span x-text="topReview.account.email"></span>
                                <small x-text="new Date(topReview.createdAt).toLocaleDateString()"></small>
                            </div>
                            <div class="rating-stars">
                                <template x-for="starIndex in 5" :key="starIndex">
                                    <i :class="starIndex <= topReview.rating ? 'fa fa-star' : 'fa fa-star-o'"></i>
                                </template>
                            </div>
                            <div class="review-card-body">
                                <p x-text="topReview.review"></p> <!-- Hiển thị nội dung đánh giá -->
                            </div>
                        </div>
                    </template>


                    <!-- Button to open Modal with all reviews -->
                    <button class="show-all-btn" data-bs-toggle="modal" data-bs-target="#reviewsModal">Hiển thị tất cả
                        đánh giá
                    </button>
                </div>
            </div>

            <!-- Modal for displaying all reviews -->
            <div class="modal fade" id="reviewsModal" tabindex="-1" aria-labelledby="reviewsModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="reviewsModalLabel">Tất cả đánh giá</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <!-- Thanh tìm kiếm và Select option -->
                            <div class="d-flex mb-4">
                                <!-- Thanh tìm kiếm -->

                                <input type="text" class="form-control me-3" placeholder="Tìm kiếm đánh giá"
                                       id="searchInput">
                                <select class="form-select col-md-3" id="startSelect">
                                    <option value="1">1 sao</option>
                                    <option value="2">2 sao</option>
                                    <option value="3">3 sao</option>
                                    <option value="4">4 sao</option>
                                    <option value="5">5 sao</option>
                                </select>
                                <!-- Select option -->
                            </div>

                            <!-- Reviews inside the modal -->
                            <!-- Loop for parent reviews -->
                            <template x-for="(review, index) in $store.courseDetail.ratings" :key="index">
                                <div class="review-card">
                                    <div class="review-card-header">
                                        <span x-text="review.account.email"></span>
                                        <small x-text="new Date(review.createdAt).toLocaleDateString()"></small>
                                    </div>
                                    <div class="rating-stars">
                                        <template x-for="starIndex in 5" :key="starIndex">
                                            <i :class="starIndex <= review.rating ? 'fa fa-star' : 'fa fa-star-o'"></i>
                                        </template>
                                    </div>
                                    <div class="review-card-body">
                                        <p x-text="review.review"></p>
                                    </div>

                                    <!-- Child reviews under this parent review -->
                                    <div class="review-card-child">
                                        <template x-for="(childReview, childIndex) in review.RatingResponseChild"
                                                  :key="childIndex">
                                            <div class="review-card review-card-child">
                                                <div class="review-card-header">
                                                    <span x-text="childReview.account.email"></span>
                                                    <small x-text="new Date(childReview.createdAt).toLocaleDateString()"></small>
                                                </div>
                                                <div class="rating-stars">
                                                    <template x-for="starIndex in 5" :key="starIndex">
                                                        <i :class="starIndex <= childReview.rating ? 'fa fa-star' : 'fa fa-star-o'"></i>
                                                    </template>
                                                </div>
                                                <div class="review-card-body">
                                                    <p x-text="childReview.review"></p>
                                                </div>
                                            </div>
                                        </template>
                                    </div>
                                </div>
                            </template>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        </div>
                    </div>
                </div>


            </div>


            <!-- Right Section (Price and Purchase Button) -->
            <div class="col-md-4">
                <div class="right-section">
                    <!-- Thumbnail (Image) at the top -->
                    <div class="thumbnail">
                        <img src="https://ant.ncc.asia/wp-content/uploads/2024/08/image-214.png" class="img-fluid"
                             alt="Course Thumbnail"/>
                    </div>

                    <div class="price-box">
                        <h2>269.000 đ</h2>
                        <p class="text-muted"><strike>1.649.000 đ</strike></p>
                        <p class="discount">Giảm 84%!</p>
                        <p class="time-left">
                            <span class="badge badge-danger">7 giờ còn lại với mức giá này!</span>
                        </p>
                        <button class="btn btn-primary btn-lg btn-block">Mua ngay</button>
                    </div>

                    <%--                    <div class="course-details">--%>
                    <%--                        <h4>Khóa học này bao gồm:</h4>--%>
                    <%--                        <ul>--%>
                    <%--                            <li>33,5 giờ video theo yêu cầu</li>--%>
                    <%--                            <li>8 bài viết</li>--%>
                    <%--                            <li>8 tài nguyên có thể tải xuống</li>--%>
                    <%--                            <li>Truy cập trên thiết bị di động và TV</li>--%>
                    <%--                            <li>Quyền truy cập đầy đủ suốt đời</li>--%>
                    <%--                            <li>Phụ đề chuẩn</li>--%>
                    <%--                            <li>Giấy chứng nhận hoàn thành</li>--%>
                    <%--                        </ul>--%>
                    <%--                    </div>--%>

                    <div class="coupon-section">
                        <h5>Áp dụng coupon</h5>
                        <div class="coupon-code">
                            <input type="text" class="form-control" placeholder="Nhập coupon"/>
                            <button class="btn btn-info mt-2">Áp dụng</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>


    <!--? Course detail Area End-->
</main>


<!-- Course detail Area End -->
</main>
<%@include file="../../common/web/footer.jsp" %>

<!-- Scroll Up -->
<div id="back-top">
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>

<script>
    const stars = document.querySelectorAll('.star');
    let selectedRating = 0;

    // Xử lý khi người dùng di chuột vào sao (hiển thị sao vàng)
    stars.forEach(star => {
        star.addEventListener('mouseover', function () {
            const starValue = this.getAttribute('data-value');
            highlightStars(starValue);  // Làm sáng các sao từ 1 tới sao hiện tại
        });

        // Xử lý khi chuột rời khỏi sao (quay lại màu sao đã chọn)
        star.addEventListener('mouseout', function () {
            highlightStars(selectedRating);  // Quay lại màu sao đã chọn
        });

        // Xử lý khi click vào sao để chọn sao
        star.addEventListener('click', function () {
            selectedRating = this.getAttribute('data-value');
            updateStarRatingDisplay();  // Cập nhật màu sao đã chọn
            document.getElementById('selectedRating').textContent = 'Đánh giá: ' + selectedRating + ' sao';
        });
    });

    // Cập nhật hiển thị sao (để sao đã chọn có màu vàng)
    function updateStarRatingDisplay() {
        stars.forEach(star => {
            if (star.getAttribute('data-value') <= selectedRating) {
                star.classList.add('selected');  // Thêm lớp 'selected' để sao có màu vàng
            } else {
                star.classList.remove('selected');  // Xóa lớp 'selected' khi sao chưa chọn
            }
        });
    }

    // Làm sáng các sao khi di chuột (hover) qua sao
    function highlightStars(rating) {
        stars.forEach(star => {
            if (star.getAttribute('data-value') <= rating) {
                star.style.color = 'gold';  // Làm sáng sao vàng
            } else {
                star.style.color = '#d3d3d3';  // Màu xám cho sao chưa chọn
            }
        });
    }
</script>
<script src="../../assets/js/vendor/modernizr-3.5.0.min.js"></script>
<!-- Jquery, Popper, Bootstrap -->
<script src="../../assets/js/vendor/jquery-1.12.4.min.js"></script>
<script src="../../assets/js/popper.min.js"></script>
<script src="../../assets/js/bootstrap.min.js"></script>
<!-- Jquery Mobile Menu -->
<script src="../../assets/js/jquery.slicknav.min.js"></script>

<!-- Jquery Slick , Owl-Carousel Plugins -->
<script src="../../assets/js/owl.carousel.min.js"></script>
<script src="../../assets/js/slick.min.js"></script>
<!-- One Page, Animated-HeadLin -->
<script src="../../assets/js/wow.min.js"></script>
<script src="../../assets/js/animated.headline.js"></script>
<script src="../../assets/js/jquery.magnific-popup.js"></script>

<!-- Date Picker -->
<script src="../../assets/js/gijgo.min.js"></script>
<!-- Nice-select, sticky -->
<script src="../../assets/js/jquery.nice-select.min.js"></script>
<script src="../../assets/js/jquery.sticky.js"></script>

<!-- counter , waypoint,Hover Direction -->
<script src="../../assets/js/jquery.counterup.min.js"></script>
<script src="../../assets/js/waypoints.min.js"></script>
<script src="../../assets/js/jquery.countdown.min.js"></script>
<script src="../../assets/js/hover-direction-snake.min.js"></script>

<!-- contact js -->
<script src="../../assets/js/contact.js"></script>
<script src="../../assets/js/jquery.form.js"></script>
<script src="../../assets/js/jquery.validate.min.js"></script>
<script src="../../assets/js/mail-script.js"></script>
<script src="../../assets/js/jquery.ajaxchimp.min.js"></script>

<!-- Jquery Plugins, main Jquery -->
<script src="../../assets/js/plugins.js"></script>
<script src="../../assets/js/main.js"></script>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
        integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>

</body>
</html>