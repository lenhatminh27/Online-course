<%@ page contentType="text/html; charset=UTF-8" %>
<!-- Sidebar Start -->
<div class="sidebar pe-4 pb-3">
    <nav class="navbar bg-light navbar-light">
        <a href="index.html" class="navbar-brand mx-4 mb-3">
            <h3 class="text-primary"><i class="fa fa-hashtag me-2"></i>DASHMIN</h3>
        </a>
        <div class="d-flex align-items-center ms-4 mb-4">
            <div class="position-relative">
                <img class="rounded-circle" id="avatarMenu" src="img/user.jpg" alt=""
                     style="width: 40px; height: 40px;">
                <div class="bg-success rounded-circle border border-2 border-white position-absolute end-0 bottom-0 p-1"></div>
            </div>
            <div class="ms-3">
                <h6 class="mb-0" id="emailMenu">Jhon Doe</h6>
                <span>Quản trị viên</span>
            </div>
        </div>
        <div class="navbar-nav w-100">
            <a href="index.html" class="nav-item nav-link active"><i class="fa fa-tachometer-alt me-2"></i>Bảng điều khiển</a>
            <a href="/admin/authorization" class="nav-item nav-link"><i class="fa fa-th me-2"></i>Phân quyền</a>
            <div class="nav-item dropdown">
                <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown"><i
                        class="far fa-file-alt me-2"></i>Blogs</a>
                <div class="dropdown-menu bg-transparent border-0">
                    <a href="/admin/blogs?action=create" class="dropdown-item"><i class="fa fa-plus me-2"></i>Tạo Blog</a>
                    <a href="/admin/blogs?action=view" class="dropdown-item"><i class="fa fa-list me-2"></i>Xem Blogs</a>
                    <a href="/admin/blogs?action=update" class="dropdown-item"><i class="fa fa-edit me-2"></i>Cập nhật Blog</a>
                    <a href="/admin/blogs?action=delete" class="dropdown-item"><i class="fa fa-trash me-2"></i>Xóa Blog</a>
                </div>
            </div>
            <div class="nav-item dropdown">
                <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown"><i
                        class="far fa-file-alt me-2"></i>Thể loại khoá học</a>
                <div class="dropdown-menu bg-transparent border-0">
                    <a href="/admin/category" class="dropdown-item"><i class="fa fa-plus me-2"></i>Tạo thể loại</a>
                    <a href="/admin/blogs?action=view" class="dropdown-item"><i class="fa fa-list me-2"></i>Xem thể loại</a>
                    <a href="/admin/blogs?action=update" class="dropdown-item"><i class="fa fa-edit me-2"></i>Cập nhật thể loại</a>
                    <a href="/admin/blogs?action=delete" class="dropdown-item"><i class="fa fa-trash me-2"></i>Xóa thể loại</a>
                </div>
            </div>
            <a href="chart.html" class="nav-item nav-link"><i class="fa fa-chart-bar me-2"></i>Biểu đồ</a>
            <div class="nav-item dropdown">
                <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown"><i
                        class="far fa-file-alt me-2"></i>Trang</a>
                <div class="dropdown-menu bg-transparent border-0">
                    <a href="signin.html" class="dropdown-item">Đăng nhập</a>
                    <a href="signup.html" class="dropdown-item">Đăng ký</a>
                    <a href="404.html" class="dropdown-item">Lỗi 404</a>
                    <a href="blank.html" class="dropdown-item">Trang trống</a>
                </div>
            </div>
        </div>
    </nav>
</div>
<!-- Sidebar End -->

<script type="module">
    import {STORAGE_KEY} from '../../assets/config/env.js';

    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
    if (userCurrent) {
        const user = JSON.parse(userCurrent);
        const userAvatar = user.avatar;
        const email = user.email;
        document.getElementById('avatarMenu').src = userAvatar;
        document.getElementById('emailMenu').innerHTML = email;
    } else {
        document.getElementById('userAvatar').style.display = 'none';
        document.getElementById('emailHeader').style.display = 'none';
    }
</script>