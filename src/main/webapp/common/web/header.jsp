<%@ page contentType="text/html; charset=UTF-8" %>
<style>
    #logout {
        background-color: #ff4d4d;
        color: white;
        border: none;
        padding: 10px 20px;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    #logout:hover {
        background-color: #ff1a1a;
    }
</style>
<header>
    <!-- Bắt đầu Header -->
    <div class="header-area header-transparent">
        <div class="main-header">
            <div class="header-bottom header-sticky">
                <div class="container-fluid">
                    <div class="row align-items-center">
                        <!-- Logo -->
                        <div class="col-xl-2 col-lg-2">
                            <div class="logo">
                                <a href="/home"><img src="/assets/img/logo/logo.png" alt="Logo"></a>
                            </div>
                        </div>
                        <div class="col-xl-10 col-lg-10">
                            <div class="menu-wrapper d-flex align-items-center justify-content-end">
                                <!-- Menu chính -->
                                <div class="main-menu d-none d-lg-block">
                                    <nav>
                                        <ul id="navigation">
                                            <li class="active"><a href="/home">Trang chủ</a></li>
                                            <li><a href="/courses">Khóa học</a></li>
                                            <li><a href="/blogs">Bài viết</a></li>
                                            <li id="redInstructor"><a href="/instructor">Giảng viên</a></li>
                                            <li><a href="/wallet">Nạp tiền</a></li>
                                            <!-- Nút tham gia -->
                                            <li class="button-header margin-left" id="joinBtn">
                                                <a href="/joinInstructor" class="btn">Tham gia giảng viên</a>
                                            </li>


                                            <!-- Nút đăng nhập -->
                                            <li class="button-header" id="loginBtn">
                                                <a href="/login" class="btn btn3">Đăng nhập</a>
                                            </li>

                                            <!-- Hiển thị thông tin người dùng khi đã đăng nhập -->
                                            <li id="userInfo" class="user-info">
                                                <a href="/profile">
                                                    <img id="userAvatar" src="" alt="Ảnh đại diện"
                                                         style="width: 40px; height: 40px; object-fit: cover;
                                                      border-radius: 50%;">
                                                </a>
                                            </li>

                                            <li class="user-info">
                                                <button id="logout">Đăng xuất</button>
                                            </li>

                                        </ul>
                                    </nav>
                                </div>
                            </div>
                        </div>
                        <!-- Menu trên điện thoại -->
                        <div class="col-12">
                            <div class="mobile_menu d-block d-lg-none"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Kết thúc Header -->
</header>

<script type="module">
    import {STORAGE_KEY, environment} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';
    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
    if (userCurrent) {
        const user = JSON.parse(userCurrent);
        document.getElementById('loginBtn').style.display = 'none';
        const userAvatar = user.avatar;
        document.getElementById('userAvatar').src = userAvatar;
    } else {
        document.getElementById('joinBtn').style.display = 'none';
        document.getElementById('userInfo').style.display = 'none';
        document.getElementById('logout').style.display = 'none';
    }
    if(userCurrent !== '' && userCurrent !== null){
        let userData = JSON.parse(userCurrent);
        if(userData.roles.includes('INSTRUCTOR')){
            document.getElementById('joinBtn').style.display = 'none';
        }
        else{
            document.getElementById('redInstructor').style.display = 'none';
        }
    }
    else{
        document.getElementById('redInstructor').style.display = 'none';
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
