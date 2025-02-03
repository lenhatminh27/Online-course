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
<!-- ? Preloader Start -->
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
<!-- Preloader Start-->


<!-- Register -->

<main class="login-body" data-vide-bg="assets/img/login-bg.mp4">
    <!-- Đăng ký Admin -->
    <form class="form-default" id="register-form">

        <div class="login-form">

            <!-- logo-login -->
            <div class="logo-login">
                <a href="index.html"><img src="assets/img/logo/loder.png" alt=""></a>
            </div>
            <h2>Đăng Ký Tại Đây</h2>
            <div id="error">

            </div>
            <div class="form-input">
                <label for="firstname">Tên</label>
                <input type="text" name="name" id="firstname" placeholder="Tên">
            </div>
            <div class="form-input">
                <label for="lastname">Họ</label>
                <input type="text" name="name" id="lastname" placeholder="Họ">
            </div>
            <div class="form-input">
                <label for="email">Địa chỉ Email</label>
                <input type="email" name="email" id="email" placeholder="Địa chỉ Email">
            </div>
            <div class="form-input">
                <label for="password">Mật khẩu</label>
                <input type="password" name="password" id="password" placeholder="Mật khẩu">
            </div>
            <div class="form-input">
                <label for="confirmpassword">Xác nhận mật khẩu</label>
                <input type="password" name="password" id="confirmpassword" placeholder="Xác nhận mật khẩu">
            </div>
            <div class="form-input pt-30">
                <input type="submit" name="submit" value="Đăng Ký">
            </div>
            <!-- Quên mật khẩu -->
            <a href="login" class="registration">Đăng nhập</a>
        </div>
    </form>
    <!-- /kết thúc form đăng ký -->
</main>


<script type="module">

    import {environment} from '../assets/config/env.js';

    document.addEventListener('DOMContentLoaded', function() {
        const registerForm = document.getElementById('register-form');
        const errorMessageDiv = document.querySelector('#error');
        errorMessageDiv.style.color = 'red';

        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const firstName = document.getElementById('firstname').value;
            const lastName = document.getElementById('lastname').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmpassword').value;

            // Validate the inputs
            let errors = [];
            if (!firstName) errors.push('First name is required');
            if (!lastName) errors.push('Last name is required');
            if (!email) errors.push('Email is required');
            if (!password) errors.push('Password is required');
            if (password !== confirmPassword) errors.push('Passwords do not match');

            if (errors.length > 0) {
                errorMessageDiv.textContent = errors.join(', ');
                return;
            }

            const data = {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
                confirmPassword: confirmPassword
            };

            try {
                const response = await fetch(environment.apiUrl + '/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    window.location.href = '/login';
                } else {
                    const errorData = await response.json();
                    let errorMess = "";
                    for (const x of errorData.error) {
                        errorMess += x;
                    }
                    errorMessageDiv.textContent = errorMess;
                }
            } catch (error) {
                errorMessageDiv.textContent = 'An error occurred. Please try again later.';
            }
        });
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