<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>DASHMIN - Bootstrap Admin Template</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

    <link href="img/favicon.ico" rel="icon">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <link href="../../assets/admin/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="../../assets/admin/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet"/>
    <link href="../../assets/admin/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../assets/admin/css/style.css" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
</head>
<script type="module">
    import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';

    const pathSegments = window.location.pathname.split('/');
    const menuSectionIndex = pathSegments.indexOf("review-course-detail");
    let courseId = null;
    if (menuSectionIndex !== -1 && menuSectionIndex + 1 < pathSegments.length) {
        courseId = pathSegments[menuSectionIndex + 1];
        console.log("Course ID:", courseId);

    } else {
        console.error("Không tìm thấy courseId trong URL");
    }

    async function loadSections() {
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

    async function loadCourse() {
        try {
            const response = await apiRequestWithToken(environment.apiUrl + "/api/review-course-detail/" + courseId)
            console.log(response.json())
        } catch (e) {
            console.log("error: " + e)
        }
    }

    window.loadSections = loadSections;
    window.loadCourse = loadCourse;


</script>
<script type="module">
    import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';

    document.addEventListener('alpine:init', () => {

        const pathSegments = window.location.pathname.split('/');
        const menuSectionIndex = pathSegments.indexOf("review-course-detail");
        let courseId = null;
        if (menuSectionIndex !== -1 && menuSectionIndex + 1 < pathSegments.length) {
            courseId = pathSegments[menuSectionIndex + 1];
            console.log("Course ID:", courseId);
        } else {
            console.error("Không tìm thấy courseId trong URL");
        }

        Alpine.store('reviewCourse', {
            course: null,
            sections: [],
            lessons: [],
            currentLesson: null,

            async loadCourse() {
                try {
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/review-course-detail/" + courseId)
                    this.course = await response
                } catch (e) {
                    console.log(e)
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

            async sendData(email, inputValue, sectionTittle, lessonTittle) {
                if (inputValue.trim() === "") {
                    Swal.fire({
                        title: "Phản hồi không được để trống!",
                        icon: "warning",
                        draggable: true
                    });
                    return;
                }
                let message;
                if (lessonTittle === "") {
                    message = inputValue + ' tại bài giảng ' + sectionTittle;
                } else {
                    message = inputValue + ' tại bài giảng ' + sectionTittle + ': ' + lessonTittle;
                }
                const requestData = {
                    email: email,
                    message: message
                };

                try {
                    const response = await apiRequestWithToken(environment.apiUrl + '/api/review-course-detail', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(requestData)
                    });
                    if (response) {
                        Swal.fire({
                            title: "Gửi phản hồi thành công!",
                            icon: "success",
                            draggable: true,
                            confirmButtonText: "OK"
                        });
                    } else {
                        Swal.fire({
                            title: "Gửi phản hồi thất bại!",
                            icon: "warning",
                            draggable: true
                        });
                    }

                    Swal.fire({
                        title: "Gửi phản hồi thành công!",
                        icon: "success",
                        draggable: true,
                        confirmButtonText: "OK"
                    });
                } catch (error) {
                    console.error("Error during API request:", error);
                }
            },
            async acceptCourse() {
                if (!courseId) {
                    Swal.fire({
                        title: "Không có courseId",
                        icon: "error",
                        draggable: true,
                    });
                    return;
                }

                try {
                    const response = await apiRequestWithToken(environment.apiUrl + '/api/review-course-detail/' + courseId + '?action=accept', {
                        method: 'PUT',
                    });
                    if (response) {
                        Swal.fire({
                            title: "Khóa học đã được chấp nhận!",
                            icon: "success",
                            draggable: true,
                            confirmButtonText: "OK",
                        }).then(() => {
                            window.location.href = "http://localhost:8080/admin/review-course";  // Điều hướng về trang review-course
                        });
                    }
                } catch (error) {
                    console.error("Error during API request:", error);
                    Swal.fire({
                        title: "Lỗi khi chấp nhận khóa học!",
                        icon: "error",
                        draggable: true,
                    });
                }
            },
            async rejectCourse() {
                if (!courseId) {
                    Swal.fire({
                        title: "Không có courseId",
                        icon: "error",
                        draggable: true,
                    });
                    return;
                }

                try {
                    const response = await apiRequestWithToken(environment.apiUrl + '/api/review-course-detail/' + courseId + '?action=reject', {
                        method: 'PUT',
                    });
                    if (response) {
                        Swal.fire({
                            title: "Yêu cầu chỉnh sửa khóa học đã được gửi!",
                            icon: "success",
                            draggable: true,
                            confirmButtonText: "OK",
                        }).then(() => {
                            // Điều hướng sau khi thông báo thành công
                            window.location.href = "http://localhost:8080/admin/review-course";  // Điều hướng về trang review-course
                        });
                    }
                } catch (error) {
                    console.error("Error during API request:", error);
                    Swal.fire({
                        title: "Lỗi khi yêu cầu chỉnh sửa khóa học!",
                        icon: "error",
                        draggable: true,
                    });
                }
            }


        });

        Alpine.store('reviewCourse').loadCourse();
        Alpine.store('reviewCourse').loadSections();
        Alpine.store('reviewCourse').loadLesson();
    })
    ;
</script>
<script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
<style>

    .modal-dialog {
        margin-left: 15vw;
        width: 70vw;
    }

    .modal-content {
        width: 70vw;
        left: 10px;
    }
</style>
<body>
<div class="container-xxl position-relative bg-white d-flex p-0">
    <%@include file="../../common/admin/menu.jsp" %>
    <div class="content" x-data>
        <%@include file="../../common/admin/header.jsp" %>
        <div class="container mt-4 col-md-11  ">
            <h1 x-text="$store.reviewCourse.course.title"></h1>
            <p class="lead"></p>
            <p x-text="'Được tạo bởi: ' + $store.reviewCourse.course.accountResponse.email"></p>
            <p x-text="'Sửa lần cuối vào: ' + $store.reviewCourse.course.updatedAt"></p>
        </div>
        <div class="container mt-4 col-md-11  ">
            <h1 x-text="'Giá tiền: ' + $store.reviewCourse.course.price"></h1>
        </div>
        <div class="container mt-5 card p-4 me-5 col-md-11">
            <h2 class="fw-bold">Mục tiêu khóa học</h2>
            <div class="row">
                <div class="col-md-6">
                    <template
                            x-for="(section, index) in $store.reviewCourse.sections.slice(0, Math.ceil($store.reviewCourse.sections.length / 2))"
                            :key="section.id">
                        <div class="d-flex">
                            <i class="bi bi-check-circle"></i>
                            <p x-text="section.target"></p>
                        </div>
                    </template>
                </div>
                <div class="col-md-6">
                    <template
                            x-for="(section, index) in $store.reviewCourse.sections.slice(Math.ceil($store.reviewCourse.sections.length / 2))"
                            :key="section.id">
                        <div class="d-flex">
                            <i class="bi bi-check-circle"></i>
                            <p x-text="section.target"></p>
                        </div>
                    </template>
                </div>
            </div>
        </div>


        <!-- Nội dung khóa học với Accordion -->
        <div class="container mt-5 col-md-11">
            <h2>Nội dung khoá học</h2>
            <template x-for="(section, index) in $store.reviewCourse.sections" :key="index">
                <div class="accordion" id="courseContent">
                    <div class="accordion-item">
                        <h2 class="accordion-header d-flex align-items-center" id="headingOne">
                            <!-- Nút mở ô nhập -->
                            <button class="btn btn-info ms-2" @click="section.showInput = !section.showInput">
                                <i class="bi bi-chat-dots"></i>
                            </button>

                            <!-- Tiêu đề accordion -->
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                    :data-bs-target="'#collapseOne' + index" aria-expanded="false"
                                    aria-controls="collapseOne"
                                    x-text="section.title">
                            </button>
                        </h2>

                        <!-- Ô nhập dữ liệu -->
                        <div x-show="section.showInput" class="mt-2 mx-2">
                            <input type="text" class="form-control" placeholder="Nhập phản hồi..."
                                   x-model="section.inputValue" required>
                            <button class="btn btn-primary mt-2"
                                    @click="$store.reviewCourse.sendData($store.reviewCourse.course.accountResponse.email, section.inputValue, section.title, '')">
                                Gửi
                            </button>
                        </div>

                        <div :id="'collapseOne' + index" class="accordion-collapse collapse"
                             data-bs-parent="#courseContent">
                            <template x-for="(lesson, lessonIndex) in section.menuLessons">
                                <div class="d-flex align-items-center">
                                    <!-- Nút mở ô nhập -->
                                    <button class="btn btn-secondary ms-2"
                                            @click="lesson.showInput = !lesson.showInput">
                                        <i class="bi bi-chat-dots"></i>
                                    </button>

                                    <!-- Tiêu đề bài học -->
                                    <div type="button" class="accordion-body me-2" x-text="lesson.title"
                                         @click="$store.reviewCourse.loadLesson(lesson.id)"
                                         data-bs-toggle="modal"
                                         data-bs-target="#modal">
                                    </div>

                                    <!-- Ô nhập dữ liệu cho bài học -->
                                    <div x-show="lesson.showInput" class="mt-2 mx-2">
                                        <div class="d-flex">
                                            <input style="margin-right:5px" type="text" class="form-control"
                                                   placeholder="Nhập phản hồi... "
                                                   x-model="lesson.inputValue">
                                            <button class="btn btn-primary mt-2"
                                                    @click="$store.reviewCourse.sendData($store.reviewCourse.course.accountResponse.email, lesson.inputValue, section.title, lesson.title)">
                                                Gửi
                                            </button>
                                        </div>
                                    </div>

                                </div>
                            </template>
                        </div>
                    </div>
                </div>
            </template>
        </div>


        <div class="modal fade" id="modal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="lesson-tittle" x-text="$store.reviewCourse.currentLesson.title"></h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <template x-if="$store.reviewCourse.currentLesson.videoUrl">
                            <div>
                                <video width="100%" controls preload="metadata">
                                    <source :src=$store.reviewCourse.currentLesson.videoUrl type="video/mp4">
                                    Trình duyệt của bạn không hỗ trợ video.
                                </video>
                            </div>
                        </template>
                        <template x-if="$store.reviewCourse.currentLesson.article">
                            <div class="article">
                                <div x-html="$store.reviewCourse.currentLesson.article"></div>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>

        <div class="container mt-4 col-md-11 " x-data>
            <h2>Mô tả</h2>
            <strong x-text="$store.reviewCourse.course.description"></strong>
        </div>

        <div class="container mt-4 col-md-11 text-center">
            <button class="btn btn-success me-2" @click="$store.reviewCourse.acceptCourse()">
                <i class="bi bi-check-circle"></i> Đồng ý
            </button>

            <button class="btn btn-warning me-2 text-white" @click="$store.reviewCourse.rejectCourse()">
                <i class="bi bi-pencil-square"></i> Yêu cầu chỉnh sửa thêm
            </button>
        </div>

    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="../../assets/admin/lib/chart/chart.min.js"></script>
<script src="../../assets/admin/lib/easing/easing.min.js"></script>
<script src="../../assets/admin/lib/waypoints/waypoints.min.js"></script>
<script src="../../assets/admin/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="../../assets/admin/lib/tempusdominus/js/moment.min.js"></script>
<script src="../../assets/admin/lib/tempusdominus/js/moment-timezone.min.js"></script>
<script src="../../assets/admin/lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>
<script src="../../assets/admin/js/main.js"></script>
</body>

</html>
