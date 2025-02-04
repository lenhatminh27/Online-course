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
        .manh-slider{
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
                        <a href="/profile" class="list-group-item list-group-item-action active" data-bs-toggle="tab">
                            Cập nhật hồ sơ
                        </a>
                        <a href="/change-password" class="list-group-item list-group-item-action" data-bs-toggle="tab">
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
                                <h4>Cập nhật hồ sơ</h4>
                                <span id="message"></span>
                                <form action="account" method="post" enctype="multipart/form-data">
                                    <!-- ID (Ẩn) -->
                                    <input type="hidden" id="id" name="id">

                                    <!-- Họ -->

                                    <div class="mb-3">
                                        <label for="lastName" class="form-label">Họ</label>
                                        <input type="text" class="form-control" id="lastName" name="lastName" required>
                                    </div>

                                    <!-- Tên -->
                                    <div class="mb-3">
                                        <label for="firstName" class="form-label">Tên</label>
                                        <input type="text" class="form-control" id="firstName" name="firstName" required>
                                    </div>

                                    <!-- Số điện thoại -->
                                    <div class="mb-3">
                                        <label for="phoneNumber" class="form-label">Số điện thoại</label>
                                        <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required>
                                    </div>

                                    <!-- Ảnh đại diện -->
                                    <div class="mb-3" style="display: none !important;">
                                        <label for="avatar" class="form-label">Ảnh đại diện</label>
                                        <input type="text" class="form-control" id="avatar" name="avatar">
                                    </div>

                                    <!-- Địa chỉ -->
                                    <div class="mb-3">
                                        <label for="address" class="form-label">Địa chỉ</label>
                                        <textarea class="form-control" id="address" name="address" rows="2" required></textarea>
                                    </div>

                                    <!-- Ngày sinh -->
                                    <div class="mb-3">
                                        <label for="dateOfBirth" class="form-label">Ngày sinh</label>
                                        <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" required>
                                    </div>

                                    <!-- Nút Lưu -->
                                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                                </form>

                            </div>
                        </div>

                        <!-- Lịch sử hoạt động -->
                        <div class="tab-pane fade" id="history">
                            <div class="card p-4">
                                <h4>Lịch sử hoạt động</h4>
                                <ul class="list-group">
                                    <li class="list-group-item">Bạn đã đăng nhập lúc ...</li>
                                    <li class="list-group-item">Bạn đã cập nhật hồ sơ lúc ...</li>
                                    <li class="list-group-item">Bạn đã đặt lịch hẹn lúc ...</li>
                                </ul>
                            </div>
                        </div>
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

    import { environment, avatarDefault,STORAGE_KEY} from "../../assets/config/env.js";
    import { apiRequestWithToken } from "../../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', function () {
        const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
        if (userCurrent === null || userCurrent === ''){
            window.location.assign('/login');
            return;
        }

        async function loadData() {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + "/api/account-profile", {
                    method: 'GET',
                });
                if (response) {
                    document.getElementById("id").value = response.id || ''; // nếu null, set thành chuỗi rỗng

                    document.getElementById("firstName").value = response.firstName || ''; // nếu null, set thành chuỗi rỗng

                    document.getElementById("lastName").value = response.lastName || ''; // nếu null, set thành chuỗi rỗng

                    document.getElementById("phoneNumber").value = response.phoneNumber || ''; // nếu null, set thành chuỗi rỗng

                    document.getElementById("address").value = response.address || ''; // nếu null, set thành chuỗi rỗng

                    if (response.dateOfBirth) {
                        const dateOfBirth = response.dateOfBirth.split('T')[0]; // lấy phần trước 'T' để có định dạng yyyy-MM-dd
                        document.getElementById("dateOfBirth").value = dateOfBirth;
                    }
                }
            } catch (error) {
                console.error('Có lỗi xảy ra:', error);
            }
        }



        async function saveProfile(event) {
            event.preventDefault();
            const formData = new FormData(document.querySelector('form'));
            const id = formData.get('id');
            const firstName = formData.get('firstName');
            const lastName = formData.get('lastName');
            const phoneNumber = formData.get('phoneNumber');
            const address = formData.get('address');
            const dateOfBirth = formData.get('dateOfBirth');
            const avatar = formData.get('avatar');
            const payload = {
                id,
                firstName,
                lastName,
                phoneNumber,
                address,
                dateOfBirth,
                avatar
            };

            try {
                const response = await apiRequestWithToken(environment.apiUrl + "/api/account-profile", {
                    method: 'PUT',
                    body: JSON.stringify(payload), // Chuyển đối tượng thành JSON
                    headers: {
                        'Content-Type': 'application/json' // Đảm bảo định dạng dữ liệu là JSON
                    }
                });
                if (response) {
                    document.getElementById('message').innerHTML = "Cập nhật hồ sơ thành công";
                    document.getElementById('message').style.color = 'green';
                    await loadData(); // Tải lại dữ liệu mới
                } else {
                    alert('Có lỗi xảy ra khi cập nhật hồ sơ!');
                }
            } catch (error) {
                console.log(error)
                alert('Không thể lưu hồ sơ. Vui lòng thử lại sau!');
            }
        }

        document.querySelector('form').addEventListener('submit', saveProfile);

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