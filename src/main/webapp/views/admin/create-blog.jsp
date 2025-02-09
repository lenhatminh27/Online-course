<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Blog</title>
    <!-- CKEditor CDN -->
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <title>DASHMIN - Bootstrap Admin Template</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">

    <!-- Favicon -->
    <link href="img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="../../assets/admin/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="../../assets/admin/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet"/>

    <!-- Customized Bootstrap Stylesheet -->
    <link href="../../assets/admin/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="../../assets/admin/css/style.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>

        {
            margin-top: 20px
        ;
        }
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f9;
        }

        .container {
            max-width: 600px;
            margin: auto;
            background: white;
            margin-top: 20px;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #007BFF;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }

        input, textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #f9f9f9;
        }

        .ck-editor__editable {
            min-height: 300px !important;
        }

        input:focus, textarea:focus {
            border-color: #007BFF;
            outline: none;
            background-color: #fff;
        }

        button {
            background-color: #007BFF;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }

        button:hover {
            background-color: #0056b3;
        }

        .form-section {
            margin-bottom: 20px;
        }

        #errorMessage {
            color: red;
            margin-bottom: 15px;
        }

        #successMessage {
            color: green;
            margin-bottom: 15px;
        }
    </style>

</head>

<body>

<!-- Sidebar Start -->
<%@include file="../../common/admin/menu.jsp" %>
<!-- Sidebar End -->


<!-- Content Start -->
<div class="content">
    <%@include file="../../common/admin/header.jsp" %>

    <div class="container">
        <h2>Tạo Blog Mới</h2>
        <div id="errorMessage"></div>
        <div id="successMessage"></div>
        <form id="createBlogForm">
            <div class="form-section">
                <label for="title">Tiêu đề</label>
                <input type="text" id="title" name="title" placeholder="Nhập tiêu đề blog" required>
            </div>

            <div class="form-section">
                <label for="content">Nội dung</label>
                <textarea id="content" name="content" placeholder="Nhập nội dung blog" style="display: none;"></textarea>
            </div>

            <div class="form-section">
                <label for="tags">Thẻ (cách nhau bằng dấu phẩy)</label>
                <input type="text" id="tags" name="tags" placeholder="ví dụ: Java, Spring, Hibernate" required>
            </div>

            <button type="submit">Gửi</button>
        </form>
    </div>

    <!-- Footer Start -->
    <%@include file="../../common/admin/footer.jsp" %>
    <!-- Footer End -->
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

    document.getElementById('createBlogForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');

        errorMessage.textContent = '';
        successMessage.textContent = '';

        // Get form data
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

                // Clear form fields after success
                document.getElementById('createBlogForm').reset();
                editorInstance.setData(''); // Clear CKEditor content
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
                } else {
                    errorMessage.textContent = error.message || 'A network error occurred.';
                    console.error('Error:', error);
                }
            });
    });
</script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="../../assets/admin/lib/chart/chart.min.js"></script>
<script src="../../assets/admin/lib/easing/easing.min.js"></script>
<script src="../../assets/admin/lib/waypoints/waypoints.min.js"></script>
<script src="../../assets/admin/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="../../assets/admin/lib/tempusdominus/js/moment.min.js"></script>
<script src="../../assets/admin/lib/tempusdominus/js/moment-timezone.min.js"></script>
<script src="../../assets/admin/lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

<!-- Template Javascript -->
<script src="../../assets/admin/js/main.js"></script>
</body>

</html>
