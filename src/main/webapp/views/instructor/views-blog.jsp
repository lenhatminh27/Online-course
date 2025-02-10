<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Instructor</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body, h1, h2, h3, h4, h5 {
            font-family: "Poppins", sans-serif
        }

        body {
            font-size: 16px;
        }
        .container-body{
            margin-left: 350px !important;
        }

        /* Container chính */
        .search-bar-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 10px;
            padding: 10px;
            flex-wrap: wrap;
        }

        /* Form tìm kiếm */
        .search-form {
            display: flex;
            align-items: center;
            flex-grow: 1;
            max-width: 600px;
        }

        /* Ô input lớn */




        .search-input:focus {
            border-color: #d9534f;
            box-shadow: 0 0 5px rgba(217, 83, 79, 0.5);
        }

        /* Nút tìm kiếm nhỏ */
        .search-button {
            padding: 8px 15px;
            background-color: #d9534f;
            color: white;
            font-size: 14px;
            font-weight: bold;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 8px;
            transition: background 0.3s ease-in-out;
        }

        .search-button:hover {
            background-color: #c9302c;
        }

        /* Nút "Add Blog" bên phải */
        .add-blog-btn {
            background-color: #5cb85c;
            color: white;
            font-size: 16px;
            padding: 10px 15px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            transition: background 0.3s ease-in-out;
            white-space: nowrap; /* Tránh xuống dòng */
        }

        .add-blog-btn:hover {
            background-color: #4cae4c;
        }

        /* Responsive cho màn hình nhỏ */
        @media (max-width: 768px) {
            .search-bar-container {
                flex-direction: column;
                align-items: stretch;
            }

            .search-form {
                width: 100%;
            }

            .add-blog-btn {
                width: 100%;
                text-align: center;
            }
        }



        /* Phần tử của mỗi trang */
        #pagination {
            margin-top: 50px;
            display: flex;
            justify-content: center;
            list-style: none;
            padding: 0;
        }

        /* Các trang */
        .page-item {
            margin: 0 5px;
        }

        /* Các nút trang */
        .page-link {
            display: block;
            padding: 10px 15px;
            color: #007bff;
            text-decoration: none;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        /* Thêm hiệu ứng hover cho các nút */
        .page-link:hover {
            background-color: #007bff;
            color: #fff;
        }

        /* Các nút trang đã chọn */
        .page-item.active .page-link {
            background-color: #007bff;
            color: #fff;
            border-color: #007bff;
        }

        /* Các nút Previous và Next */
        .page-item .page-link[aria-label="Previous"],
        .page-item .page-link[aria-label="Next"] {
            font-weight: bold;
        }

        /* Các nút Disabled (khi không thể nhấn) */
        .page-item.disabled .page-link {
            color: #ccc;
            pointer-events: none;
        }

        /* Căn chỉnh modal và form */
        .modal-dialog {
            max-width: 600px; /* Giới hạn chiều rộng modal */
        }

        .modal-content {
            border-radius: 12px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
        }

        /* Tiêu đề modal */
        .modal-header {
            background-color: gray;
            color: white;
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
        }

        .modal-title {
            font-weight: bold;
        }

        .btn-close {
            background: none;
            border: none;
            font-size: 18px;
        }

        /* Form bên trong modal */
        .form-section {
            margin-bottom: 15px;
        }

        .form-section label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .form-section input,
        .form-section textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }

        .ck-editor__editable {
            min-height: 200px !important;
        }

        /* Nút submit */
        #createBlogForm button[type="submit"] {
            width: 100%;
            background-color: #28a745;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: 0.3s;
        }

        #createBlogForm button[type="submit"]:hover {
            background-color: #218838;
        }


        /* Nút submit */
        #updateBlogForm button[type="submit"] {
            width: 100%;
            background-color: #28a745;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: 0.3s;
        }

        #updateBlogForm button[type="submit"]:hover {
            background-color: #218838;
        }


    </style>

</head>
<body>

<!-- Sidebar/menu -->
<%@include file="../../common/instructor/menu.jsp" %>

