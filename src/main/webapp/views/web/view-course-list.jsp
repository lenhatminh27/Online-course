<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Courses | Education</title>
    <meta name="description" content="Danh sách khóa học">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="assets/img/favicon.ico">

    <!-- CSS here -->
    <link rel="stylesheet" href="../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="../assets/css/slicknav.css">
    <link rel="stylesheet" href="../assets/css/animate.min.css">
    <link rel="stylesheet" href="../assets/css/hamburgers.min.css">
    <link rel="stylesheet" href="../assets/css/magnific-popup.css">
    <link rel="stylesheet" href="../assets/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../assets/css/slick.css">
    <link rel="stylesheet" href="../assets/css/nice-select.css">
    <link rel="stylesheet" href="../assets/css/style.css">
    <link rel="stylesheet" href="../assets/css/responsive.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Mulish', 'Josefin Sans', sans-serif, 'Arial', 'Helvetica', 'Times New Roman', 'serif' !important;
        }

        h1 {
            font-size: 60px;
            color: #dc3545;
        }

        p {
            font-size: 18px;
            color: #6c757d;
        }

        .error-area {
            margin-bottom: 100px;
        }

        .error-container {
            margin-top: 10%;
        }

        .btn-home {
            margin-top: 20px;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
        }

        .btn-home:hover {
            background-color: #0056b3;
        }

        .search-container {
            display: flex;
            align-items: center;
            justify-content: flex-start;
            gap: 20px;
        }

        .filter-container {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .select-sort,
        .w-50 {
            width: auto;
        }

        .container-body {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            gap: 30px;
            padding: 0 15px;
        }

        .properties__card {
            width: calc(30% - 20px);
            margin-bottom: 30px;
            font-size: 14px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            background-color: #fff;
        }

        .properties__img img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }

        .properties__caption {
            padding: 15px;
            text-align: center;
        }

        .properties__caption h3 {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .properties__caption p {
            font-size: 12px;
            color: #777;
            margin-bottom: 10px;
        }

        .properties__footer {
            font-size: 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .price {
            font-size: 14px;
            font-weight: bold;
        }

        .border-btn {
            font-size: 12px;
            padding: 8px 16px;
            width: 100%;
            text-align: center;
        }

        .bookmark-icon {
            font-size: 18px;
            position: absolute;
            top: 10px;
            right: 10px;
            cursor: pointer;
        }

        .heart {
            font-size: 2rem;
            cursor: pointer;
            transition: color 0.3s ease;
        }

        .liked {
            color: red;
        }

        .unliked {
            color: gray;
        }
    </style>
    <script type="module">
        import {environment, avatarDefault, STORAGE_KEY} from "../../assets/config/env.js";
        import {apiRequestWithToken} from "../../assets/config/service.js";

        document.addEventListener('alpine:init', function () {
            Alpine.store('courses', {
                courses: [],
                search: '',
                sort: 'newest',
                totalPages: 1,
                page: 1,
                size: 6,

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
                    this.page = 1;
                    this.loadCourse();
                },
                changeSort(newSort) {
                    this.sort = newSort;
                    this.page = 1;
                    this.loadCourse();
                },
                async loadCourse() {
                    try {
                        const response = await apiRequestWithToken(
                            '/api/view-course-list?page=' + this.page + '&size=' + this.size + '&search=' + this.search + '&sort=' + this.sort
                        );
                        this.courses = response.data.map(course => {
                            return { ...course, isLiked: course.isWishlist };
                        });
                        this.totalPages = response.totalPages;
                        console.log(response);
                    } catch (error) {
                        console.error('Lỗi khi tải khóa học:', error);
                    }
                },

                async toggleLike(courseId) {
                    const course = this.courses.find(course => course.id === courseId);
                    if (course) {
                        if (course.isLiked) {
                            await this.dislikeCourse(courseId);
                        } else {
                            await this.likeCourse(courseId);
                        }
                    }
                },

                async likeCourse(courseId) {
                    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);

                    if (!userCurrent) {
                        Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: "Bạn phải đăng nhập để thực hiện thao tác này!",
                            footer: '<a href="login">Đăng nhập</a>'
                        });
                        return;
                    }

                    try {
                        const course = this.courses.find(course => course.id === courseId);
                        if (!course) return;
                        course.isLiked = true;

                        await apiRequestWithToken(
                            environment.apiUrl + "/api/wishlist/" + courseId,
                            {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                },
                            }
                        );
                    } catch (error) {
                        console.error('Lỗi khi thực hiện thao tác yêu thích:', error);
                    }
                },

                async dislikeCourse(courseId) {
                    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
                    if (!userCurrent) return;
                    console.log(userCurrent);

                    try {
                        const course = this.courses.find(course => course.id === courseId);
                        if (!course) return;
                        course.isLiked = false;

                        await apiRequestWithToken(
                            environment.apiUrl + "/api/wishlist/" + courseId,
                            {
                                method: "DELETE",
                                headers: {
                                    "Content-Type": "application/json",
                                },
                            }
                        );
                    } catch (error) {
                        console.error('Lỗi khi thực hiện thao tác không thích:', error);
                    }
                }
            });

            Alpine.store('courses').loadCourse();
        });

    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
