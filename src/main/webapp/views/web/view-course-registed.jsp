<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
  <meta charset="utf-8">
  <meta http-equiv="x-ua-compatible" content="ie=edge">
  <title>Danh sách khóa học yêu thích</title>
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
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>


  <style>


    .courses-actives {
      display: flex;
      flex-wrap: wrap;   /* Cho phép các phần tử xuống dòng khi không đủ không gian */
      justify-content: flex-start; /* Căn đều các phần tử */
      gap: 20px;
      /* Khoảng cách giữa các phần tử */
    }

    .properties {
      width: calc(30% - 20px);  /* Chiều rộng của mỗi thẻ khóa học (3 khóa học mỗi dòng) */
      margin-bottom: 20px;
    }




    .properties__card {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      padding: 20px;
    }

    .properties__img.overlay1 {
      position: relative;
      overflow: hidden;
      border-radius: 8px;
    }

    .properties__img.overlay1 img {
      width: 100%;
      height: auto;
      object-fit: cover;
    }

    .properties__caption {
      flex-grow: 1;
      text-align: center;
    }

    .properties__caption p {
      font-size: 14px;
      color: #777;
    }

    .properties__caption h3 {
      font-size: 18px;
      font-weight: bold;
      color: #007bff;
      margin-bottom: 10px;
    }

    .properties__footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 14px;
      color: #555;
    }

    .properties__footer .restaurant-name .rating {
      font-size: 16px;
      color: #f39c12;
    }

    .properties__footer .price {
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }

    .border-btn {
      background-color: #007bff;
      color: white;
      padding: 10px 20px;
      border-radius: 5px;
      text-decoration: none;
      display: block;
      width: 80%;
      margin: 10px auto;
    }

    .border-btn:hover {
      background-color: #0056b3;
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
      <div class="single-slider slider-height2">
        <div class="container">
          <div class="row">
            <div class="col-xl-8 col-lg-11 col-md-12">
              <div class="hero__caption hero__caption2">
                <h1 data-animation="bounceIn" data-delay="0.2s">Danh sách khóa học yêu thích</h1>
                <!-- breadcrumb Bắt đầu-->
                <nav aria-label="breadcrumb">
                  <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="home">Trang chủ</a></li>
                    <li class="breadcrumb-item"><a href="wishlist">Danh sách khóa học yêu thích</a></li>
                    <li class="breadcrumb-item"><a href="courses-registed">Đang học</a></li>

                  </ol>
                </nav>
                <!-- breadcrumb Kết thúc -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
  <!--? Khu vực Slider Kết thúc-->


  <section class="blog_area section-padding">
    <div class="courses-area section-padding40 fix">
      <div class="container">
        <div class="courses-actives" id="courses">
          <!-- Các khóa học sẽ được hiển thị trong đây -->
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
  import { environment, avatarDefault, STORAGE_KEY } from "../../assets/config/env.js";
  import { apiRequestWithToken } from "../../assets/config/service.js";

  document.addEventListener('DOMContentLoaded', function () {
    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
    if (!userCurrent) {
      window.location.assign('/login');
      return;
    }

    const CourseEvent = document.querySelector("#courses");

    // Redirect to blog page when clicking on an article
    CourseEvent.addEventListener("click", function (event) {
      // Kiểm tra nếu người dùng nhấn vào phần tử có lớp 'article' (hoặc phần tử chứa)
      let article = event.target.closest(".properties__card"); // Chọn đúng thẻ chứa thông tin bài viết
      if (article && !event.target.classList.contains("bookmark-icon")) {
        let id = article.getAttribute("data-id"); // Lấy ID hoặc slug từ thuộc tính 'data-id' (hoặc 'data-slug')
        if (id) {
          // Nếu có slug (hoặc ID), điều hướng tới trang chi tiết bài viết
          window.location.href = "/course/" + id;
        }
      }
    });


    // Load wishlist courses
    async function loadData() {
      try {
        const response = await apiRequestWithToken(environment.apiUrl + "/api/course-registed", {
          method: 'GET',
        });

        console.log(response);

        if (response.length === 0) {
          // Nếu không có khóa học nào trong wishlist, hiển thị alert
          Swal.fire({
            title: "Bạn chưa đăng ký khóa học nào",
            text: "Bạn chưa đăng ký khóa học nào. Quay về trang chủ để khám phá các khóa học mới.",
            icon: "warning",
            showCancelButton: false,
            confirmButtonText: "Quay về trang chủ",
          }).then((result) => {
            if (result.isConfirmed) {
              // Chuyển hướng về trang chủ
              window.location.href = "/home"; // Điều chỉnh URL trang chủ nếu cần
            }
          });
          return; // Dừng tiếp tục xử lý nếu không có dữ liệu
        }

        let htmls = "";
        for (let i = 0; i < response.length; i++) {
          let course = response[i];
          let bookmarkClass = "fa-solid"; // Assume all loaded wishlist


          htmls += "<div class='properties pb-20'>";
          htmls += "  <div class='properties__card' data-id='" + course.id + "'>";
          htmls += "    <div class='properties__img overlay1'>";
          htmls += "      <a class='' href='#'><img src='" + course.thumbnail + "' alt=''></a>";
          htmls += "    </div>";
          htmls += "    <div class='properties__caption'>";
          htmls += "      <p>Khóa học hữu ích</p>";
          htmls += "      <h3><a class='imageStyle' href='#'>" + course.title + "</a></h3>";
          htmls += "      <p>" + course.description + "</p>";
          htmls += "      <div class='properties__footer d-flex justify-content-between align-items-center'>";
          htmls += "        <div class='restaurant-name'>";
          htmls += "          <div class='rating'>";
          htmls += "          <span>(" + course.rating + ")</span>";
          htmls += "            <i class='fas fa-star'></i>";
          htmls += "            <i class='fas fa-star'></i>";
          htmls += "            <i class='fas fa-star'></i>";
          htmls += "            <i class='fas fa-star'></i>";
          htmls += "            <i class='fas fa-star'></i>";
          htmls += "          </div>";
          htmls += "          <p> Học viên đánh giá</p>";
          htmls += "        </div>";
          htmls += "        <div class='price'>";
          htmls += "          <span>"+ course.price +"VNĐ</span>";
          htmls += "        </div>";
          htmls += "      </div>";
          htmls += "      <a href='#' class='border-btn'>Vào học </a>";
          htmls += "</div>";
          htmls += "</div>";
          htmls += "    </div>";
          htmls += "  </div>";
          htmls += "</div>";

        }
        CourseEvent.innerHTML = htmls;
      } catch (error) {
        console.error('Error fetching wishlist:', error);
      }
    }


    // Create new scratch file from selection




    // Initial Load
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