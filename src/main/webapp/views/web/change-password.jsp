<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>User Profile</title>
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
        .manh-slider {
            height: 100px !important;
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
                        <a href="/change-password" class="list-group-item list-group-item-action active"
                           data-bs-toggle="tab">
                            Thay đổi mật khẩu
                        </a>
                        <a href="logout.jsp" class="list-group-item list-group-item-action text-danger">
                            Đăng xuất
                        </a>
                    </div>
                </div>

                <!-- Nội dung bên phải -->
                <div class="col-7">
                    <div class="tab-content">
                        <!-- Cập nhật hồ sơ -->
                        <div class="tab-pane fade show active" id="profile">
                            <div class="card p-4">
                                <h4>Thay đổi mật khẩu</h4>
                                <span id="message" style="color: green; margin-bottom: 10px"></span>
                                <div id="errorMessage" style="color: red; margin-bottom: 10px"></div>
                                <form action="account" method="post">

                                    <div class="mb-3">
                                        <label for="currentPassword" class="form-label">Mật khẩu hiện tại</label>
                                        <input type="password" class="form-control" id="currentPassword"
                                               name="currentPassword"
                                               required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                        <input type="password" class="form-control" id="newPassword" name="newPassword"
                                               required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                                        <input type="password" class="form-control" id="confirmPassword"
                                               name="confirmPassword" required>
                                    </div>

                                    <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                                </form>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </section>

</main>
<%@include file="../../common/web/footer.jsp" %>

<!-- Scroll Up -->
<div id="back-top">
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>

<script type="module">

    import {environment, avatarDefault, STORAGE_KEY} from "../../assets/config/env.js";
    import {apiRequestWithToken} from "../../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', function () {
        const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
        if (userCurrent === null || userCurrent === '') {
            window.location.assign('/login');
            return;
        }

        async function changePassword(event) {
            event.preventDefault();
            const formData = new FormData(document.querySelector('form'));
            const currentPassword = formData.get('currentPassword');
            const newPassword = formData.get('newPassword');
            const confirmPassword = formData.get('confirmPassword');
            const errors = [];
            if (newPassword !== confirmPassword) errors.push('Mật khẩu mới không trùng khớp');
            const errDiv = document.querySelector('#errorMessage');
            if (errors.length > 0) {
                errDiv.textContent = errors.join(' ');
                return;
            }
            const payload = {
                currentPassword,
                newPassword,
                confirmPassword
            }

            apiRequestWithToken(environment.apiUrl + "/api/change-password",
                {
                    method: 'PUT',
                    body: JSON.stringify(payload),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    const message = document.getElementById('message');
                    message.textContent = "Đổi mật khẩu thành công";
                    const errorMessage = document.getElementById('errorMessage');
                    errorMessage.textContent = "";
                })
                .catch(error => {
                    const message = document.getElementById('message');
                    message.textContent = "";
                    const errorMessage = document.getElementById('errorMessage');
                    if (error.response?.status === 400 && error.data?.error) {
                        let errorMess = "";
                        for (const x of error.data.error) {
                            errorMess += x;
                        }
                        errorMessage.textContent = errorMess;
                    } else {
                        errorMessage.textContent = error.message || 'A network error occurred.';
                        console.error('Error:', error);
                    }
                });

        }

        document.querySelector('form').addEventListener('submit', changePassword);
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