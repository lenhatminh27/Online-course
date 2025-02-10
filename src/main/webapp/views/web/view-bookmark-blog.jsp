<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>View Bookmark Blog</title>
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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        .manh-slider{
            height: 100px !important;
        }
         * {
             box-sizing: border-box;
         }

        body {
            font-family: 'Mulish', 'Josefin Sans', sans-serif, 'Arial', 'Helvetica', 'Times New Roman', 'serif' !important;
        }

        .article {
            border-top: 1px solid rgba(0, 0, 0, 0.1);
            padding: 40px 0px;
        }

        .article p {
            margin-bottom: 0px;
        }

        /* Meta Section */
        .article__meta {
            display: flex;
            align-items: center;
        }

        .article__meta img {
            border-radius: 50%;
            width: 32px;
            height: 32px;
            margin-right: 8px;
            object-fit: cover;
        }

        .article__meta__info--author {
            text-decoration: none;
            color: #5CB85C;
        }

        .article__meta__info--author:hover {
            text-decoration: underline;
        }

        .article__meta__info--date {
            font-size: 12.8px;
            color: #bbb;
            margin-top: -4px;
        }

        /* Preview Link */
        .article__preview-link {
            margin-top: 10px;
        }

        .article__preview-link:hover {
            cursor: pointer;
        }

        .article__preview-link__title {
            font-size: 23px;
            font-weight: 600;
        }

        .article__preview-link__desc {
            font-size: 15px;
            color: #bbb;
            margin-top: 10px;
        }

        /* Footer */
        .article__preview-link__footer {
            padding-top: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
            color: #bbb;
        }

        .article__preview-link__footer--read-more {
            cursor: pointer;
        }

        /* Tags */
        .article__preview-link__footer__tag--tag-default {
            font-size: 12px;
            margin-right: 5px;
            padding: 4px 8px;
            border: 1px solid #bbb;
            border-radius: 30px;
            cursor: pointer;
        }

        /* Favorite Button */
        .favorite-btn {
            border: 2px solid #5CB85C;
            color: #5CB85C;
            background: none;
            padding: 5px 10px;
            font-size: 14px;
            cursor: pointer;
        }

        .favorite-btn:hover {
            color: #fff;
            background-color: #5CB85C;
        }

        /* Favorite Button Icon */
        .favorite-btn::before {
            content: "\f004";
            margin-right: 5px;
            font-family: "Font Awesome 5 Free";
        }

        /* Phần tử của mỗi trang */
        #pagination {
            display: flex;
            justify-content: center;
            list-style: none;
            padding: 0;
        }

        /* Các trang */
        .page-item {
            margin: 0 5px;
        }

        /* Các nút trang */
        .page-link {
            display: block;
            padding: 10px 15px;
            color: #007bff;
            text-decoration: none;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        /* Thêm hiệu ứng hover cho các nút */
        .page-link:hover {
            background-color: #007bff;
            color: #fff;
        }

        /* Các nút trang đã chọn */
        .page-item.active .page-link {
            background-color: #007bff;
            color: #fff;
            border-color: #007bff;
        }

        /* Các nút Previous và Next */
        .page-item .page-link[aria-label="Previous"],
        .page-item .page-link[aria-label="Next"] {
            font-weight: bold;
        }

        /* Các nút Disabled (khi không thể nhấn) */
        .page-item.disabled .page-link {
            color: #ccc;
            pointer-events: none;
        }

        .selected {
            background-color: #5cb85c;
            color: #fff;
            border-radius: 5px;
            padding: 3px 8px;
        }

        .tag-item {
            display: inline-block;
            padding: 8px 16px;
            background-color: #f1f1f1;
            color: #333;
            border-radius: 20px;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.3s, color 0.3s;
        }

        /* Hover effect for tag items */
        .tag-item:hover {
            background-color: #007bff;
            color: #fff;
        }

        /* Selected tag style */
        .tag-item.selected {
            background-color: #5cb85c;
            color: #fff;
        }

        /* Optional: Styling for when the tag is active (when clicked) */
        .tag-item:active {
            transform: scale(0.98); /* Small animation when clicked */
        }

        #search-recommendations{
            width: 310px;
        }
        .search-recommendation-box {
            position: absolute;
            width: 100%;
            background: white;
            border: 1px solid #ddd;
            max-height: 200px;
            overflow-y: auto;
            display: none;
            z-index: 1000;
            padding: 10px;
            border-radius: 5px;
        }

        .search-suggestion {
            padding: 8px;
            cursor: pointer;
            border-bottom: 1px solid #ddd;
            font-size: 14px;
            color: #333;
        }

        .search-suggestion:hover {
            background-color: #f1f1f1;
        }

        .bookmark-icon {
            font-size: 18px;
            color: #bbb;
            cursor: pointer;
            margin-left: auto;
            transition: color 0.3s ease;
        }

        .bookmark-icon:hover {
            color: #5CB85C;
        }
    </style>
