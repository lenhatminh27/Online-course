<!doctype html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title> App landing</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="manifest" href="site.webmanifest">
    <link rel="shortcut icon" type="image/x-icon" href="../assets/img/favicon.ico">

    <!-- CSS here -->
    <link rel="stylesheet" href="../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="../assets/css/slicknav.css">
    <link rel="stylesheet" href="../assets/css/flaticon.css">
    <link rel="stylesheet" href="../assets/css/progressbar_barfiller.css">
    <link rel="stylesheet" href="../assets/css/gijgo.css">
    <link rel="stylesheet" href="../assets/css/animate.min.css">
    <link rel="stylesheet" href="../assets/css/animated-headline.css">
    <link rel="stylesheet" href="../assets/css/magnific-popup.css">
    <link rel="stylesheet" href="../assets/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../assets/css/slick.css">
    <link rel="stylesheet" href="../assets/css/nice-select.css">
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body>
<!-- Preloader Start -->
<div id="preloader-active">
    <div class="preloader d-flex align-items-center justify-content-center">
        <div class="preloader-inner position-relative">
            <div class="preloader-circle"></div>
            <div class="preloader-img pere-text">
                <img src="assets/img/logo/loder.png" alt="">
            </div>
        </div>
    </div>
</div>
<!-- Preloader End -->

<!-- Main content -->
<main class="login-body" style="background: linear-gradient(to bottom, #e6e0ea, #b8c2ed);">
    <form class="form-default" id="reset-password-form">

        <div class="login-form">

            <!-- logo-login -->
            <div class="logo-login">
                <a href="index.html"><img src="assets/img/logo/loder.png" alt=""></a>
            </div>
            <h2>Mật khẩu mới</h2>
            <span id="message" style="color: green; margin-bottom: 10px"></span>
            <div id="errorMessage" style="color: red; margin-bottom: 10px"></div>
            <div class="form-input">
                <label for="password">Mật khẩu</label>
                <input type="password" name="password" id="password" placeholder="Nhập mật khẩu" required>
            </div>
            <div class="form-input">
                <label for="confirmPassword">Xác nhận mật khẩu</label>
                <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Xác nhận mật khẩu"
                       required>
            </div>

            <div class="form-input pt-30">
                <input type="submit" name="submit" value="Cập nhật mật khẩu">
            </div>
            <!-- Quay lại đăng nhập -->
            <a href="login" class="registration">Đăng nhập</a>
        </div>
    </form>
</main>

<script type="module">

    import {environment} from "../assets/config/env.js";
    import {apiRequestWithToken} from "../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', function () {
        async function resetPassword(event) {
            event.preventDefault();
            const formData = new FormData(document.querySelector('form'));
            const urlParams = new URLSearchParams(window.location.search);
            const token = urlParams.get('token');
            const password = formData.get('password');
            const confirmPassword = formData.get('confirmPassword');

            if (!password || !confirmPassword) {
                document.getElementById('errorMessage').textContent = "Mật khẩu không được để trống!";
                return;
            }

            if (password !== confirmPassword) {
                document.getElementById('errorMessage').textContent = "Mật khẩu không trùng khớp!";
                return;
            }

            const payload = {
                token,
                newPassword: password,
                confirmPassword: confirmPassword
            };

            try {
                const response = await fetch(environment.apiUrl + "/api/reset-password", {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const message = document.getElementById('message');
                    message.textContent = "";
                    const errorData = await response.json();
                    document.getElementById('errorMessage').textContent = errorData;
                    return;
                }

                document.getElementById('message').textContent = "Đặt lại mật khẩu thành công!";
                document.getElementById('errorMessage').textContent = "";

                setTimeout(() => {
                    window.location.assign('/login');
                }, 2000);

            } catch (error) {
                document.getElementById('errorMessage').textContent = error.message;
                document.getElementById('message').textContent = "";
            }
        }

        document.querySelector('form').addEventListener('submit', resetPassword);
    });
</script>


<script src="../assets/js/vendor/modernizr-3.5.0.min.js"></script>
<!-- Jquery, Popper, Bootstrap -->
<script src="../assets/js/vendor/jquery-1.12.4.min.js"></script>
<script src="../assets/js/popper.min.js"></script>
<script src="../assets/js/bootstrap.min.js"></script>
<!-- Jquery Mobile Menu -->
<script src="../assets/js/jquery.slicknav.min.js"></script>

<!-- Video bg -->
<script src="../assets/js/jquery.vide.js"></script>

<!-- Jquery Slick , Owl-Carousel Plugins -->
<script src="../assets/js/owl.carousel.min.js"></script>
<script src="../assets/js/slick.min.js"></script>
<!-- One Page, Animated-HeadLin -->
<script src="../assets/js/wow.min.js"></script>
<script src="../assets/js/animated.headline.js"></script>
<script src="../assets/js/jquery.magnific-popup.js"></script>

<!-- Date Picker -->
<script src="../assets/js/gijgo.min.js"></script>
<!-- Nice-select, sticky -->
<script src="../assets/js/jquery.nice-select.min.js"></script>
<script src="../assets/js/jquery.sticky.js"></script>
<!-- Progress -->
<script src="../assets/js/jquery.barfiller.js"></script>

<!-- counter , waypoint,Hover Direction -->
<script src="../assets/js/jquery.counterup.min.js"></script>
<script src="../assets/js/waypoints.min.js"></script>
<script src="../assets/js/jquery.countdown.min.js"></script>
<script src="../assets/js/hover-direction-snake.min.js"></script>

<!-- contact js -->
<script src="../assets/js/contact.js"></script>
<script src="../assets/js/jquery.form.js"></script>
<script src="../assets/js/jquery.validate.min.js"></script>
<script src="../assets/js/mail-script.js"></script>
<script src="../assets/js/jquery.ajaxchimp.min.js"></script>

<!-- Jquery Plugins, main Jquery -->
<script src="../assets/js/plugins.js"></script>
<script src="../assets/js/main.js"></script>

</body>
</html>
