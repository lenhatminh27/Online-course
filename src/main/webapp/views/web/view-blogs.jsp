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
    <style>
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
            <div class="single-slider slider-height2">
                <div class="container">
                    <div class="row">
                        <div class="col-xl-8 col-lg-11 col-md-12">
                            <div class="hero__caption hero__caption2">
                                <h1 data-animation="bounceIn" data-delay="0.2s">Thông tin công ty</h1>
                                <!-- breadcrumb Bắt đầu-->
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="index.html">Trang chủ</a></li>
                                        <li class="breadcrumb-item"><a href="#">Blog</a></li>
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

    <!--? Blog Area Start-->
    <section class="blog_area section-padding">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mb-5 mb-lg-0">
                    <div class="blog_left_sidebar" id="blogs">
                    </div>
                    <div id="pagination">
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="blog_right_sidebar">
                        <aside class="single_sidebar_widget search_widget">
                            <form id="search-form" onsubmit="handleSearch(event)">
                                <div class="form-group">
                                    <div class="input-group mb-3">
                                        <input type="text" class="form-control" id="search-input"
                                               placeholder="Tìm kiếm bài viết"
                                               onfocus="fetchSearchRecommendations()"
                                               oninput="fetchSearchRecommendations()">
                                        <div class="input-group-append">
                                            <button class="btns" type="submit"><i class="ti-search"></i></button>
                                        </div>
                                    </div>
                                </div>
                                <div id="search-recommendations" class="search-recommendation-box"></div>
                                <button class="button rounded-0 primary-bg text-white w-100 btn_1 boxed-btn"
                                        type="submit">Tìm kiếm
                                </button>
                            </form>
                        </aside>
                        <aside class="single_sidebar_widget popular_post_widget" id="recent"></aside>
                        <aside class="single_sidebar_widget tag_cloud_widget" id="recent-tag">
                            <h4 class="widget_title" style="color: #2d2d2d;">Các từ khóa</h4>
                            <ul class="list">
                            </ul>
                        </aside>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Blog Area End -->
</main>
<%@include file="../../common/web/footer.jsp" %>