</head>
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
            <div class="single-slider slider-height2 manh-slider">
            </div>
        </div>
    </section>
    <!--? Khu vực Slider Kết thúc-->

    <!-- Account Management -->
    <section class="blog_area section-padding">
        <div class="container">
            <div class="row">
                <!-- Menu bên trái -->
                <div class="col-3">
                    <div class="list-group">
                        <a href="/profile" class="list-group-item list-group-item-action" data-bs-toggle="tab">
                            Cập nhật hồ sơ
                        </a>
                        <a href="/change-password" class="list-group-item list-group-item-action" data-bs-toggle="tab">
                            Thay đổi mật khẩu
                        </a>
                        <a href="/boomarks" class="list-group-item list-group-item-action active" data-bs-toggle="tab">
                            Xem các bài viết đã đánh dấu
                        </a>
                    </div>
                </div>

                <!-- Nội dung bên phải -->
                <div class="col-7">
                    <div class="tab-content" id="blogs">
                        <!-- Cập nhật hồ sơ -->

                    </div>
                </div>
            </div>
        </div>
    </section>

</main>
<%@include file="../../common/web/footer.jsp" %>

<!-- Scroll Up -->
<div id="back-top" >
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>

<script type="module">
    import { environment, avatarDefault, STORAGE_KEY } from "../../assets/config/env.js";
    import { apiRequestWithToken } from "../../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', function () {
        const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
        if (!userCurrent) {
            window.location.assign('/login');
            return;
        }

        const blogsContainer = document.querySelector("#blogs");

        // Redirect to blog page when clicking on an article
        blogsContainer.addEventListener("click", function (event) {
            let article = event.target.closest(".article");
            if (article && !event.target.classList.contains("bookmark-icon")) {
                let slug = article.getAttribute("data-slug");
                if (slug) {
                    window.location.href = "/blog/" + slug;
                }
            }
        });

        // Load Bookmarked Blogs
        async function loadData() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + "/api/bookmarks", {
                    method: 'GET',
                });

                let htmls = "";
                for (let i = 0; i < response.length; i++) {
                    let blog = response[i];
                    let createDate = new Date(blog.createAt).toLocaleDateString();
                    let avatar = avatarDefault;
                    let bookmarkClass = "fa-solid"; // Assume all loaded blogs are bookmarked

                    htmls += "<div class='article' data-slug='" + blog.slug + "' data-id='" + blog.blogId + "'>";
                    htmls += "<div class='article__meta'>";
                    htmls += "<img src='" + avatar + "' alt='Author Avatar'/>";
                    htmls += "<div class='article__meta__info'>";
                    htmls += "<span class='article__meta__info--author'>" + blog.createBy + "</span>";
                    htmls += "<p class='article__meta__info--date'>" + createDate + "</p>";
                    htmls += "</div>";
                    htmls += "<i class='" + bookmarkClass + " fa-bookmark bookmark-icon'></i>";
                    htmls += "</div>";
                    htmls += "<div class='article__preview-link'>";
                    htmls += "<h1 class='article__preview-link__title'>" + blog.title + "</h1>";
                    htmls += "<div class='article__preview-link__footer'>";
                    htmls += "<span class='article__preview-link__footer--read-more'>Xem thêm...</span>";
                    htmls += "<div class='article__preview-link__footer__tags'></div>";
                    htmls += "</div>";
                    htmls += "</div>";
                    htmls += "</div>";
                }
                blogsContainer.innerHTML = htmls;
            } catch (error) {
                console.error('Error fetching bookmarks:', error);
            }
        }

        async function removeBookmark(id, iconElement) {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + "/api/bookmarks/" + id, {
                    method: "DELETE",
                    body: JSON.stringify({ blogId: id }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
                console.log(response);
            } catch (error) {
                console.error("Lỗi khi xóa bookmark:", error);
            }
        }

        // Event Listener for Bookmark Removal
        blogsContainer.addEventListener("click", async function (event) {
            if (event.target.classList.contains("bookmark-icon")) {
                let article = event.target.closest(".article");
                if (!article) return;

                let id = article.getAttribute("data-id");

                if (event.target.classList.contains("fa-solid")) {
                    Swal.fire({
                        title: "Bạn có muốn gỡ đánh dấu trang này không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Gỡ",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                        if (result.isConfirmed) {
                            await removeBookmark(id, event.target);
                            await loadData();
                        }
                    });
                }
            }
        });

        // Initial Load
        loadData();
    });
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

</body>
</html>