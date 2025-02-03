<!-- Navbar Start -->
<%@ page contentType="text/html; charset=UTF-8" %>
<nav class="navbar navbar-expand bg-light navbar-light sticky-top px-4 py-0">
    <a href="index.html" class="navbar-brand d-flex d-lg-none me-4">
        <h2 class="text-primary mb-0"><i class="fa fa-hashtag"></i></h2>
    </a>
    <a href="#" class="sidebar-toggler flex-shrink-0">
        <i class="fa fa-bars"></i>
    </a>
    <form class="d-none d-md-flex ms-4">
        <input class="form-control border-0" type="search" placeholder="Tìm kiếm">
    </form>
    <div class="navbar-nav align-items-center ms-auto">
        <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                <i class="fa fa-envelope me-lg-2"></i>
                <span class="d-none d-lg-inline-flex">Tin nhắn</span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
                <a href="#" class="dropdown-item">
                    <div class="d-flex align-items-center">
                        <img class="rounded-circle" src="img/user.jpg" alt="" style="width: 40px; height: 40px;">
                        <div class="ms-2">
                            <h6 class="fw-normal mb-0">Jhon đã gửi bạn một tin nhắn</h6>
                            <small>15 phút trước</small>
                        </div>
                    </div>
                </a>
                <hr class="dropdown-divider">
                <a href="#" class="dropdown-item">
                    <div class="d-flex align-items-center">
                        <img class="rounded-circle" src="img/user.jpg" alt="" style="width: 40px; height: 40px;">
                        <div class="ms-2">
                            <h6 class="fw-normal mb-0">Jhon đã gửi bạn một tin nhắn</h6>
                            <small>15 phút trước</small>
                        </div>
                    </div>
                </a>
                <hr class="dropdown-divider">
                <a href="#" class="dropdown-item">
                    <div class="d-flex align-items-center">
                        <img class="rounded-circle" src="img/user.jpg" alt="" style="width: 40px; height: 40px;">
                        <div class="ms-2">
                            <h6 class="fw-normal mb-0">Jhon đã gửi bạn một tin nhắn</h6>
                            <small>15 phút trước</small>
                        </div>
                    </div>
                </a>
                <hr class="dropdown-divider">
                <a href="#" class="dropdown-item text-center">Xem tất cả tin nhắn</a>
            </div>
        </div>
        <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                <i class="fa fa-bell me-lg-2"></i>
                <span class="d-none d-lg-inline-flex">Thông báo</span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
                <a href="#" class="dropdown-item">
                    <h6 class="fw-normal mb-0">Cập nhật hồ sơ</h6>
                    <small>15 phút trước</small>
                </a>
                <hr class="dropdown-divider">
                <a href="#" class="dropdown-item">
                    <h6 class="fw-normal mb-0">Người dùng mới đã được thêm vào</h6>
                    <small>15 phút trước</small>
                </a>
                <hr class="dropdown-divider">
                <a href="#" class="dropdown-item">
                    <h6 class="fw-normal mb-0">Mật khẩu đã được thay đổi</h6>
                    <small>15 phút trước</small>
                </a>
                <hr class="dropdown-divider">
                <a href="#" class="dropdown-item text-center">Xem tất cả thông báo</a>
            </div>
        </div>
        <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                <img class="rounded-circle me-lg-2" id="userAvatar" src="img/user.jpg" alt="" style="width: 40px; height: 40px;">
                <span class="d-none d-lg-inline-flex" id="emailHeader"></span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
                <a href="#" class="dropdown-item">Hồ sơ của tôi</a>
                <a href="#" class="dropdown-item">Cài đặt</a>
                <a id="logout" class="dropdown-item" style="cursor: pointer">Đăng xuất</a>
            </div>
        </div>
    </div>
</nav>
<!-- Kết thúc Navbar -->



<script type="module">
    import {STORAGE_KEY, environment} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';
    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
    if (userCurrent) {
        const user = JSON.parse(userCurrent);
        if(!user.roles.includes('ADMIN')){
            window.location.replace('/403');
        }
        const userAvatar = user.avatar;
        const email = user.email;
        document.getElementById('userAvatar').src = userAvatar;
        document.getElementById('emailHeader').innerHTML = email;
    } else {
        window.location.replace('/login');
        document.getElementById('userAvatar').style.display = 'none';
        document.getElementById('emailHeader').style.display = 'none';
    }
    document.getElementById('logout').addEventListener('click', function() {
        apiRequestWithToken(environment.apiUrl + '/api/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({refreshToken: localStorage.getItem(STORAGE_KEY.refreshToken)})
        })
            .then(response => {
                console.log(response);
            })
            .catch(error => console.error('Error:', error))
            .finally(() => {
                localStorage.removeItem(STORAGE_KEY.userCurrent);
                localStorage.removeItem(STORAGE_KEY.accessToken);
                localStorage.removeItem(STORAGE_KEY.refreshToken);
                // Redirect to the login page
                window.location.href = '/login';
            });
    });
</script>