<!-- !PAGE CONTENT! -->
<div class="w3-main">

    <%@include file="../../common/instructor/header.jsp" %>
    <div class="container-body">
        <h2>Quản lý bài viết</h2>

        <!-- Search and Add Blog Buttons -->
        <div class="w3-bar search-bar-container">
            <form id="search-form" onsubmit="handleSearch2(event)" class="search-form">
                <input type="text" class="form-control search-input" id="search-input" placeholder="Tìm kiếm bài viết"
                       onfocus="this.placeholder = ''" onblur="this.placeholder = 'Tìm kiếm bài viết'">
                <select id="sort-options" class="form-control sort-select">
                    <option value="newest">Mới nhất</option>
                    <option value="oldest">Cũ nhất</option>
                    <option value="views">Xem nhiều nhất</option>
                    <option value="likes">Tim nhiều nhất</option>
                </select>
                <button class="search-button" type="submit">Tìm</button>
            </form>
            <button class="add-blog-btn" data-bs-toggle="modal" data-bs-target="#blogModal">
                <i class="fas fa-plus"></i> Tạo bài viết
            </button>
        </div>


        <br>

        <!-- Blog Details Table -->
        <div class="w3-responsive">
            <table class="w3-table w3-bordered w3-striped">
                <thead>
                <tr class="w3-red">
                    <th>ID</th>
                    <th>Title</th>
                    <th>Created At</th>
                    <th>Like</th>
                    <th>Views</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="blogTableBody">
                <!-- Blog rows will be dynamically inserted here -->
                </tbody>
            </table>
        </div>

        <div id="pagination">
        </div>

        <!-- Modal Bootstrap -->
        <div class="modal fade" id="blogModal" tabindex="-1" aria-labelledby="blogModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="blogModalLabel">Tạo bài viết mới</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="createBlogForm">
                            <div id="errorMessage" style="color: red"></div>
                            <div id="successMessage" style="color: green"></div>
                            <div class="form-section">
                                <label for="title">Tiêu đề</label>
                                <input type="text" id="title" name="title" placeholder="Enter blog title" required>
                            </div>

                            <div class="form-section">
                                <label for="content">Nội dung</label>
                                <textarea id="content" name="content" placeholder="Enter blog content"
                                          style="display: none;"></textarea>
                            </div>

                            <div class="form-section">
                                <label for="tags">Tags (comma-separated)</label>
                                <input type="text" id="tags" name="tags" placeholder="e.g., Java, Spring, Hibernate" required>
                            </div>

                            <button type="submit">Tạo Blog</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="blogModalUpdate" tabindex="-1" aria-labelledby="blogModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="blogUpdateModalLabel">Cập nhật bài viết</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="updateBlogForm">
                            <div id="errorMessageUpdate" style="color: red"></div>
                            <div id="successMessageUpdate" style="color: green"></div>
                            <div class="form-section">
                                <label for="title">Tiêu đề</label>
                                <input type="text" id="titleUpdate" name="title" placeholder="Enter blog title" required>
                            </div>

                            <div class="form-section">
                                <label for="content">Nội dung</label>
                                <textarea id="contentUpdate" name="content" placeholder="Enter blog content"
                                          style="display: none;"></textarea>
                            </div>

                            <div class="form-section">
                                <label for="tags">Tags (comma-separated)</label>
                                <input type="text" id="tagsUpdate" name="tags" placeholder="e.g., Java, Spring, Hibernate" required>
                            </div>

                            <button type="submit">Cập nhật Blog</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>




