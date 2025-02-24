<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Courses | Education</title>
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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <style>
        body {
            width: 100vw;
            height: 100vh;
            overscroll-behavior: none;
            overflow: hidden !important;
        }

        .cursor-pointer {
            cursor: pointer;
        }

        .scroller {
            height: 73vh;
            overflow-y: scroll;
            scroll-snap-type: y mandatory;
        }

        .article {
            max-width: 100%;
        }

        .progress {
            width: 200px;
        }

        .nav-color {
            background: linear-gradient(to bottom, #be55ff, #5972ff);
        }

        .bg-gray {
            background-color: #eeeeee; /* Màu xám, bạn có thể thay đổi theo ý muốn */
        }

        .search {
            background-color: transparent;
            color: white;
            border: 1px solid white;
            border-radius: 50px;
            padding: 5px 50px;
        }

        .search i {
            margin: 0 10px;
        }

        .input-group {
            border-radius: 5px;
        }
    </style>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        const pathSegments = window.location.pathname.split('/');
        const menuSectionIndex = pathSegments.indexOf("menu-section");
        let courseId = null;
        if (menuSectionIndex !== -1 && menuSectionIndex + 1 < pathSegments.length) {
            courseId = pathSegments[menuSectionIndex + 1];
            console.log("Course ID:", courseId);

        } else {
            console.error("Không tìm thấy courseId trong URL");
        }

        async function loadSection() {
            if (!courseId) {
                console.error("Không có courseId, không thể gọi API!");
                return [];
            }

            const apiUrl = environment.apiUrl + '/api/menu-section/' + courseId;
            console.log("Gọi API:", apiUrl);

            try {
                const response = await apiRequestWithToken(apiUrl);
                console.log("Dữ liệu nhận được:", response);
                return response;
            } catch (error) {
                console.error("Lỗi khi gọi API:", error);
                return [];
            }
        }

        async function loadLesson() {
            try {
                const lesson = await apiRequestWithToken(environment.apiUrl + '/api/lesson/1')
                console.log("Lesson: " + lesson)
            } catch (error) {
                console.error("Lỗi khi gọi API:", error);
                return [];
            }
        }

        async function loadSearch() {
            try {
                await fetch(environment.apiUrl + "/api/search-in-course", {
                    method: "POST",
                    body: JSON.stringify({content: this.searchValue, courseId: courseId}),
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(data => console.log("Searh: " + data));
            } catch (error) {
                console.error("Lỗi khi gọi API: ", error);
            }
        }

        // Attach function to window so Alpine.js can access it
        window.loadSection = loadSection;
        window.loadLesson = loadLesson;
        window.loadSearch = loadSearch;


    </script>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        document.addEventListener('alpine:init', () => {

            const pathSegments = window.location.pathname.split('/');
            const menuSectionIndex = pathSegments.indexOf("menu-section");
            let courseId = null;
            if (menuSectionIndex !== -1 && menuSectionIndex + 1 < pathSegments.length) {
                courseId = pathSegments[menuSectionIndex + 1];
                console.log("Course ID:", courseId);
            } else {
                console.error("Không tìm thấy courseId trong URL");
            }

            Alpine.store('menuSection', {
                sections: [],
                course: null,
                selectedSection: [],
                currentLesson: null,
                doneLessons: [],
                progress: 80,
                searchValue: null,
                searchItem: null,

                setInput(e) {
                    this.searchValue = e;
                },

                async loadSections() {
                    if (!courseId) {
                        console.error("Không có courseId, không thể gọi API!");
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/menu-section/' + courseId);
                        this.sections = response;
                        this.course = this.sections.at(0).course;
                        console.log(response);
                    } catch (error) {
                        console.error("Lỗi khi gọi API:", error);
                    }
                },

                async loadLesson(lessonId = 1) {
                    try {
                        const lesson = await apiRequestWithToken(environment.apiUrl + '/api/lesson/' + lessonId);
                        this.currentLesson = lesson;
                        console.log(this.currentLesson);
                    } catch (error) {
                        console.error("Lỗi khi gọi API:", error);
                    }
                },

                async loadSearch() {
                    // Nếu searchValue rỗng thì không gọi API và reset searchItem
                    if (!this.searchValue.trim()) {
                        console.warn("Không gọi API vì searchValue rỗng!");
                        this.searchItem = { sections: [], lessons: [], articles: [] };
                        return;
                    }

                    // Nếu có request cũ đang chạy, hủy nó đi
                    if (this.searchAbortController) {
                        this.searchAbortController.abort();
                    }
                    // Tạo AbortController mới cho request hiện tại
                    this.searchAbortController = new AbortController();
                    const { signal } = this.searchAbortController;

                    try {
                        const response = await fetch(environment.apiUrl + "/api/search-in-course", {
                            method: "POST",
                            body: JSON.stringify({ content: this.searchValue, courseId: courseId }),
                            headers: {
                                "Content-Type": "application/json"
                            },
                            signal // Đính kèm signal để có thể hủy request
                        });

                        this.searchItem = await response.json();
                        console.log("✅ Kết quả tìm kiếm:", this.searchItem);

                        // Đảm bảo luôn có sections, lessons, articles để tránh lỗi undefined
                        if (!this.searchItem) this.searchItem = {};
                        if (!this.searchItem.sections) this.searchItem.sections = [];
                        if (!this.searchItem.lessons) this.searchItem.lessons = [];
                        if (!this.searchItem.articles) this.searchItem.articles = [];
                    } catch (error) {
                        if (error.name === "AbortError") {
                            console.log("❌ Yêu cầu tìm kiếm đã bị hủy bỏ.");
                        } else {
                            console.error("❌ Lỗi khi gọi API:", error);
                        }
                        // Reset searchItem để tránh lỗi khi API gặp sự cố
                        this.searchItem = { sections: [], lessons: [], articles: [] };
                    }
                }


            });

            Alpine.effect(() => {
                let timeout;
                let searchStore = Alpine.store('menuSection');

                clearTimeout(timeout);
                timeout = setTimeout(() => {
                    searchStore.loadSearch();
                }, 1000);
            });


            Alpine.store('menuSection').loadSections();
            Alpine.store('menuSection').loadLesson();
            Alpine.store('menuSection').loadSearch();
            console.log("Data in store:", Alpine.store('menuSection').searchItem);

        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
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

<div class="d-flex flex-column pl-2" x-data>
    <div class="row w-105 ">
        <nav class="navbar nav-color navbar-expand-lg bg-body-tertiary d-flex justify-content-lg-between pt-3 pl-5">
            <div>
                <p class="navbar-brand fs-1 text-light" href="#" x-text="$store.menuSection.course.title"></p>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
                        aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
            </div>
            <div>
                <button class="search" data-bs-toggle="modal" data-bs-target="#staticBackdrop"><span><i
                        class="fa-solid fa-magnifying-glass"></i></span><span>Tìm gì à? Để tôi giúp
                    bạn một tay</span><span><i class="fa-solid fa-face-laugh-wink fa-rotate-by"
                                               style="color: #FFD43B; --fa-rotate-angle: 20deg;"></i></span>
                </button>
            </div>
            <div class="progress mr-5" role="progressbar" aria-label="Example with label"
                 aria-valuenow="$store.menuSection.progress" aria-valuemin="0" aria-valuemax="100">
                <div class="progress-bar"
                     :style="'width: ' + $store.menuSection.progress + '%'"
                     x-text="$store.menuSection.progress + '%'">
                </div>
            </div>

        </nav>
        <div class="myModal modal fade" id="staticBackdrop" data-bs-backdrop="true" data-bs-keyboard="false" tabindex="-1"
             aria-labelledby="staticBackdropLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <input type="text" x-model="$store.menuSection.searchValue"
                               @input="$store.menuSection.loadSearch()" class="form-control" placeholder="Tìm kiếm..."
                               aria-describedby="basic-addon1">
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cslose"></button>
                    </div>

                    <div class="modal-body">
                        <div>
                            <template x-text="'Section'"
                                      x-for="(section, index) in $store.menuSection.searchItem.sections"
                                      :key="index">
                                <div class="border p-3">
                                    <p class="border-bottom font-weight-bold" x-text="section.title"></p>
                                    <template  x-for="(lesson, index) in section.menuLessons" :key="index">
                                        <p type="button" data-bs-dismiss="modal" @click="$store.menuSection.loadLesson(lesson.id);
                                                                                         $store.menuSection.searchValue = null;
                                                                                         $store.menuSection.searchItem = null;
                                        " class="border-bottom pl-5 m-0 cursor-pointer" x-text="lesson.title"></p>
                                    </template>
                                </div>
                            </template>
                            <template x-for="(lesson, i) in $store.menuSection.searchItem.lessons" :key="i">
                                <p type="button" data-bs-dismiss="modal" @click="$store.menuSection.loadLesson(lesson.id);
                                                                                         $store.menuSection.searchValue = null;
                                                                                         $store.menuSection.searchItem = null;
                                        " class="border-bottom cursor-pointer"
                                   x-text="lesson.title"></p>
                            </template>
                            <template
                                    x-for="(article, e) in $store.menuSection.searchItem.articles" :key="e">
                                <div type="button" data-bs-dismiss="modal" @click="$store.menuSection.loadLesson(article.menuLesson.id);
                                                                                         $store.menuSection.searchValue = null;
                                                                                         $store.menuSection.searchItem = null;
                                        "
                                     class="border-bottom p-0 m-0 cursor-pointer">
                                    <p x-text="article.article"></p>
                                    <p class="font-weight-bold p-0 m-0" x-text="article.menuLesson.title"></p>
                                </div>
                            </template>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="mt-4 row">
        <div class="col-4">
            <div class='border-bottom'>
                <h1 class="pl-1 pt-2 mb-0">Nội dung khoá học</h1>
            </div>
            <div class="scroller">
                <!-- Lặp qua từng Section -->
                <template x-for="(section, index) in $store.menuSection.sections" :key="section.id">
                    <div class="border-bottom" x-data="{ open: false }">
                        <div class="d-flex justify-content-lg-between cursor-pointer"
                             data-toggle="collapse"
                             :data-target="'#section-' + index"
                             @click="open = !open">
                            <p class="font-weight-bold p-2 pt-4">
        <span>
          <i :class="open ? 'fa-solid fa-chevron-up mr-2' : 'fa-solid fa-chevron-down mr-2'"></i>
        </span>
                                <span x-text="'Chương ' + (index + 1)"></span>: <span x-text="section.title"></span>
                            </p>
                            <p class="p-2" x-text="'0/' + section.menuLessons.length"></p>
                        </div>

                        <!-- Nội dung collapse -->
                        <div class="collapse" :id="'section-' + index"
                             x-init="
            $el.addEventListener('shown.bs.collapse', () => { open = true });
            $el.addEventListener('hidden.bs.collapse', () => { open = false });
         ">
                            <template x-for="(lesson, lessonIndex) in section.menuLessons" :key="lesson.id">
                                <div class="pl-4 cursor-pointer border-bottom border-top d-flex justify-content-lg-between"
                                     @click="$store.menuSection.loadLesson(lesson.id)"
                                     :class="{'bg-gray': $store.menuSection.currentLesson && $store.menuSection.currentLesson.id === lesson.id}">
                                    <p class="p-2">
                                        <span x-text="lessonIndex + 1"></span>. <span x-text="lesson.title"></span>
                                    </p>
                                    <div class="mr-4 mt-3"><i class="fa-solid fa-circle-check"
                                                              style="color: #63E6BE;"></i></div>
                                </div>
                            </template>
                        </div>
                    </div>
                </template>

            </div>
        </div>
        <div class="col-8 ">
            <div class="scroller">
                <h1 class="lesson-tittle" x-text="$store.menuSection.currentLesson.title"></h1>
                <template x-if="$store.menuSection.currentLesson.videoUrl">
                    <div>
                        <video width="100%" controls preload="metadata">
                            <source :src=$store.menuSection.currentLesson.videoUrl type="video/mp4">
                            Trình duyệt của bạn không hỗ trợ video.
                        </video>
                    </div>
                </template>
                <template x-if="$store.menuSection.currentLesson.article">
                    <div class="article">
                        <div x-html="$store.menuSection.currentLesson.article"></div>
                    </div>
                </template>
            </div>
        </div>
    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

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