</head>
<body x-data>
<!-- Preloader -->
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

<!-- Header -->
<%@include file="../../common/web/header.jsp" %>

<main>
    <section class="slider-area slider-area2">
        <div class="slider-active">
            <div class="single-slider slider-height2">
                <div class="container">
                    <div class="row">
                        <div class="col-xl-8 col-lg-11 col-md-12">
                            <div class="hero__caption hero__caption2">
                                <h1 data-animation="bounceIn" data-delay="0.2s">Chào mừng bạn đến với các khóa học</h1>
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="home">Trang chủ</a></li>
                                        <li class="breadcrumb-item"><a href="wishlist">Danh sách khóa học yêu thích</a>
                                        </li>
                                        <li class="breadcrumb-item"><a href="#">Đang học</a></li>
                                    </ol>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <div>
        <h2 class="mb-3">Khóa học</h2>
        <div class="d-flex mb-3 align-items-center search-container">
            <input type="text" class="form-control w-25" placeholder="Tìm kiếm khóa học của bạn"
                   x-model="$store.courses.search"
                   @input.debounce.500ms="$store.courses.loadCourse()">
            <div class="filter-container">

            </div>
        </div>

        <div class="container-body">
            <template x-for="course in $store.courses.courses" :key="course.id">
                <div class="properties__card" :data-id="course.id">
                    <div class="properties__img">
                        <a :href="'/courses/' + course.id">
                            <img :src="course.thumbnail" alt="">
                        </a>
                    </div>
                    <div class="properties__caption">
                        <p>Khóa học hữu ích</p>
                        <h3><a href="#" x-text="course.title"></a></h3>
                        <p x-text="course.description"></p>
                        <div class="properties__footer d-flex justify-content-between align-items-center">
                            <div class="restaurant-name">
                                <div class="rating">
                                    <div x-data="{ liked: course.isLiked }">
                                        <span
                                            class="heart"
                                            :class="liked ? 'liked' : 'unliked'"
                                            @click="
                                            $store.courses.toggleLike(course.id);
                                            liked = !liked;
                                            "
                                        >
                                            ❤
                                        </span>
                                    </div>

                                </div>
                                <span x-text="'(' + course.rating + ')'"></span>
                                <template x-for="n in 5" :key="n">
                                    <i :class="n <= course.rating ? 'fas fa-star' : 'fas fa-star-half-alt'"></i>
                                </template>
                            </div>
                            <div class="price">
                                <span x-text="course.price + ' VNĐ'"></span>
                            </div>
                        </div>
                        <a :href="'/courses/' + course.id" class="border-btn">Tìm hiểu thêm</a>
                    </div>
                    <i :class="course.isLiked ? 'fa fa-heart bookmark-icon text-danger' : 'fa fa-heart bookmark-icon'"></i>
                </div>
            </template>
        </div>

        <div class="d-flex justify-content-center mt-3">
            <template x-for="p in $store.courses.totalPages" :key="p">
                <button class="btn btn-sm mx-1" :class="p === $store.courses.page ? 'btn-primary' : 'btn-secondary'"
                        @click="$store.courses.goToPage(p)">
                    <span x-text="p"></span>
                </button>
            </template>
        </div>
    </div>
</main>

<%@include file="../../common/web/footer.jsp" %>

<!-- Scroll Up -->
<div id="back-top">
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>

<script src="../assets/js/vendor/modernizr-3.5.0.min.js"></script>
<script src="../assets/js/vendor/jquery-1.12.4.min.js"></script>
<script src="../assets/js/popper.min.js"></script>
<script src="../assets/js/bootstrap.min.js"></script>
<script src="../assets/js/jquery.slicknav.min.js"></script>
<script src="../assets/js/owl.carousel.min.js"></script>
<script src="../assets/js/slick.min.js"></script>
<script src="../assets/js/wow.min.js"></script>
<script src="../assets/js/animated.headline.js"></script>
<script src="../assets/js/jquery.magnific-popup.js"></script>
<script src="../assets/js/gijgo.min.js"></script>
<script src="../assets/js/jquery.nice-select.min.js"></script>
<script src="../assets/js/jquery.sticky.js"></script>
<script src="../assets/js/jquery.counterup.min.js"></script>
<script src="../assets/js/waypoints.min.js"></script>
<script src="../assets/js/jquery.countdown.min.js"></script>
<script src="../assets/js/hover-direction-snake.min.js"></script>
<script src="../assets/js/contact.js"></script>
<script src="../assets/js/jquery.form.js"></script>
<script src="../assets/js/jquery.validate.min.js"></script>
<script src="../assets/js/mail-script.js"></script>
<script src="../assets/js/jquery.ajaxchimp.min.js"></script>
<script src="../assets/js/plugins.js"></script>
<script src="../assets/js/main.js"></script>

</body>
</html>
