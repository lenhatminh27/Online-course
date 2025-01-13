<header>
    <!-- Header Start -->
    <div class="header-area header-transparent">
        <div class="main-header">
            <div class="header-bottom header-sticky">
                <div class="container-fluid">
                    <div class="row align-items-center">
                        <!-- Logo -->
                        <div class="col-xl-2 col-lg-2">
                            <div class="logo">
                                <a href="index.html"><img src="assets/img/logo/logo.png" alt=""></a>
                            </div>
                        </div>
                        <div class="col-xl-10 col-lg-10">
                            <div class="menu-wrapper d-flex align-items-center justify-content-end">
                                <!-- Main-menu -->
                                <div class="main-menu d-none d-lg-block">
                                    <nav>
                                        <ul id="navigation">
                                            <li class="active"><a href="/home">Home</a></li>
                                            <li><a href="courses.html">Courses</a></li>
                                            <li><a href="about.html">About</a></li>
                                            <li><a href="#">Blog</a>
                                                <ul class="submenu">
                                                    <li><a href="blog.html">Blog</a></li>
                                                    <li><a href="blog_details.html">Blog Details</a></li>
                                                    <li><a href="elements.html">Element</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="contact.html">Contact</a></li>

                                            <!-- Join button -->
                                            <li class="button-header margin-left" id="joinBtn">
                                                <a href="#" class="btn">Join</a>
                                            </li>

                                            <!-- Log in button -->
                                            <li class="button-header" id="loginBtn">
                                                <a href="/login" class="btn btn3">Log in</a>
                                            </li>

                                            <!-- Display user info when logged in -->
                                            <li id="userInfo" class="user-info" >
                                                <img id="userAvatar" src="" alt="User Avatar"
                                                     style="width: 40px; height: 40px; object-fit: cover;
                                                      border-radius: 50%;
                                                    "
                                                     class="avatar">
                                            </li>

                                        </ul>
                                    </nav>
                                </div>
                            </div>
                        </div>
                        <!-- Mobile Menu -->
                        <div class="col-12">
                            <div class="mobile_menu d-block d-lg-none"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Header End -->
</header>

<script type="text/javascript">
    const userCurrent = localStorage.getItem('online_course_user_current');
    if (userCurrent) {
        const user = JSON.parse(userCurrent);
        document.getElementById('loginBtn').style.display = 'none';
        const userAvatar = user.avatar;
        document.getElementById('userAvatar').src = userAvatar;
    } else {
        document.getElementById('joinBtn').style.display = 'none';
        document.getElementById('userInfo').style.display = 'none';
    }
</script>
