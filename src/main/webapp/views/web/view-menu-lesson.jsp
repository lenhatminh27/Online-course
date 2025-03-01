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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

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

        .cursor-pointer:hover {
            color: #0d6efd;
        }

        .menu-scroller {
            height: 80vh;
            overflow-y: scroll;
            scroll-snap-type: y mandatory;
        }

        .content-scroller {
            height: 88vh;
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
            background-color: #fafafa; /* Màu xám, bạn có thể thay đổi theo ý muốn */
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

        .comment-button {
            right: 20px;
            bottom: 10px;
            background-color: #74C0FC;
            opacity: 80%;
            color: white;
            width: 150px;
            border: none;
            border-radius: 50px;
            padding: 10px 0;
        }

        .comment-modal {
            top: 0;
            right: 0;
            height: 100%;
            position: fixed;
            width: 100vw;
        }

        .comment-modal .modal-content {
            top: 0;
            right: -242px;
            height: 100%;
            width: 50vw;
        }

        .avatar {
            background-color: white;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .account h5 {
            margin-bottom: auto;
            margin-top: auto;
            color: #0d6efd;
        }

        .comment-input {
            min-height: 50px;
            margin-bottom: 10px;
        }

        .comment-group {
            width: 70%;
        }

        .btn {
            padding: 5px 20px !important;
        }

        .input-group {
            margin-bottom: 10px;
        }

        .dropdown:hover .dropdown-menu {
            display: block;
            margin-top: 0;
        }

        .dropdown-menu {
            position: absolute;
            left: auto;
            right: 1%;
            top: 30px;
        }
    </style>
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
                window.location.assign('/404');
            }

            Alpine.store('menuSection', {
                sections: [],
                course: null,
                currentUser: localStorage.getItem(STORAGE_KEY.userCurrent),
                isCreator: false,
                selectedSection: [],
                currentLesson: null,
                doneLessons: [],
                progress: 80,
                searchValue: null,
                searchItem: null,
                listComments: [],
                comment: {parentId: null, content: ""},
                currentInput: null,
                noParentComment: "",
                inputUpdateComment: {commentId: null, content: ""},
                setUpdateComment(id, content) {
                    this.inputUpdateComment.commentId = id
                    this.inputUpdateComment.content = content
                },
                setCurrentInput(id) {
                    this.currentInput = id
                },
                setComment(parentId) {
                    this.comment.parentId = parentId
                },
                setInput(e) {
                    this.searchValue = e;
                },
                async loadCourse() {
                    if (!courseId) {
                        console.error("Không có courseId, không thể gọi API!");
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/course/detail/' + courseId);
                        this.course = await response;
                        this.isCreator = JSON.parse(this.currentUser).email === this.course.accountResponse.email
                        console.log(response)
                        console.log(this.isCreator)
                        console.log("This course: " + this.course);
                    } catch (error) {
                        console.error("Lỗi khi gọi API:", error);
                        window.location.assign('/404');
                    }
                },

                async loadSections() {
                    if (!courseId) {
                        console.error("Không có courseId, không thể gọi API!");
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/menu-section/' + courseId);
                        this.sections = response;
                        console.log(response);
                    } catch (error) {
                        console.error("Lỗi khi gọi API:", error);
                        window.location.assign('/404');
                    }
                },

                async loadLesson(lessonId) {
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
                        this.searchItem = {sections: [], lessons: [], articles: []};
                        return;
                    }

                    // Nếu có request cũ đang chạy, hủy nó đi
                    if (this.searchAbortController) {
                        this.searchAbortController.abort();
                    }
                    // Tạo AbortController mới cho request hiện tại
                    this.searchAbortController = new AbortController();
                    const {signal} = this.searchAbortController;

                    try {
                        const response = await fetch(environment.apiUrl + "/api/search-in-course", {
                            method: "POST",
                            body: JSON.stringify({content: this.searchValue, courseId: courseId}),
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
                        this.searchItem = {sections: [], lessons: [], articles: []};
                    }
                },

                async loadComment() {
                    try {
                        const response = await fetch(environment.apiUrl + '/api/lesson-comment/' + this.currentLesson.id);
                        this.listComments = await response.json()
                        this.listComments = this.listComments.reverse()
                        console.log("🔥 Alpine.js đã cập nhật danh sách bình luận:", this.listComments);

                    } catch (error) {
                        console.error("❌ Lỗi khi lấy bình luận:", error);
                    }
                }
                ,

                async sendComment() {
                    if (this.comment.content.length > 500) {
                        Swal.fire({
                            icon: "error",
                            title: "Gửi bình luận thất bại!",
                            text: "Nội dung bình luận không được quá 500 ký tự!"
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/lesson-comment',
                            {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                },
                                body: JSON.stringify({
                                    lessonId: this.currentLesson.id,
                                    parentId: this.comment.parentId,
                                    content: this.comment.content
                                }),
                            }
                        )
                        if (response) {
                            document.getElementById("comment").value = "";
                            this.comment = {parentId: null, content: ""}
                            this.currentInput = ""
                            await this.loadComment()
                        }
                    } catch (error) {
                        console.log("Error: " + error)
                    }
                },
                async sendNoParentComment() {
                    if (this.noParentComment.length > 500) {
                        Swal.fire({
                            icon: "error",
                            title: "Gửi bình luận thất bại!",
                            text: "Nội dung bình luận không được quá 500 ký tự!"
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/lesson-comment',
                            {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                },
                                body: JSON.stringify({
                                    lessonId: this.currentLesson.id,
                                    content: this.noParentComment
                                }),
                            }
                        )
                        if (response) {
                            document.getElementById("comment").value = "";
                            this.comment = {parentId: null, content: ""}
                            this.noParentComment = ""
                            await this.loadComment()
                        }
                    } catch (error) {
                        console.log("Error: " + error)
                    }
                },
                async updateComment() {
                    if (this.inputUpdateComment.content.length > 500) {
                        Swal.fire({
                            icon: "error",
                            title: "Gửi bình luận thất bại!",
                            text: "Nội dung bình luận không được để trống!"
                        });
                        return;
                    }
                    Swal.fire({
                        icon: "warning",
                        title: "Bạn có chắc chắn?",
                        text: "Hành động này không thể hoàn tác!",
                        showCancelButton: "true",
                        confirmButtonColor: "#d33",
                        cancelButtonColor: "#3085d6",
                        confirmButtonText: "Đồng ý",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                            if (result.isConfirmed) {
                                try {
                                    const response = await apiRequestWithToken(environment.apiUrl + '/api/lesson-comment',
                                        {
                                            method: "PUT",
                                            headers: {
                                                "Content-Type": "application/json",
                                            },
                                            body: JSON.stringify({
                                                commentId: this.inputUpdateComment.commentId,
                                                content: this.inputUpdateComment.content
                                            }),
                                        }
                                    )
                                    if (response) {
                                        this.inputUpdateComment.commentId = null
                                        this.inputUpdateComment.content = ""
                                        await this.loadComment()
                                    }
                                } catch (error) {

                                }
                            }
                        }
                    )
                },
                async deleteComment(id) {
                    Swal.fire({
                        icon: "warning",
                        title: "Bạn có chắc chắn?",
                        text: "Hành động này không thể hoàn tác!",
                        showCancelButton: "true",
                        confirmButtonColor: "#d33",
                        cancelButtonColor: "#3085d6",
                        confirmButtonText: "Đồng ý",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                            if (result.isConfirmed) {
                                try {
                                    await apiRequestWithToken(environment.apiUrl + '/api/lesson-comment/' + id,
                                        {
                                            method: "DELETE",
                                            headers: {
                                                "Content-Type": "application/json",
                                            },
                                        }
                                    )
                                    await this.loadComment()
                                } catch (error) {

                                }
                            }
                        }
                    )
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

            Alpine.store('menuSection').loadCourse();
            Alpine.store('menuSection').loadSections();
            Alpine.store('menuSection').loadSearch();
            Alpine.store('menuSection').loadComment();
            console.log("Data in store:", Alpine.store('menuSection').searchItem);

        });

    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

</head>
<body x-data>

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

<div class="d-flex flex-column pl-2">

    <div class="row w-105 ">
        <nav class="navbar nav-color navbar-expand-lg bg-body-tertiary d-flex justify-content-lg-between pt-3 pl-5">
            <div>
                <p class="navbar-brand fs-1 text-light" href="#" x-text="$store.menuSection.course.data.title"></p>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
                        aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
            </div>
            <div>
                <button class="search" data-bs-toggle="modal" data-bs-target="#staticBackdrop"><span><i
                        class="fa-solid fa-magnifying-glass "></i></span><span>Tìm gì à? Để tôi giúp
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
        <div class="myModal modal fade" id="staticBackdrop" data-bs-backdrop="true" data-bs-keyboard="false"
             tabindex="-1"
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
                                    <template x-for="(lesson, index) in section.menuLessons" :key="index">
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
        <div class="col-3">
            <div class='border-bottom'>
                <h1 class="pl-1 pt-2 mb-0">Nội dung khoá học</h1>
            </div>
            <div class="menu-scroller">
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
        <div class="col-9 ">
            <div class="content-scroller">
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

<!-- Modal Bình Luận -->
<div class="modal fadeInRight animated comment-modal" id="exampleModal" tabindex="-1"
     aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div class="comment-group">
                    <textarea class="input-group comment-input" id="comment"
                              x-model="$store.menuSection.noParentComment"></textarea>
                    <button class="btn btn-outline-primary" @click="$store.menuSection.sendNoParentComment()">Gửi
                    </button>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <template x-if="$store.menuSection.listComments && $store.menuSection.listComments.length > 0">
                    <template x-for="(comment, index) in $store.menuSection.listComments" :key="index">
                        <div>
                            <div class="bg-gray rounded-2 p-4 pt-2 ml-1 mt-4">
                                <div class="d-flex justify-content-lg-between mb-2">
                                    <div class="d-flex account">
                                        <template x-if="comment.account.avatar">
                                            <img src="comment.account.avatar" class="rounded-50"/>
                                        </template>

                                        <template x-if="!comment.account.avatar">
                                            <div class="avatar mr-2"><i class="fa-solid fa-user-tie"></i></div>
                                        </template>
                                        <h5 x-text="comment.account.email"></h5>
                                    </div>
                                    <div class="d-flex">
                                        <p class="cursor-pointer" @click="$store.menuSection.setComment(comment.id)
                                                                      $store.menuSection.setCurrentInput(comment.id)">
                                            Phản hồi</p>
                                        <template
                                                x-if="JSON.parse($store.menuSection.currentUser).email === comment.account?.email || $store.menuSection.isCreator === true">
                                            <div class="dropdown dropstart">
                                                <i class="fa-solid fa-ellipsis-vertical ml-5 mt-2 cursor-pointer"
                                                   type="button" data-bs-toggle="dropdown"></i>
                                                <ul class="dropdown-menu">
                                                    <template x-if="JSON.parse($store.menuSection.currentUser).email === comment.account?.email">
                                                    <li @click="$store.menuSection.setUpdateComment(comment.id, comment.content)">
                                                        <a
                                                                class="dropdown-item">Sửa bình luận</a></li>
                                                    </template>
                                                    <li @click="$store.menuSection.deleteComment(comment.id)"><a class="dropdown-item" style="color: red">Xoá bình luận</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </template>
                                    </div>
                                </div>
                                <template x-if="$store.menuSection.inputUpdateComment.commentId !== comment.id">
                                    <p class="ml-3 text-wrap text-break" x-text="comment.content"></p>
                                </template>
                                <template x-if="$store.menuSection.inputUpdateComment.commentId === comment.id">
                                    <div>
                                        <textarea class="input-group comment-input"
                                                  x-text="$store.menuSection.inputUpdateComment.content"
                                                  x-model="$store.menuSection.inputUpdateComment.content"></textarea>
                                        <button class="btn btn-outline-primary"
                                                @click="$store.menuSection.updateComment()">Sửa
                                        </button>
                                    </div>
                                </template>
                                <p style="color: #0d6efd"
                                   x-text="new Date(comment.createdAt).toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit', second: '2-digit', day: '2-digit', month: '2-digit', year: 'numeric' })"></p>

                                <template x-if="$store.menuSection.currentInput === comment.id">
                                    <div class="comment-group comment-input">
                                        <textarea class="input-group"
                                                  x-model="$store.menuSection.comment.content"></textarea>
                                        <button class="btn btn-outline-primary"
                                                @click="$store.menuSection.sendComment()">
                                            Gửi
                                        </button>
                                    </div>
                                </template>
                            </div>
                            <template x-if="comment.childrenComments && comment.childrenComments.length > 0">
                                <template x-for="(child, index) in comment.childrenComments" :key="index">
                                    <div class="bg-gray rounded-2 p-4 pt-2 ml-5 mt-4 border-left border-2 border-primary">
                                        <div class="d-flex justify-content-lg-between mb-2">
                                            <div class="d-flex account">
                                                <template x-if="child.account.avatar">
                                                    <img src="child.account.avatar" class="rounded-50"/>
                                                </template>

                                                <template x-if="!child.account.avatar">
                                                    <div class="avatar mr-2"><i class="fa-solid fa-user-tie"></i></div>
                                                </template>
                                                <h5 x-text="child.account.email"></h5>
                                            </div>
                                            <div class="d-flex">
                                                <p class="ml-3 cursor-pointer " @click="$store.menuSection.setComment(comment.id)
                                                                                   $store.menuSection.setCurrentInput(child.id)
                                            ">Phản hồi</p>
                                                <template
                                                        x-if="JSON.parse($store.menuSection.currentUser).email === child.account?.email || $store.menuSection.isCreator === true">
                                                    <div class="dropdown dropstart ">
                                                        <i class="fa-solid fa-ellipsis-vertical ml-5 mt-2 cursor-pointer"
                                                           type="button" data-bs-toggle="dropdown"></i>
                                                        <ul class="dropdown-menu">
                                                            <template x-if="JSON.parse($store.menuSection.currentUser).email === child.account?.email">
                                                                <li @click="$store.menuSection.setUpdateComment(child.id, child.content)">
                                                                    <a
                                                                            class="dropdown-item">Sửa bình luận</a></li>
                                                            </template>
                                                            <li @click="$store.menuSection.deleteComment(child.id)"><a class="dropdown-item" style="color: red">Xoá bình luận</a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </template>
                                            </div>
                                        </div>
                                        <template x-if="$store.menuSection.inputUpdateComment.commentId !== child.id">
                                            <p class="ml-3 text-wrap text-break" x-text="child.content"></p>
                                        </template>
                                        <template x-if="$store.menuSection.inputUpdateComment.commentId === child.id">
                                            <div>
                                        <textarea class="input-group comment-input"
                                                  x-text="$store.menuSection.inputUpdateComment.content"
                                                  x-model="$store.menuSection.inputUpdateComment.content"></textarea>
                                                <button class="btn btn-outline-primary"
                                                        @click="$store.menuSection.updateComment()">Sửa
                                                </button>
                                            </div>
                                        </template>

                                        <p style="color: #0d6efd"
                                           x-text="new Date(child.createdAt).toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit', second: '2-digit', day: '2-digit', month: '2-digit', year: 'numeric' })"></p>


                                        <template x-if="$store.menuSection.currentInput === child.id">
                                            <div class="comment-group comment-input ">
                                                <textarea class="input-group"
                                                          x-model="$store.menuSection.comment.content"></textarea>
                                                <button class="btn btn-outline-primary"
                                                        @click="$store.menuSection.sendComment()">
                                                    Gửi
                                                </button>
                                            </div>
                                        </template>
                                    </div>

                                </template>

                            </template>
                        </div>
                    </template>
                </template>
            </div>
        </div>
    </div>
</div>

<!-- Button mở modal -->
<template x-if="$store.menuSection.currentLesson !== null">
    <button class="comment-button position-fixed" @click="$store.menuSection.loadComment(); renderReactComments();"
            data-bs-toggle="modal" data-bs-target="#exampleModal">
        <i class="fa-regular fa-comments" style="color: white; margin-right: 5px"></i> Bình luận
    </button>
</template>


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