<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
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


<main class="login-body" data-vide-bg="assets/img/login-bg.mp4">
    <!-- Đăng nhập Admin -->
    <form class="form-default" id="login-form">
        <div class="login-form">
            <!-- logo-login -->
            <div class="logo-login">
                <a href="index.html"><img src="assets/img/logo/loder.png" alt=""></a>
            </div>
            <h2>Đăng Nhập Tại Đây</h2>
            <!-- Container thông báo lỗi -->
            <div id="error-message" class="error-message" style="color: red; margin-bottom: 10px;"></div>
            <div class="form-input">
                <label for="email">Email</label>
                <input type="email" name="email" id="email" placeholder="Email">
            </div>
            <div class="form-input">
                <label for="password">Mật khẩu</label>
                <input type="password" name="password" id="password" placeholder="Mật khẩu">
            </div>
            <div class="form-input pt-30">
                <input type="submit" name="submit" value="Đăng nhập">
            </div>
            <!-- Quên mật khẩu -->
            <a href="#" class="forget">Quên mật khẩu</a>
            <!-- Đăng ký -->
            <a href="register" class="registration">Đăng ký</a>
        </div>
    </form>
    <!-- /kết thúc form đăng nhập -->
</main>



<script type="module">
    import { apiRequestWithToken } from  '../assets/config/service.js';
    import { environment, STORAGE_KEY, avatarDefault } from  '../assets/config/env.js';

    document.addEventListener('DOMContentLoaded', () => {
        const loginForm = document.getElementById('login-form');
        const errorMessageDiv = document.getElementById('error-message');
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            errorMessageDiv.textContent = '';
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const data = {
                email: email,
                password: password,
            };
            try {
                const response = await fetch(environment.apiUrl + '/auth', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data),
                });
                if (response.ok) {
                    const responseData = await response.json();
                    localStorage.setItem(STORAGE_KEY.accessToken, responseData.accessToken);
                    localStorage.setItem(STORAGE_KEY.refreshToken, responseData.refreshToken);
                    callApiAfterLogin();
                } else {
                    const errorData = await response.json();
                    errorMessageDiv.textContent = errorData;
                }
            } catch (error) {
                console.error('Login failed:', error);
                errorMessageDiv.textContent = 'Failed to connect to the server. Please try again later.';
            }
        });

        async function callApiAfterLogin() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/accounts', {
                    method: 'GET',
                });
                console.log('API response after login:', response);
                const userCurrent = {
                    email: response.email,
                    avatar: getAvatarUrl(response.avatar),
                    roles: response.roles
                }
                localStorage.setItem(STORAGE_KEY.userCurrent, JSON.stringify(userCurrent));
                if(response.roles.includes('ADMIN')){
                    window.location.href = '/admin';
                }
                else{
                    window.location.href = '/home';
                }
            } catch (error) {
                console.error('Error making authenticated API request:', error);
            }
        }

        function getAvatarUrl(avatar) {
            const isAvatarPresent = !!avatar;
            if (isAvatarPresent) {
                return typeof avatar === 'string' ? avatar : avatarDefault;
            } else {
                return avatarDefault;
            }
        }
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