<script type="module">
    import {environment} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';

    let editorInstance;

    // Initialize CKEditor for the content field
    ClassicEditor
        .create(document.querySelector('#content'))
        .then(editor => {
            editorInstance = editor;
        })
        .catch(error => {
            console.error('Error initializing CKEditor:', error);
        });



    let editorInstanceUpdate;

    ClassicEditor.create(document.querySelector('#contentUpdate'))
        .then(editor => {
            editorInstanceUpdate = editor;
        })
        .catch(error => console.error('Error initializing CKEditor:', error));

    document.addEventListener('DOMContentLoaded', function () {
        const urlParams = new URLSearchParams(window.location.search);
        const page = urlParams.has('page') ? parseInt(urlParams.get('page')) : 1;
        const searchQuery = urlParams.has('search') ? urlParams.get('search') : '';
        const tagsQuery = urlParams.has('tags') ? urlParams.get('tags') : '';
        const sort = urlParams.get('sort') || 'newest';

        function formatDate(dateString) {
            const date = new Date(dateString);
            return date.toLocaleDateString("vi-VN", {
                day: "2-digit",
                month: "2-digit",
                year: "numeric"
            });
        }

        async function deleteBlog(blogId) {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/blogs/' + blogId, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (response) {
                    Swal.fire({
                        title: "Xóa bài viết thành công!",
                        icon: "success",
                        draggable: true
                    });
                    loadBlogs(page, searchQuery, tagsQuery, sort);
                }
                else {
                    Swal.fire({
                        icon: "error",
                        title: "Xóa thất bại",
                        text: error.message || "Lỗi hệ thống!",
                    });
                }
            } catch (error) {
                console.log(error.response?.status);
                console.log(error.data);
            }
        }

        async function loadBlogs(page, searchQuery, tags, sort){
            try {
                let url = environment.apiUrl + "/api/blogs/instructor?page=" + page;
                if (searchQuery) {
                    url += '&search=' + searchQuery;
                }
                if (tags) {
                    url += '&tags=' + tags;
                }
                if (sort){
                    url += '&sort=' + sort;
                }
                const response = await apiRequestWithToken(url, {
                    method: 'GET',
                });
                loadBlogsData(response.data);
                const pagination = document.querySelector('#pagination');
                let paginationHtml = "";

                if (response.page > 1) {
                    paginationHtml += "<li class='page-item'><a href='#' class='page-link' onclick='updatePage(" + (response.page - 1) + ")'>Trước</a></li>";
                }

                for (let i = 1; i <= response.totalPages; i++) {
                    const activeClass = (i === response.page) ? 'active' : '';
                    paginationHtml += "<li class='page-item " + activeClass + "'><a href='#' class='page-link' onclick='updatePage2(" + i + ")'>" + i + "</a></li>";
                }

                if (response.page < response.totalPages) {
                    paginationHtml += "<li class='page-item'><a href='#' class='page-link' onclick='updatePage2(" + (response.page + 1) + ")'>Sau</a></li>";
                }

                pagination.innerHTML = paginationHtml;
            }catch (error){
                console.error('Error occurred:', error);
            }
        }

        window.updatePage2 = function (page) {
            const urlParams = new URLSearchParams(window.location.search);
            const searchQuery = document.getElementById('search-input').value;
            const tagsQuery = urlParams.get('tags') || '';
            const sort = urlParams.get('sort') || 'newest';
            urlParams.set('page', page);
            if (searchQuery) {
                urlParams.set('search', searchQuery);
            }
            if (tagsQuery) {
                urlParams.set('tags', tagsQuery);
            }
            if (sort) {
                urlParams.set('sort', sort);
            }
            window.history.pushState({}, '', '?' + urlParams.toString());
            loadBlogs(page, searchQuery, tagsQuery, sort);
        };

        window.handleSearch2 = function handleSearch(event) {
            event.preventDefault();
            const searchQuery = document.getElementById('search-input').value;
            const sort = document.getElementById('sort-options').value;
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('search', searchQuery);
            urlParams.set('sort', sort);
            window.history.pushState({}, '', '?' + urlParams.toString());
            loadBlogs(1, searchQuery, tagsQuery, sort);
        };




        function loadBlogsData(data) {
            const tableBody = document.getElementById('blogTableBody');
            tableBody.innerHTML = '';

            data.forEach(function (blog) {
                const row = document.createElement('tr');
                let tagsString = blog.tagResponses ? blog.tagResponses.map(tag => tag.name).join(', ') : '';
                let rowContent = "<td>" + blog.id + "</td>";
                rowContent += "<td>" + blog.title + "</td>";
                rowContent += "<td>" + formatDate(blog.createAt) + "</td>";
                rowContent += "<td>" + blog.likesCount + "</td>";
                rowContent += "<td>" + blog.viewsCount + "</td>";
                rowContent += "<td>" +
                    "<button class='w3-button w3-green edit-btn' data-bs-toggle='modal' data-bs-target='#blogModalUpdate' data-id='" + blog.id + "' data-title='" + blog.title + "' data-content='" + encodeURIComponent(blog.content) + "' data-tags='" + tagsString + "'>" +
                    "<i class='fas fa-edit'></i> Sửa</button> " +
                    "<button class='w3-button w3-red delete-btn' data-id='" + blog.id + "'>" +
                    "<i class='fas fa-trash'></i> Xóa</button>" +
                    "</td>";

                row.innerHTML = rowContent;
                tableBody.appendChild(row);
            });

            document.querySelectorAll('.edit-btn').forEach(button => {
                button.addEventListener('click', function () {
                    document.getElementById('titleUpdate').value = this.getAttribute('data-title');
                    editorInstanceUpdate.setData(decodeURIComponent(this.getAttribute('data-content')));
                    document.getElementById('tagsUpdate').value = this.getAttribute('data-tags');
                    document.getElementById('updateBlogForm').setAttribute('data-id', this.getAttribute('data-id'));
                });
            });

            document.querySelectorAll('.delete-btn').forEach(function (button) {
                button.addEventListener('click', function () {
                    const blogId = this.getAttribute('data-id');
                    Swal.fire({
                        title: "Bạn có muốn xóa blog này không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Xóa",
                        cancelButtonText: "Hủy"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            deleteBlog(blogId);
                            Swal.fire("Saved!", "", "success");
                        }
                    });
                });
            });
        }

        loadBlogs(page, searchQuery, tagsQuery, sort);

        document.getElementById('createBlogForm').addEventListener('submit', function (event) {
            event.preventDefault();
            const errorMessage = document.getElementById('errorMessage');
            const successMessage = document.getElementById('successMessage');
            errorMessage.textContent = '';
            successMessage.textContent = '';
            const title = document.getElementById('title').value.trim();
            const content = editorInstance.getData(); // Get content from CKEditor
            const tags = document.getElementById('tags').value.split(',').map(tag => tag.trim()).filter(tag => tag);

            // Validate input
            if (!title || !content || tags.length === 0) {
                errorMessage.textContent = 'All fields are required. Please fill in the Title, Content, and Tags.';
                return;
            }

            const blogData = {
                title: title,
                content: content,
                tagName: tags
            };

            apiRequestWithToken(environment.apiUrl + '/api/blogs', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(blogData)
            })
                .then(response => {
                    Swal.fire({
                        title: "Tạo bài viết thành công!",
                        icon: "success",
                        draggable: true
                    });
                    document.getElementById('createBlogForm').reset();
                    editorInstance.setData(''); // Clear CKEditor content
                    loadBlogs(page, searchQuery, tagsQuery, sort);
                })
                .catch(error => {
                    console.log(error);
                    if (error.response?.status === 400 && error.data?.error) {
                        console.error('Validation errors:', error.data.error);
                        let errorMess = "";
                        for (const x of error.data.error) {
                            errorMess += x;
                        }
                        errorMessage.textContent = errorMess;
                        Swal.fire({
                            icon: "error",
                            title: "Tạo bài viết thất bại",
                            text: errorMess,
                        });
                    } else {
                        let errorMessage = error.message || 'A network error occurred.';
                        console.error('Error:', error);
                        Swal.fire({
                            icon: "error",
                            title: "Tạo bài viết thất bại",
                            text: errorMessage,
                        });
                    }
                });
        });

        function updateBlogApi(blogId, blogData) {
            var apiUrl = environment.apiUrl + "/api/blogs/" + blogId;

            apiRequestWithToken(apiUrl, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(blogData)
            })
                .then(function (response) {
                    Swal.fire({
                        title: "Cập nhật bài viết thành công!",
                        icon: "success",
                        draggable: true
                    });
                    loadBlogs(page, searchQuery, tagsQuery, sort);
                })
                .catch(function (error) {
                    console.log(error);
                    if (error.response?.status === 400 && error.data?.error) {
                        console.error('Validation errors:', error.data.error);
                        let errorMess = "";
                        for (const x of error.data.error) {
                            errorMess += x;
                        }
                        errorMessage.textContent = errorMess;
                        Swal.fire({
                            icon: "error",
                            title: "Cập nhật bài viết thất bại",
                            text: errorMess,
                        });
                    }else {
                        let errorMessage = error.message || 'A network error occurred.';
                        console.error('Error:', error);
                        Swal.fire({
                            icon: "error",
                            title: "Cập nhật bài viết thất bại",
                            text: errorMessage,
                        });
                    }
                });
        }



        // Xử lý sự kiện submit form cập nhật
        document.getElementById('updateBlogForm').addEventListener('submit', function (event) {
            event.preventDefault();

            var blogId = this.getAttribute('data-id');
            var title = document.getElementById('titleUpdate').value.trim();
            var content = editorInstanceUpdate.getData();
            var tags = document.getElementById('tagsUpdate').value.split(',').map(function (tag) {
                return tag.trim();
            }).filter(function (tag) {
                return tag;
            });

            if (!title || !content || tags.length === 0) {
                Swal.fire({
                    icon: "warning",
                    title: "Vui lòng nhập đầy đủ thông tin",
                });
                return;
            }

            var blogData = { title: title, content: content, tagName: tags };
            updateBlogApi(blogId, blogData);
        });
    });


</script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>

</html>