<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chương trình giảng dạy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        function updateUrlSegment(oldSegment, newSegment) {
            let urlParts = window.location.pathname.split('/');
            let index = urlParts.indexOf(oldSegment);
            if (index !== -1 && urlParts[index + 1]) {
                urlParts[index] = newSegment; // Thay thế segment
                let newUrl = window.location.origin + urlParts.join('/');
                window.location.href = newUrl;
            } else {
                console.error(`Không tìm thấy segment:`);
            }
        }

    </script>
    <script src="https://cdn.ckeditor.com/4.20.0/standard/ckeditor.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <style>
        .ck-editor__editable {
            min-height: 300px !important; /* Đặt chiều cao tối thiểu */
        }

        .btn-icon {
            background: none;
            border: none;
            color: #333;
            cursor: pointer;
            font-size: 14px;
        }


        .btn-icon:hover {
            color: #007bff;
        }

        .btn-icon {
            opacity: 0;
            transition: opacity 0.2s ease-in-out;
        }

        .list-group-item:hover .btn-icon,
        h5:hover .btn-icon {
            opacity: 1;
        }


        input.form-control {
            width: 100%;
            padding: 8px;
            font-size: 14px;
            border-radius: 5px;
        }

        .delete-btn {
            background-color: #dc3545; /* Màu đỏ Bootstrap */
            color: white; /* Chữ trắng */
            border: none;
            padding: 8px 12px;
            font-size: 14px;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease-in-out;
        }

        .delete-btn:hover {
            background-color: #c82333; /* Màu đỏ đậm hơn khi hover */
        }

        .delete-btn:active {
            background-color: #a71d2a;
        }


        .delete-video-btn {
            background-color: #dc3545; /* Màu đỏ Bootstrap */
            color: white; /* Chữ trắng */
            border: none;
            padding: 8px 12px;
            font-size: 14px;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease-in-out;
        }

        .delete-video-btn:hover {
            background-color: #c82333; /* Màu đỏ đậm hơn khi hover */
        }

        .delete-video-btn:active {
            background-color: #a71d2a;
        }

        .course-status {
            font-weight: bold;
            margin-top: 15px;
            text-align: center;
            padding: 10px;
            border-top: 1px solid #ddd;
        }

        .status-badge {
            background: #ff9800;
            color: white;
            padding: 5px 10px;
            border-radius: 5px;
            font-size: 14px;
            display: inline-block;
            margin-top: 5px;
        }


    </style>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        const pathSegments = window.location.pathname.split('/');
        const curriculumIndex = pathSegments.indexOf("curriculum");
        let courseId = null;
        if (curriculumIndex !== -1 && curriculumIndex + 1 < pathSegments.length) {
            courseId = pathSegments[curriculumIndex + 1];
            console.log("Course ID:", courseId);
        } else {
            console.error("Không tìm thấy courseId trong URL");
        }

        async function loadSection() {
            if (!courseId) {
                console.error("Không có courseId, không thể gọi API!");
                return [];
            }

            const apiUrl = environment.apiUrl + '/api/section/' + courseId;
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

        // Attach function to window so Alpine.js can access it
        window.loadSection = loadSection;
    </script>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        document.addEventListener('alpine:init', () => {

            const pathSegments = window.location.pathname.split('/');
            const curriculumIndex = pathSegments.indexOf("curriculum");
            let courseId = null;
            if (curriculumIndex !== -1 && curriculumIndex + 1 < pathSegments.length) {
                courseId = pathSegments[curriculumIndex + 1];
                console.log("Course ID:", courseId);
            } else {
                console.error("Không tìm thấy courseId trong URL");
            }

            Alpine.store('curriculum', {
                hoveredSection: null,
                hoveredLesson: null,
                sections: [],
                showFormSection: false,
                showFormLesson: {},  // Sử dụng object để lưu trạng thái mở form theo từng section
                newSectionTitle: '',
                newSectionGoal: '',
                selectedSectionId: null,
                selectedContentType: {},
                videoLinks: {},
                newLessonTitle: {},
                file: null,
                chunkSize: 5 * 1024 * 1024, // 5MB mỗi chunk
                totalChunks: 0,
                uploadedChunks: 0,
                uploading: false,
                progress: 0,
                videoUrl: "",
                articleEditors: {}, // Lưu các instance CKEditor theo lessonId
                editingSection: null, // ID của section đang chỉnh sửa
                editedTitle: "", // Lưu tiêu đề khi chỉnh sửa
                editedTarget: "",// Lưu mục tiêu học tập khi chỉnh sửa
                editingLesson: null, // ID của section đang chỉnh sửa
                editedLessonTitle: "",// Lưu tiêu đề bài học khi chỉnh sửa
                course: null,
                canEdit: true,


                toggleLessonForm(sectionId) {
                    this.showFormLesson = {};
                    this.showFormLesson[sectionId] = !this.showFormLesson[sectionId];

                    if (!this.newLessonTitle[sectionId]) {
                        this.newLessonTitle[sectionId] = '';
                    }
                },

                async loadSections() {
                    if (!courseId) {
                        console.error("Không có courseId, không thể gọi API!");
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/section/' + courseId);
                        this.sections = response;

                        setTimeout(() => {
                            this.initializeEditors();
                        }, 300);
                    } catch (error) {
                        console.error("Lỗi khi gọi API:", error);
                    }
                },

                async saveLessonContent(lessonId, editorInstance) {
                    let updatedContent = editorInstance.getData(); // Lấy dữ liệu từ CKEditor
                    console.log("Lưu nội dung bài học ID:", lessonId, "Nội dung:", updatedContent);
                    if (!updatedContent.trim()) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Vui lòng nhập đầy đủ thông tin.",
                        });
                        return;
                    }
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/lesson", {
                        method: 'PUT',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({
                            lessonId: lessonId,
                            article: updatedContent,
                        })
                    });
                    if (response) {
                        await this.loadSections();
                        Swal.fire({
                            title: "Cập nhật nội dung học thành công!",
                            icon: "success",
                            confirmButtonText: "OK"
                        });
                    }

                },

                async deleteLessonContent(lessonId) {
                    Swal.fire({
                        title: "Bạn có muốn xóa nội dung này không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Xóa",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                        if (result.isConfirmed) {
                            await apiRequestWithToken(environment.apiUrl + "/api/lesson", {
                                method: 'PUT',
                                headers: {'Content-Type': 'application/json'},
                                body: JSON.stringify({
                                    lessonId: lessonId,
                                    article: "delete",
                                })
                            });
                            await this.loadSections();
                            Swal.fire("Xóa thành công!", "", "success");
                        }
                    });
                },

                initializeEditors() {
                    this.sections.forEach(section => {
                        section.lessons.forEach(lesson => {
                            if (lesson.article) {
                                let editorId = `editor-view-` + lesson.id;
                                let editorElement = document.getElementById(editorId);
                                if (editorElement && !editorElement.dataset.ckeditorInitialized) {
                                    // Khởi tạo CKEditor
                                    CKEDITOR.replace(editorId, {height: 400});
                                    let editorInstance = CKEDITOR.instances[editorId];
                                    editorInstance.setData(lesson.article); // Đặt nội dung từ API vào CKEditor
                                    editorElement.dataset.ckeditorInitialized = "true"; // Đánh dấu đã khởi tạo

                                    if(this.canEdit){
                                        // Thêm nút "Lưu thay đổi"
                                        let saveButton = document.createElement("button");
                                        saveButton.textContent = "Lưu thay đổi";
                                        saveButton.classList.add("save-btn");
                                        saveButton.style.marginTop = "10px";
                                        saveButton.onclick = () => this.saveLessonContent(lesson.id, editorInstance);

                                        // Thêm nút "Xóa bài viết"
                                        let deleteButton = document.createElement("button");
                                        deleteButton.textContent = "Xóa bài viết";
                                        deleteButton.classList.add("delete-btn");
                                        deleteButton.style.marginLeft = "10px";
                                        deleteButton.onclick = () => this.deleteLessonContent(lesson.id);

                                        // Thêm nút vào DOM
                                        let buttonContainer = document.createElement("div");
                                        buttonContainer.style.marginTop = "10px";
                                        buttonContainer.appendChild(saveButton);
                                        buttonContainer.appendChild(deleteButton);
                                        editorElement.parentElement.appendChild(buttonContainer);
                                    }

                                }
                            }
                        });
                    });
                },


                async addSection() {
                    if (!this.newSectionTitle.trim() || !this.newSectionGoal.trim()) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Vui lòng nhập đầy đủ thông tin.",
                        });
                        return;
                    }
                    if (this.newSectionTitle.length > 80) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Tiêu đề phần không được vượt quá 80 ký tự.",
                        });
                        return;
                    }
                    if (this.newSectionGoal.length > 200) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Mục tiêu phần không được vượt quá 200 ký tự.",
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/section', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({
                                title: this.newSectionTitle,
                                target: this.newSectionGoal,
                                courseId: courseId,
                            })
                        });
                        if (response) {
                            this.newSectionTitle = '';
                            this.newSectionGoal = '';
                            this.showFormSection = false;
                            // Reload sections
                            await this.loadSections();
                            // Show success notification
                            Swal.fire({
                                title: "Tạo phần học thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                        }
                    } catch (error) {
                        console.log(error);
                        if (error.response?.status === 400 && error.data?.error) {
                            console.error('Validation errors:', error.data.error);
                            let errorMess = "";
                            for (const x of error.data.error) {
                                errorMess += x;
                            }
                            Swal.fire({
                                icon: "error",
                                title: "Tạo phần học thất bại",
                                text: errorMess,
                            });
                        } else {
                            let errorMessage = error.message || 'A network error occurred.';
                            console.error('Error:', error);
                            Swal.fire({
                                icon: "error",
                                title: "Tạo phần học thất bại",
                                text: errorMessage,
                            });
                        }
                    }
                },


                async addLesson(sectionId) {
                    if (!this.newLessonTitle[sectionId] || this.newLessonTitle[sectionId].trim() === "") {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Vui lòng nhập tiêu đề bài học.",
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/lesson', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({
                                title: this.newLessonTitle[sectionId],
                                sectionId: sectionId,
                            })
                        });
                        if (response) {
                            this.newLessonTitle[sectionId] = '';
                            this.showFormLesson[sectionId] = false;
                            await this.loadSections();
                            Swal.fire({
                                title: "Tạo bài học thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                        }
                    } catch (error) {
                        console.log(error);
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Đã xảy ra lỗi trong quá trình tạo bài học.",
                        });
                    }
                },

                // Chọn bài học
                selectLesson(lessonId) {
                    this.selectedLessonId = (this.selectedLessonId === lessonId ? null : lessonId);
                },

                // Chọn loại nội dung (riêng cho từng bài học)
                async selectContentType(lessonId, type) {
                    this.selectedContentType[lessonId] = type;
                    if (type === 'article') {
                        setTimeout(() => {
                            let editorId = `editor-` + lessonId;
                            if (!CKEDITOR.instances[editorId]) {
                                CKEDITOR.replace(editorId, {
                                    height: 400 // Đặt chiều cao CKEditor
                                });
                            }
                        }, 100);
                    }
                },


                // Lưu bài viết
                async saveArticle(lessonId) {
                    const content = CKEDITOR.instances['editor-' + lessonId].getData();
                    if (content.trim() === '') {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Bài viết không được để trống!.",
                        });
                        return;
                    }
                    const response2 = await apiRequestWithToken(environment.apiUrl + '/api/lesson', {
                        method: "PUT",
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify({
                            lessonId: lessonId,
                            article: content,
                        })
                    });
                    if (response2) {
                        Swal.fire({
                            title: "Tải bài viết thành công!",
                            icon: "success",
                            draggable: true,
                            confirmButtonText: "OK"
                        }).then(async (result) => {
                            if (result.isConfirmed) {
                                CKEDITOR.instances['editor-' + lessonId].setData("");
                                // Đóng form nhập bài viết
                                this.selectedContentType[lessonId] = null;
                                await this.loadSections(courseId);
                            }
                        });
                    }
                }
                ,


                async uploadVideo(lessonId, event) {
                    const file = event.target.files[0]; // Lấy file từ input
                    if (!file) return;

                    // Kiểm tra dung lượng file (giới hạn 4GB)
                    if (file.size > 4 * 1024 * 1024 * 1024) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "File quá lớn! Vui lòng chọn file nhỏ hơn 4GB.",
                        });
                        return;
                    }

                    // Khởi tạo biến để chia file
                    this.file = file;
                    this.totalChunks = Math.ceil(file.size / this.chunkSize);
                    this.uploadedChunks = 0;
                    this.uploading = true;
                    this.progress = 0;

                    // Gửi từng chunk lên server
                    for (let chunkIndex = 0; chunkIndex < this.totalChunks; chunkIndex++) {
                        let start = chunkIndex * this.chunkSize;
                        let end = Math.min(start + this.chunkSize, file.size);
                        let chunk = file.slice(start, end);

                        let formData = new FormData();
                        formData.append("file", chunk);
                        formData.append("fileName", file.name);
                        formData.append("chunkIndex", chunkIndex);
                        formData.append("totalChunks", this.totalChunks);

                        try {
                            let response = await apiRequestWithToken(environment.apiUrl + '/api/truck', {
                                method: "POST",
                                body: formData
                            });

                            console.log(response);
                            this.uploadedChunks++;
                            this.progress = Math.round((this.uploadedChunks / this.totalChunks) * 100);

                            // Khi tất cả chunks đã tải xong, gọi API merge
                            if (this.uploadedChunks === this.totalChunks) {
                                await this.mergeFile(lessonId, file.name);
                            }

                        } catch (error) {
                            console.error("Lỗi khi tải chunk:", error);
                            Swal.fire({
                                icon: "error",
                                title: "Lỗi",
                                text: "Có lỗi xảy ra khi tải video lên.",
                            });
                            return;
                        }
                    }
                }
                ,

                // Gọi API `/api/merge` để ghép file sau khi tải xong
                async mergeFile(lessonId, fileName) {
                    try {
                        let response = await apiRequestWithToken(environment.apiUrl + '/api/truck', {
                            method: "PUT",
                            headers: {"Content-Type": "application/json"},
                            body: JSON.stringify({fileName: fileName})
                        });
                        console.log(response);
                        this.videoUrl = response.file;
                        const response2 = await apiRequestWithToken(environment.apiUrl + '/api/lesson', {
                            method: "PUT",
                            headers: {"Content-Type": "application/json"},
                            body: JSON.stringify({
                                lessonId: lessonId,
                                videoUrl: response.file,
                            })
                        });
                        if (response2) {
                            await this.loadSections(courseId);
                            Swal.fire({
                                title: "Tải video lên thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                        }

                    } catch (error) {
                        console.error("Lỗi khi ghép file:", error);
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Có lỗi xảy ra khi ghép video.",
                        });
                    } finally {
                        this.uploading = false;
                    }
                },

                async saveEditedSection(sectionId) {
                    if (!this.editedTitle.trim()) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Tiêu đề không được để trống!",
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/section', {
                            method: "PUT",
                            headers: {"Content-Type": "application/json"},
                            body: JSON.stringify(
                                {sectionId: sectionId, title: this.editedTitle, target: this.editedTarget}
                            )

                        });
                        if (response) {
                            await this.loadSections(); // Cập nhật danh sách sections
                            Swal.fire({
                                title: "Cập nhật tiêu đề thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });

                        }
                    } catch (error) {
                        console.log(error);
                        if (error.response?.status === 400 && error.data?.error) {
                            console.error('Validation errors:', error.data.error);
                            let errorMess = "";
                            for (const x of error.data.error) {
                                errorMess += x;
                            }
                            Swal.fire({
                                icon: "error",
                                title: "Tạo phần học thất bại",
                                text: errorMess,
                            });
                        } else {
                            let errorMessage = error.message || 'A network error occurred.';
                            console.error('Error:', error);
                            Swal.fire({
                                icon: "error",
                                title: "Tạo phần học thất bại",
                                text: errorMessage,
                            });
                        }
                    }
                },


                async saveEditedLesson(lessonId) {
                    if (!this.editedLessonTitle.trim()) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Tiêu đề không được để trống!",
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(`/api/lesson`, {
                            method: "PUT",
                            headers: {"Content-Type": "application/json"},
                            body: JSON.stringify({title: this.editedLessonTitle.trim(), lessonId: lessonId})
                        });
                        if (response) {
                            await this.loadSections();
                            Swal.fire({
                                title: "Cập nhật tiêu đề thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                        }
                    } catch (error) {
                        console.log(error);
                        if (error.response?.status === 400 && error.data?.error) {
                            console.error('Validation errors:', error.data.error);
                            let errorMess = "";
                            for (const x of error.data.error) {
                                errorMess += x;
                            }
                            Swal.fire({
                                icon: "error",
                                title: "Tạo phần học thất bại",
                                text: errorMess,
                            });
                        } else {
                            let errorMessage = error.message || 'A network error occurred.';
                            console.error('Error:', error);
                            Swal.fire({
                                icon: "error",
                                title: "Tạo phần học thất bại",
                                text: errorMessage,
                            });
                        }
                    }
                },


                async deleteVideo(lessonId) {
                    console.log(lessonId);
                    Swal.fire({
                        title: "Bạn có muốn xóa video này không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Xóa",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                        if (result.isConfirmed) {
                            await apiRequestWithToken(environment.apiUrl + "/api/lesson/video/" + lessonId, {
                                method: "DELETE",
                            })
                            await this.loadSections();
                            Swal.fire("Xóa thành công!", "", "success");
                        }
                    });
                },

                async deleteLesson(lessonId) {
                    Swal.fire({
                        title: "Bạn có muốn xóa bài học này không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Xóa",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                        if (result.isConfirmed) {
                            await apiRequestWithToken(environment.apiUrl + "/api/lesson/" + lessonId, {
                                method: "DELETE",
                            })
                            await this.loadSections();
                            Swal.fire("Xóa thành công!", "", "success");
                        }
                    });
                },

                async deleteSection(sectionId) {
                    Swal.fire({
                        title: "Bạn có muốn xóa học phần này không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Xóa",
                        cancelButtonText: "Hủy"
                    }).then(async (result) => {
                        if (result.isConfirmed) {
                            await apiRequestWithToken(environment.apiUrl + "/api/section/" + sectionId, {
                                method: "DELETE",
                            })
                            await this.loadSections();
                            Swal.fire("Xóa thành công!", "", "success");
                        }
                    });
                },

                async loadCourse() {
                    if (!courseId) {
                        console.error("Không có courseId, không thể gọi API!");
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + '/api/course/detail/' + courseId);
                        this.course = await response;
                        console.log(response)
                        console.log(this.isCreator)
                        console.log("This course: " + this.course);
                    } catch (error) {
                        console.error("Lỗi khi gọi API:", error);
                        window.location.assign('/404');
                    }
                },

                async checkEdit(){
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/course/detail/" + courseId, {
                        method: "POST",
                    });
                    if(response){
                        this.canEdit = response.canEdit;
                    }
                },


                async sendAdminReview(){
                    const response = await apiRequestWithToken(environment.apiUrl + "/api/courseToReview/" + courseId, {
                        method: "PUT",
                    });
                    if(response){
                        await this.loadCourse();
                        await this.checkEdit();
                        Swal.fire("Chuyển trạng thái thành công!", "", "success");
                    }
                }

            })
            ;

            Alpine.store('curriculum').loadSections();
            Alpine.store('curriculum').loadCourse();
            Alpine.store('curriculum').checkEdit();

        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>


    <style>
        body {
            background-color: #f9f9f9;
        }

        .sidebar {
            width: 300px;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        /* Định dạng chung cho các icon của bài học */
        .lesson-icon {
            font-size: 1.2rem; /* Kích thước icon */
            margin-right: 0.5rem; /* Khoảng cách bên phải */
            transition: color 0.3s ease, transform 0.3s ease;
        }

        /* Màu riêng cho từng loại icon */
        .lesson-icon.video {
            color: #9c27b0; /* Màu tím cho video */
        }

        .lesson-icon.article {
            color: #28a745; /* Màu xanh lá cây cho bài viết */
        }

        .lesson-icon.both {
            color: #ffc107; /* Màu vàng cam cho cả hai */
        }

        .lesson-icon.none {
            color: #6c757d; /* Màu xám khi không có nội dung */
        }

        /* Hiệu ứng hover cho icon */
        .lesson-icon:hover {
            transform: scale(1.1);
            filter: brightness(120%);
        }


        .content {
            flex: 1;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .info-box {
            background-color: #eef;
            padding: 15px;
            border-left: 5px solid #6a0dad;
            margin-bottom: 20px;
        }

        .save-btn {
            background-color: #007bff; /* Màu xanh dương */
            color: white;
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }

        .save-btn:hover {
            background-color: #0056b3; /* Màu đậm hơn khi hover */
        }

        /* Nút Thêm Bài Viết */
        .add-article-btn {
            background-color: #007bff; /* Màu xanh dương */
            color: #fff;
            border: none;
            padding: 10px 15px;
            font-size: 14px;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease;
            margin-right: 10px; /* Tạo khoảng cách nếu cần */
        }

        .add-article-btn:hover {
            background-color: #0069d9;
        }

        /* Nút Thêm Tài Nguyên */
        .add-resource-btn {
            background-color: #17a2b8; /* Màu xanh ngọc */
            color: #fff;
            border: none;
            padding: 10px 15px;
            font-size: 14px;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        .add-resource-btn:hover {
            background-color: #138496;
        }

    </style>
</head>
<body>
<nav class="navbar navbar-dark bg-dark p-3">
    <a class="navbar-brand" href="#">Chương trình giảng dạy</a>
    <div>
        <button class="btn btn-outline-light">Trình tải lên hàng loạt</button>
        <a href="/instructor/course/" class="btn btn-light ms-2">
            <i class="fas fa-times"></i>
        </a>
    </div>
</nav>

<div class="container d-flex mt-4">
    <div class="sidebar" x-data>
        <h5 class="mb-3">Lên kế hoạch cho khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('target.html')">
            <label class="form-check-label">Học viên mục tiêu</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('/basic')">
            <label class="form-check-label">Cấu trúc khóa học</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('studio.html')">
            <label class="form-check-label">Thiết lập studio và tạo video thử nghiệm</label>
        </div>

        <h5 class="mt-4">Tạo nội dung của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" checked
                   onclick="updateUrlSegment('curriculum', 'curriculum')">
            <label class="form-check-label">Chương trình giảng dạy</label>
        </div>

        <h5 class="mt-4">Xuất bản khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="updateUrlSegment('curriculum', 'basics')">
            <label class="form-check-label">Trang tổng quan khóa học</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="updateUrlSegment('curriculum', 'price')">
            <label class="form-check-label">Định giá</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('/discount')">
            <label class="form-check-label">Khuyến mại</label>
        </div>

        <div class="course-status mt-4">
            Trạng thái khóa học: <span class="status-badge" x-text="$store.curriculum.course.status"></span>
        </div>

        <!-- Ẩn nút "Gửi đi để xem xét" nếu trạng thái là PUBLIC -->
        <button class="btn btn-primary w-100 mt-3"
                x-show="$store.curriculum.course.status && $store.curriculum.course.status !== 'PUBLIC' && $store.curriculum.course.status !== 'IN_REVIEW'"
                @click="$store.curriculum.sendAdminReview()">
            Gửi đi để xem xét
        </button>

    </div>
    <div class="content ms-4" x-data>
        <h2>Chương trình giảng dạy</h2>


        <template x-for="section in $store.curriculum.sections" :key="section.id">
            <div class="card p-3 mt-3">
                <!-- Hiển thị tiêu đề -->
                <div>
                    <!-- Tiêu đề hiển thị khi chưa chỉnh sửa -->
                    <h5
                            class="fw-bold"
                            @mouseover="$store.curriculum.hoveredSection = section.id"
                            @mouseleave="$store.curriculum.hoveredSection = null"
                    >
                        <span x-text="section.title"></span>
                        <button class="btn-icon"
                                x-show="$store.curriculum.hoveredSection === section.id && $store.curriculum.canEdit"
                                @click="
                $store.curriculum.editingSection = section.id;
                $store.curriculum.editedTitle = section.title;
                $store.curriculum.editedTarget = section.target;
            ">
                            <i class="fa-solid fa-pen"></i>
                        </button>

                        <button class="btn-icon"
                                x-show="$store.curriculum.hoveredSection === section.id && $store.curriculum.canEdit"
                                @click="
                            $store.curriculum.deleteSection(section.id)
                        ">
                            <i class="fa-solid fa-trash"></i>

                        </button>
                    </h5>


                    <!-- Form chỉnh sửa tiêu đề -->
                    <div x-show="$store.curriculum.editingSection === section.id" class="mt-2">
                        <!-- Tiêu đề -->
                        <label class="fw-bold mb-1">Tiêu đề</label>
                        <input type="text" class="form-control border-primary"
                               x-model="$store.curriculum.editedTitle"
                               maxlength="80">
                        <small class="text-muted"
                               x-text="80 - ($store.curriculum.editedTitle?.length ?? 0) + ' ký tự còn lại'"></small>
                        <br/>
                        <!-- Mô tả mục tiêu học tập -->
                        <label class="fw-bold mt-3 mb-1">Học viên có thể làm những gì khi phần này kết thúc?</label>
                        <textarea rows="4" class="form-control border-primary"
                                  x-model="$store.curriculum.editedTarget"
                                  maxlength="200"></textarea>
                        <small class="text-muted"
                               x-text="200 - ($store.curriculum.editedTarget?.length ?? 0) + ' ký tự còn lại'"></small>
                        <!-- Nút Hủy và Lưu -->
                        <div class="d-flex justify-content-end mt-2">
                            <button class="btn btn-light me-2" @click="$store.curriculum.editingSection = null">Hủy
                            </button>
                            <button class="btn btn-primary" @click="$store.curriculum.saveEditedSection(section.id)">Lưu
                                phần
                            </button>
                        </div>
                    </div>
                </div>


                <!-- Danh sách bài giảng -->
                <ul class="list-group mt-2" x-show="section.lessons.length > 0">
                    <template x-for="lesson in section.lessons" :key="lesson.id">
                        <li class="list-group-item" class="list-group-item"
                            @mouseover="$store.curriculum.hoveredLesson = lesson.id"
                            @mouseleave="$store.curriculum.hoveredLesson = null">
                            <div class="d-flex justify-content-between align-items-center">
                                <%--                                <span x-text="lesson.title"></span>--%>

                                <div x-show="$store.curriculum.editingLesson !== lesson.id">
                                    <template x-if="lesson.videoUrl && !lesson.article">
                                        <i class="fa-solid fa-video me-2 lesson-icon video"></i>
                                    </template>
                                    <template x-if="lesson.article && !lesson.videoUrl">
                                        <i class="fa-solid fa-newspaper me-2 lesson-icon article"></i>
                                    </template>
                                    <template x-if="lesson.videoUrl && lesson.article">
                                        <i class="fa-solid fa-layer-group me-2 lesson-icon both"></i>
                                    </template>
                                    <template x-if="!lesson.videoUrl && !lesson.article">
                                        <i class="fa-solid fa-file me-2 lesson-icon none"></i>
                                    </template>
                                    <span x-text="lesson.title"></span>
                                    <button class="btn-icon"
                                            x-show="$store.curriculum.hoveredLesson === lesson.id && $store.curriculum.canEdit"
                                            @click="
                            $store.curriculum.editingLesson = lesson.id;
                            $store.curriculum.editedLessonTitle = lesson.title;
                        ">
                                        <i class="fa-solid fa-pen"></i>

                                    </button>

                                    <button class="btn-icon"
                                            x-show="$store.curriculum.hoveredLesson === lesson.id && $store.curriculum.canEdit"
                                            @click="
                            $store.curriculum.deleteLesson(lesson.id)
                        ">
                                        <i class="fa-solid fa-trash"></i>

                                    </button>

                                </div>

                                <!-- Form chỉnh sửa tiêu đề bài học -->
                                <div x-show="$store.curriculum.editingLesson === lesson.id" class="mt-2"
                                     style="width: 100%">
                                    <label class="fw-bold mb-1">Tiêu đề bài học</label>
                                    <input type="text" class="form-control border-primary"
                                           x-model="$store.curriculum.editedLessonTitle"
                                           maxlength="80">
                                    <small class="text-muted"
                                           x-text="80 - ($store.curriculum.editedLessonTitle?.length ?? 0) + ' ký tự còn lại'"></small>

                                    <!-- Nút Hủy và Lưu -->
                                    <div class="d-flex justify-content-end mt-2">
                                        <button class="btn btn-light me-2"
                                                @click="$store.curriculum.editingLesson = null">Hủy
                                        </button>
                                        <button class="btn btn-primary"
                                                @click="$store.curriculum.saveEditedLesson(lesson.id)">Lưu bài giảng
                                        </button>
                                    </div>
                                </div>

                                <!-- Nếu bài học chưa có video & bài viết -> Hiển thị "Chọn nội dung" -->
                                <template x-if="!lesson.videoUrl && !lesson.article">
                                    <button class="btn btn-sm btn-outline-primary"
                                            x-show="$store.curriculum.canEdit"
                                            @click="$store.curriculum.selectedLessonId = ($store.curriculum.selectedLessonId === lesson.id ? null : lesson.id)">
                                        Chọn nội dung
                                    </button>
                                </template>

                                <!-- Nếu đã có video hoặc bài viết -> Hiển thị nút thả xuống -->
                                <template x-if="lesson.videoUrl || lesson.article">
                                    <button class="btn btn-sm btn-outline-secondary"
                                            @click="$store.curriculum.selectedLessonId = ($store.curriculum.selectedLessonId === lesson.id ? null : lesson.id)">
                                        <span x-text="$store.curriculum.selectedLessonId === lesson.id ? '▲' : '▼'"></span>
                                    </button>
                                </template>
                            </div>

                            <!-- Nếu đã có video hoặc bài viết -> Hiển thị nội dung -->
                            <div x-show="$store.curriculum.selectedLessonId === lesson.id && (lesson.videoUrl || lesson.article)"
                                 class="mt-2 p-3 border rounded bg-light">
                                <button class="btn-close float-end"
                                        @click="$store.curriculum.selectedLessonId = null"></button>

                                <!-- Nếu có videoUrl -> Hiển thị ảnh thumbnail -->
                                <template x-if="lesson.videoUrl">
                                    <div class="mt-2">
                                        <video width="100%" controls preload="none">
                                            <source :src="lesson.videoUrl" type="video/mp4">
                                            Trình duyệt của bạn không hỗ trợ video.
                                        </video>
                                        <!-- Nút Xóa Video -->
                                        <button
                                                x-show="$store.curriculum.canEdit"
                                                class="delete-video-btn mt-2"
                                                @click="$store.curriculum.deleteVideo(lesson.id)"
                                        >
                                            Xóa Video
                                        </button>
                                    </div>
                                </template>

                                <template x-if="!lesson.article">
                                    <div class="mt-2">
                                        <!-- Nút Thêm Bài Viết -->
                                        <button class="add-resource-btn mt-2"
                                                @click="$store.curriculum.selectContentType(lesson.id, 'article')">
                                            Thêm Bài Viết
                                        </button>
                                    </div>
                                </template>


                                <!-- Nếu có bài viết -> Hiển thị nội dung -->
                                <template x-if="lesson.article">
                                    <div class="mt-2">
                                        <h6>Bài viết:</h6>
                                        <div :id="'editor-view-' + lesson.id"></div>
                                    </div>
                                </template>
                            </div>

                            <!-- Nếu chưa có nội dung, mở form chọn loại nội dung -->
                            <div x-show="$store.curriculum.selectedLessonId === lesson.id && !lesson.videoUrl && !lesson.article"
                                 class="mt-2 p-3 border rounded bg-light">
                                <button class="btn-close float-end"
                                        @click="$store.curriculum.selectedLessonId = null"></button>
                                <h6>Chọn loại nội dung</h6>
                                <p>Chọn loại nội dung chính. Có thể thêm file và đường liên kết dưới dạng tài
                                    nguyên.</p>
                                <div class="d-flex gap-2">
                                    <button class="btn btn-outline-secondary"
                                            @click="$store.curriculum.selectContentType(lesson.id, 'video')">
                                        Video
                                    </button>
                                    <button class="btn btn-outline-secondary"
                                            @click="$store.curriculum.selectContentType(lesson.id, 'article')">
                                        Bài viết
                                    </button>
                                </div>
                            </div>

                            <!-- Form tải video -->
                            <div x-show="$store.curriculum.selectedContentType[lesson.id] === 'video'"
                                 class="mt-3 p-3 border rounded bg-light">
                                <button class="btn-close float-end"
                                        @click="$store.curriculum.selectedContentType[lesson.id] = null"></button>
                                <h6>Tải Video lên</h6>
                                <input type="file" accept="video/*"
                                       @change="$store.curriculum.uploadVideo(lesson.id, $event)">
                                <p class="text-muted">Lưu ý: File phải có độ phân giải ít nhất 360p và nhỏ hơn 4GB.</p>
                                <div x-show="$store.curriculum.uploading" class="progress mt-2">
                                    <div class="progress-bar"
                                         role="progressbar"
                                         :style="'width: ' + $store.curriculum.progress + '%'"
                                         x-text="$store.curriculum.progress + '%'">
                                    </div>
                                </div>
                            </div>

                            <!-- Form nhập bài viết -->
                            <div x-show="$store.curriculum.selectedContentType[lesson.id] === 'article'"
                                 class="mt-3 p-3 border rounded bg-light">
                                <button class="btn-close float-end"
                                        @click="$store.curriculum.selectedContentType[lesson.id] = null"></button>
                                <h6>Nhập nội dung bài viết</h6>
                                <!-- CKEditor container -->
                                <div :id="'editor-' + lesson.id" class="editor-container"></div>
                                <button class="btn btn-primary mt-2"
                                        @click="$store.curriculum.saveArticle(lesson.id)">
                                    Lưu bài viết
                                </button>
                            </div>

                        </li>
                    </template>
                </ul>

                <!-- Nút Thêm bài học -->
                <button class="btn btn-sm btn-outline-primary mt-2"
                        x-show="$store.curriculum.canEdit"
                        style="width: 120px; font-size: 12px; padding: 5px 8px;"
                        @click="$store.curriculum.toggleLessonForm(section.id)">
                    + Thêm bài học
                </button>

                <!-- Form thêm bài học -->
                <div x-show="$store.curriculum.showFormLesson[section.id]" class="mt-3 border p-3 rounded"
                     @click.outside="$store.curriculum.showFormLesson[section.id] = false"
                     @click.stop>
                    <button class="btn-close float-end"
                            @click="$store.curriculum.showFormLesson[section.id] = false"></button>

                    <label class="form-label">Tiêu đề bài học:</label>
                    <input type="text" class="form-control" placeholder="Nhập tiêu đề bài học (tối đa 80 ký tự)"
                           x-model="$store.curriculum.newLessonTitle[section.id]" maxlength="80" @click.stop>
                    <small class="text-muted"
                           x-text="80 - ($store.curriculum.newLessonTitle[section.id]?.length || 0) + ' ký tự còn lại'"></small>

                    <div></div>
                    <button class="btn btn-primary mt-3"
                            @click="$store.curriculum.addLesson(section.id)">
                        Thêm bài học
                    </button>
                </div>


            </div>
        </template>

        <!-- Nút thêm phần mới -->
        <button x-show="$store.curriculum.canEdit"
                class="btn btn-outline-primary mt-3"
                @click="$store.curriculum.showFormSection = !$store.curriculum.showFormSection">
            + Thêm phần mới
        </button>

        <!-- Form thêm phần mới -->
        <div x-show="$store.curriculum.showFormSection" class="mt-3 border p-3 rounded"
             @click.outside="$store.curriculum.showFormSection = false" @click.stop>
            <button class="btn-close float-end" @click="$store.curriculum.showFormSection = false"></button>
            <label class="form-label">Phần mới:</label>
            <input type="text" class="form-control" placeholder="Nhập tiêu đề (tối đa 80 ký tự)"
                   x-model="$store.curriculum.newSectionTitle" maxlength="80">
            <small class="text-muted" x-text="80 - $store.curriculum.newSectionTitle.length + ' ký tự còn lại'"></small>
            <div></div>
            <label class="form-label mt-2">Mục tiêu:</label>
            <textarea rows="5" type="text" class="form-control" placeholder="Nhập mục tiêu học tập (tối đa 200 ký tự)"
                      x-model="$store.curriculum.newSectionGoal" maxlength="200"></textarea>
            <small class="text-muted" x-text="200 - $store.curriculum.newSectionGoal.length + ' ký tự còn lại'"></small>
            <div></div>
            <button class="btn btn-primary mt-3" @click="$store.curriculum.addSection()">Thêm phần</button>
        </div>


    </div>

</div>
<script type="module">

    import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';

    document.addEventListener('DOMContentLoaded', function () {
        const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
        if (!userCurrent) {
            window.location.replace('/403');
        } else {
            const user = JSON.parse(userCurrent);
            const isInstructor = user?.roles?.includes('INSTRUCTOR');

            if (!isInstructor) {
                window.location.replace('/403');
            }
        }
        const pathSegments = window.location.pathname.split('/');
        const curriculumIndex = pathSegments.indexOf("curriculum"); // Tìm "curriculum"
        let courseId = null;
        if (curriculumIndex !== -1 && curriculumIndex + 1 < pathSegments.length) {
            courseId = pathSegments[curriculumIndex + 1]; // Lấy phần tử sau "curriculum"
            console.log("Course ID:", courseId);
        } else {
            console.error("Không tìm thấy courseId trong URL");
        }
    });
</script>
</body>
</html>