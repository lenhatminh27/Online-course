<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Nạp tiền</title>
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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script type="module">
        import {STORAGE_KEY} from "../../assets/config/env.js";
        const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
        if (userCurrent === null || userCurrent === ''){
                window.location.assign('/login');
        }
    </script>

    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault, ws} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        document.addEventListener('alpine:init', () => {
            Alpine.store('wallet', {
                rates: {
                    5000: 5000,
                    10000: 10000,
                    20000: 20000,
                    50000: 50000,
                    100000: 100000,
                    200000: 200000,
                    500000: 500000,
                    1000000: 1000000
                },
                selectedAmount: 5000,  // Giá trị mặc định
                selectedSohAmount: 5000, // Sò tương ứng với số tiền
                showQR: false,
                QR: '',
                qrTimer: null, // Biến lưu timeout
                pointCurrent: 0,
                paymentCheckInterval: null,
                tranId: null,

                // Cập nhật số sò khi thay đổi số tiền
                updateSohAmount() {
                    this.showQR = false;
                    this.selectedSohAmount = this.rates[this.selectedAmount] || 0;
                },

                async loadPoint(){
                    try{
                        const response = await apiRequestWithToken(environment.apiUrl + "/api/wallets");
                        if(response){
                            this.pointCurrent = response.balance;
                        }
                    }catch (error){
                        console.log(error);
                    }
                },

                async processQr() {
                    try{
                        const response = await apiRequestWithToken(environment.apiUrl + "/api/process/qr", {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({
                                point: this.selectedAmount
                            })
                        });
                        if (response){
                            this.showQR = true;
                            this.QR = response.qr;
                            this.tranId = response.tranId;
                            // Xóa timer cũ nếu có
                            if (this.qrTimer) {
                                clearTimeout(this.qrTimer);
                            }
                            // Đặt thời gian hết hạn 5 phút
                            this.qrTimer = setTimeout(() => {
                                this.showQR = false;
                                this.QR = '';
                            }, 3 * 60 * 1000); // 5 phút = 300,000ms

                            // 🟢 Kết nối đến WebSocket khi thành công
                            this.startPaymentCheck();
                        }
                    }
                    catch (error){
                        console.log(error);
                    }
                },


                // Hàm kiểm tra trạng thái thanh toán
                async checkPaymentStatus() {
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + "/hooks/sepay-payment/" + this.tranId);
                        if (response && response.success) {
                            console.log("✅ Thanh toán thành công! Cập nhật lại số dư.");
                            await this.loadPoint();
                            clearInterval(this.paymentCheckInterval);
                            Swal.fire({
                                title: "Nạp point thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                            this.showQR = false;
                            this.tranId = null;
                        }
                    } catch (error) {
                        console.log("Lỗi khi kiểm tra thanh toán:", error);
                    }
                },


                startPaymentCheck() {
                    if (this.paymentCheckInterval) {
                        clearInterval(this.paymentCheckInterval); // Xóa interval cũ nếu có
                    }
                    this.paymentCheckInterval = setInterval(() => {
                        this.checkPaymentStatus();
                    }, 5000); // Kiểm tra mỗi 5 giây
                    // 🛑 Dừng kiểm tra sau 3 phút
                    setTimeout(() => {
                        clearInterval(this.paymentCheckInterval);
                        this.paymentCheckInterval = null;
                        console.log("⏳ Dừng kiểm tra thanh toán sau 3 phút.");
                        this.tranId = null;
                    }, 3 * 60 * 1000); // 3 phút = 180,000 ms
                },


            });
            Alpine.store("wallet").loadPoint();
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
    <style>
        .manh-slider{
            height: 100px !important;
        }
             /* CSS cho các phần tử trong giao diện */
         .payment-option {
             border: 1px solid #e3e3e3;
             margin-bottom: 10px;
             padding: 15px;
             border-radius: 8px;
             transition: all 0.3s ease;
         }

        .payment-option:hover {
            border-color: #ff5722;
            background-color: #fdf3f2;
        }

        ._3V9DM0qZ5XUDQCKZboGom{
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .payment-option input[type="radio"] {
            display: none;
        }

        .payment-option input[type="radio"]:checked + .payment-info {
            background-color: #ff5722;
            color: white;
            border-color: #ff5722;
        }

        .payment-option input[type="radio"]:checked + .payment-info .payment-price {
            font-weight: bold;
        }

        .payment-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            cursor: pointer;
        }

        .payment-info .payment-price {
            font-size: 18px;
            font-weight: normal;
        }

        .payment-info .payment-so {
            color: #757575;
        }

        .active {
            background-color: #ff5722;
            color: white;
        }

        .fa-bitcoin{
            color: blueviolet;
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
    <section class="blog_area section-padding" x-data>
        <div class="container">
            <div class="row">
                <div class="current-point">
                    <h4>Điểm hiện tại: <span x-text="$store.wallet.pointCurrent"></span> <i class='fa-brands fa-bitcoin'></i></h4>
                </div>
            </div>
            <div class="row">
                <!-- Lựa chọn gói nạp -->
                <div class="col-md-6">
                    <div class="list-group">
                        <!-- Sử dụng Alpine Store để lưu các giá trị rate -->
                        <template x-for="(rate, amount) in $store.wallet.rates" :key="amount">
                            <a href="#" class="list-group-item list-group-item-action" :class="{'active': $store.wallet.selectedAmount === amount}">
                                <input type="radio" name="payment-count" :value="amount" x-model="$store.wallet.selectedAmount" class="form-check-input" @change="$store.wallet.updateSohAmount()" />
                                <div class="_3V9DM0qZ5XUDQCKZboGom">
                                    <div class="_3nTgjjyoq4NSC4bbztkcnl">
                                        <span class="fHutKFROlyyE1qVJNGaEq" x-text="amount.toLocaleString() + ' ₫'"></span>
                                    </div>
                                    <div class="_1v4QMCKGPgfdVXYRO07us">
                                        <div><i class='fa-brands fa-bitcoin'></i> × <span x-text="rate"></span></div>
                                    </div>
                                </div>
                            </a>
                        </template>
                    </div>
                </div>

                <!-- Chi tiết giao dịch -->
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <!-- Nếu chưa xử lý thanh toán -->
                            <template x-if="!$store.wallet.showQR">
                                <div>
                                    <h5 class="card-title">Chi tiết giao dịch</h5>
                                    <p class="card-text">Sản phẩm được chọn: <i class='fa-brands fa-bitcoin'></i> × <span x-text="$store.wallet.selectedSohAmount"></span></p>
                                    <p class="card-text">Giá: <span x-text="$store.wallet.selectedAmount.toLocaleString()"></span> ₫</p>
                                    <p class="card-text">Phương thức thanh toán: Quét mã QR</p>
                                    <button class="btn btn-danger" @click="$store.wallet.processQr()">Xử lý thanh toán</button>
                                </div>
                            </template>

                            <!-- Nếu xử lý thành công, hiển thị mã QR -->
                            <template x-if="$store.wallet.showQR">
                                <div class="text-center">
                                    <p>Quét mã QR</p>
                                    <img :src="$store.wallet.QR" alt="QR Code" class="img-fluid">
                                    <p class="text-muted">Mã QR sẽ hết hạn sau 3 phút. Vui lòng hoàn tất thanh toán.</p>
                                </div>
                            </template>
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