<!-- Scroll Up -->
<div id="back-top">
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>
<!-- JS here -->
<script type="module">
    import {environment, avatarDefault} from "../../assets/config/env.js";
    import {apiRequestWithToken} from "../../assets/config/service.js";

    let debounceTimer;


    document.addEventListener('DOMContentLoaded', function () {
        const urlParams = new URLSearchParams(window.location.search);
        const page = urlParams.has('page') ? parseInt(urlParams.get('page')) : 1;
        const searchQuery = urlParams.has('search') ? urlParams.get('search') : '';
        const tagsQuery = urlParams.has('tags') ? urlParams.get('tags') : '';

        document.querySelector("#blogs").addEventListener("click", function (event) {
            let article = event.target.closest(".article");
            if (article) {
                let slug = article.getAttribute("data-slug");
                window.location.href = "/blog/" + slug;
            }
        });

        document.querySelector('#recent-tag').addEventListener('click', function (event) {
            if (event.target.tagName.toLowerCase() === 'span') {
                let selectedTag = event.target.textContent.trim();
                let tags = urlParams.get('tags') ? urlParams.get('tags').split(',') : [];
                if (tags.includes(selectedTag)) {
                    tags = tags.filter(tag => tag !== selectedTag);
                    event.target.classList.remove('selected');
                } else {
                    tags.push(selectedTag);
                    event.target.classList.add('selected');
                }
                urlParams.set('tags', tags.join(','));
                window.history.pushState({}, '', '?' + urlParams.toString());
                loadBlogs(page, searchQuery, tags.join(','));
            }
        });

        document.addEventListener("click", function (event) {
            const searchInput = document.getElementById("search-input");
            const recommendationBox = document.getElementById("search-recommendations");

            if (!searchInput.contains(event.target) && !recommendationBox.contains(event.target)) {
                recommendationBox.style.display = "none";
            }
        });

        async function loadRecent() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + "/api/blogs/recent", {
                    method: 'GET',
                });
                console.log(response);

                let blogs = document.querySelector('#recent');
                let htmls = '<h3 class="widget_title" style="color: #2d2d2d;">Bài viết gần đây</h3>';

                response.forEach(function (blog) {
                    let createDate = new Date(blog.createAt);
                    let now = new Date();
                    let diffInMinutes = Math.floor((now - createDate) / 60000);

                    let timeAgo = '';
                    if (diffInMinutes < 1) {
                        timeAgo = 'Vừa xong';
                    } else if (diffInMinutes < 60) {
                        timeAgo = diffInMinutes + ' phút trước';
                    } else {
                        let diffInHours = Math.floor(diffInMinutes / 60);
                        timeAgo = diffInHours + ' giờ trước';
                    }
                    htmls +=
                        '<div class="media post_item">' +
                        '<div class="media-body">' +
                        '<a href="/blog/' + blog.slug + '">' +
                        '<h3 style="color: #2d2d2d;">' + blog.title + '</h3>' +
                        '</a>' +
                        '<p>' + timeAgo + '</p>' +
                        '</div>' +
                        '</div>';
                });
                blogs.innerHTML = htmls;
            } catch (error) {
                console.error('Có lỗi xảy ra:', error);
            }
        }

        async function loadRecentTags() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + "/api/tags/recent", {
                    method: 'GET',
                });

                console.log(response);
                let tags = document.querySelector('#recent-tag .list');
                let htmls = '';

                response.forEach(tag => {
                    htmls += '<li><span class="tag-item">' + tag.name + '</span></li>';
                });

                tags.innerHTML = htmls;
            } catch (error) {
                console.error('Có lỗi xảy ra:', error);
            }
        }

        async function loadBlogs(page, searchQuery, tags) {
            try {
                let url = environment.apiUrl + "/api/blogs?page=" + page;
                if (searchQuery) {
                    url += '&search=' + searchQuery;
                }
                if (tags) {
                    url += '&tags=' + tags;
                }
                const response = await apiRequestWithToken(url, {
                    method: 'GET',
                });
                console.log(response);
                let blogs = document.querySelector('#blogs');
                let htmls = "";
                for (let i = 0; i < response.data.length; i++) {
                    let blog = response.data[i];
                    let createDate = new Date(blog.createAt).toLocaleDateString();
                    let avatar = blog.accountResponse.avatar ? blog.accountResponse.avatar : avatarDefault;
                    htmls += "<div class='article' data-slug='" + blog.slug + "'>" +
                        "<div class='article__meta'>" +
                        "<img src='" + avatar + "' alt='Author Avatar'/>" +
                        "<div class='article__meta__info'>" +
                        "<span class='article__meta__info--author'>" +
                        blog.accountResponse.email +
                        "</span>" +
                        "<p class='article__meta__info--date'>" + createDate + "</p>" +
                        "</div>" +
                        "</div>" +
                        "<div class='article__preview-link'>" +
                        "<h1 class='article__preview-link__title'>" + blog.title + "</h1>" +
                        "<div class='article__preview-link__footer'>" +
                        "<span class='article__preview-link__footer--read-more'>Xem thêm...</span>" +
                        "<div class='article__preview-link__footer__tags'>";

                    for (let j = 0; j < blog.tagResponses.length; j++) {
                        let tag = blog.tagResponses[j];
                        htmls += "<span class='article__preview-link__footer__tag--tag-default'>" + tag.name + "</span>";
                    }

                    htmls += "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>";
                }

                blogs.innerHTML = htmls;
                const pagination = document.querySelector('#pagination');
                let paginationHtml = "";

                if (response.page > 1) {
                    paginationHtml += "<li class='page-item'><a href='#' class='page-link' onclick='updatePage(" + (response.page - 1) + ")'>Previous</a></li>";
                }

                for (let i = 1; i <= response.totalPages; i++) {
                    const activeClass = (i === response.page) ? 'active' : '';
                    paginationHtml += "<li class='page-item " + activeClass + "'><a href='#' class='page-link' onclick='updatePage(" + i + ")'>" + i + "</a></li>";
                }

                if (response.page < response.totalPages) {
                    paginationHtml += "<li class='page-item'><a href='#' class='page-link' onclick='updatePage(" + (response.page + 1) + ")'>Next</a></li>";
                }

                pagination.innerHTML = paginationHtml;
            } catch (error) {
                console.error('Error occurred:', error);
            }
        }

        let debounceTimer;

        async function fetchSearchRecommendations() {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(async () => {
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/search-recommendation", {
                        method: "GET",
                    });
                    displaySearchRecommendations(response.content);
                } catch (error) {
                    console.error("Error fetching search recommendations:", error);
                }
            }, 300);
        }

        function displaySearchRecommendations(recommendations) {
            const recommendationBox = document.getElementById("search-recommendations");
            recommendationBox.innerHTML = "";

            if (!recommendations || recommendations.length === 0) {
                recommendationBox.style.display = "none";
                return;
            }

            recommendationBox.style.display = "block";

            recommendations.forEach(item => {
                const suggestionItem = document.createElement("div");
                suggestionItem.classList.add("search-suggestion");
                suggestionItem.innerText = item;
                suggestionItem.onclick = () => {
                    document.getElementById("search-input").value = item;
                    recommendationBox.style.display = "none";
                };
                recommendationBox.appendChild(suggestionItem);
            });
        }

        window.fetchSearchRecommendations = fetchSearchRecommendations;

        window.updatePage = function (page) {
            const urlParams = new URLSearchParams(window.location.search);
            const searchQuery = document.getElementById('search-input').value;
            const tagsQuery = urlParams.get('tags') || '';
            urlParams.set('page', page);
            if (searchQuery) {
                urlParams.set('search', searchQuery);
            }
            if (tagsQuery) {
                urlParams.set('tags', tagsQuery);
            }
            window.history.pushState({}, '', '?' + urlParams.toString());
            loadBlogs(page, searchQuery, tagsQuery);
        };

        window.handleSearch = function handleSearch(event) {
            event.preventDefault();
            const searchQuery = document.getElementById('search-input').value;
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('search', searchQuery);
            window.history.pushState({}, '', '?' + urlParams.toString());
            loadBlogs(1, searchQuery);
        };

        loadBlogs(page, searchQuery, tagsQuery);
        loadRecent();
        loadRecentTags();
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