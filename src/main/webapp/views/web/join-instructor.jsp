<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Join Instructor</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="manifest" href="site.webmanifest">
    <link rel="shortcut icon" type="image/x-icon" href="../../assets/img/favicon.ico">
    <!-- CSS here -->
    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="../../assets/css/slicknav.css">
    <link rel="stylesheet" href="../../assets/css/flaticon.css">
    <link rel="stylesheet" href="../../assets/css/progressbar_barfiller.css">
    <link rel="stylesheet" href="../../assets/css/gijgo.css">
    <link rel="stylesheet" href="../../assets/css/animate.min.css">
    <link rel="stylesheet" href="../../assets/css/animated-headline.css">
    <link rel="stylesheet" href="../../assets/css/magnific-popup.css">
    <link rel="stylesheet" href="../../assets/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="../../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../../assets/css/slick.css">
    <link rel="stylesheet" href="../../assets/css/nice-select.css">
    <link rel="stylesheet" href="../../assets/css/style.css">

</head>

<body>
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
<!-- Preloader Start -->

<%@include file="../../common/web/header.jsp" %>
<div style="background: linear-gradient(180deg, #c055ff, #676eff); height: 100px">
</div>

<main>
    <div>
        <div style="margin: 100px 200px;">
            <form id="to-instructor">
                <h2>Điều khoản trở thành giảng viên</h2>
                <h3>1. Trách nhiệm của giảng viên:</h3>
                <ul style="list-style-type: disc">
                    <li>Cung cấp nội dung đào tạo chính xác, đầy đủ, và không vi phạm bản quyền.</li>
                    <li>Đảm bảo nội dung phù hợp với các tiêu chuẩn và yêu cầu do nền tảng đề ra.</li>
                    <li>Hỗ trợ và phản hồi thắc mắc từ học viên một cách chuyên nghiệp và kịp thời.</li>
                </ul>
                <h3>2. Yêu cầu về nội dung:</h3>
                <ul>
                    <li>Tất cả nội dung được đăng tải phải tuân thủ chính sách và quy định pháp luật.</li>
                    <li>Không được đăng tải nội dung có tính phân biệt, kỳ thị, hoặc vi phạm thuần phong mỹ tục.</li>
                    <li>Đảm bảo tài liệu, hình ảnh, và video được sử dụng không vi phạm bản quyền.</li>
                </ul>
                <h3>3. Quyền lợi và nghĩa vụ:</h3>
                <ul>
                    <li>Giảng viên sẽ nhận được doanh thu chia sẻ theo thỏa thuận của nền tảng.</li>
                    <li>Nền tảng có quyền kiểm duyệt và gỡ bỏ nội dung không phù hợp hoặc vi phạm chính sách.</li>
                </ul>
                <h3>4. Điều khoản chấm dứt:</h3>
                <ul>
                    <li>Nền tảng có quyền chấm dứt hợp tác nếu phát hiện vi phạm chính sách nghiêm trọng.</li>
                    <li>Giảng viên có thể yêu cầu chấm dứt hợp tác bằng cách gửi thông báo trước 30 ngày.</li>
                </ul>
                <h3>5. Cam kết:</h3>
                <ul>
                    <li>Giảng viên cam kết tuân thủ các điều khoản, chính sách và hợp tác minh bạch.</li>
                </ul>
                <div style="margin: 20px 0;">
                    <input type="checkbox" id="agree-terms">
                    <label for="agree-terms">Tôi đã đọc và đồng ý với các điều khoản trên.</label>
                </div>
                <button name="button" type="submit" class="genric-btn danger">Tôi đồng ý với các điều khoản và hợp đồng
                    trên
                </button>
            </form>

        </div>
    </div>
</main>
<%@include file="../../common/web/footer.jsp" %>
<script type="module">
    import { environment, STORAGE_KEY, avatarDefault } from "../../assets/config/env.js";
    import { apiRequestWithToken } from "../../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById("to-instructor");
        const checkbox = document.getElementById("agree-terms");
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (!checkbox.checked) {
                alert('Bạn cần đồng ý với các điều khoản để tiếp tục.');
                return;
            }
            const success = await updateToInstructor();
            if (success) {
                await callApiAfterChangeRole();
            }
        });
    });

    async function updateToInstructor() {
        try {
            const response = await fetch(environment.apiUrl + `/api/to-instructor`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ` + localStorage.getItem(STORAGE_KEY.accessToken)
                }
            })
            if (response.status === 204) {
                return true;
            } else {
                const errorMessage = await response.text();
                alert(`Error: ` + errorMessage);
                return false;
            }
        } catch (error) {
            console.error('Error occurred while updating the role:', error);
            alert('An error occurred while updating the role.');
            return false;
        }
    }

    async function callApiAfterChangeRole() {
        try {
            const response = await apiRequestWithToken(environment.apiUrl + `/api/accounts`, {
                method: 'GET',
            });
            const userCurrent = {
                email: response.email,
                avatar: response.avatar || avatarDefault,
                roles: response.roles || [],
            };
            localStorage.setItem(STORAGE_KEY.userCurrent, JSON.stringify(userCurrent));
            window.location.assign("/home")
        } catch (error) {
            console.error('Error making authenticated API request:', error);
        }
    }

</script>
<!-- Scroll Up -->
<div id="back-top">
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>

<!-- JS here -->
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
<!-- Progress -->
<script src="../../assets/js/jquery.barfiller.js"></script>

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
