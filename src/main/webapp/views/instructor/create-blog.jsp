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

    <style>
        body, h1, h2, h3, h4, h5 {
            font-family: "Poppins", sans-serif
        }

        body {
            font-size: 16px;
        }

        .w3-half img {
            margin-bottom: -6px;
            margin-top: 16px;
            opacity: 0.8;
            cursor: pointer
        }

        .w3-half img:hover {
            opacity: 1
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

        .ck-editor__editable {
            min-height: 300px !important;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #f44336;
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

        input:focus, textarea:focus {
            border-color: #007BFF;
            outline: none;
            background-color: #fff;
        }

        button {
            background-color: #f44336;
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

<!-- Sidebar/menu -->
<%@include file="../../common/instructor/menu.jsp" %>

<!-- !PAGE CONTENT! -->
<div class="w3-main">

    <%@include file="../../common/instructor/header.jsp" %>
    <div class="container">
        <h2>Create New Blog</h2>
        <div id="errorMessage"></div>
        <div id="successMessage"></div>
        <form id="createBlogForm">
            <div class="form-section">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" placeholder="Enter blog title" required>
            </div>

            <div class="form-section">
                <label for="content">Content</label>
                <textarea id="content" name="content" placeholder="Enter blog content"
                          style="display: none;"></textarea>
            </div>

            <div class="form-section">
                <label for="tags">Tags (comma-separated)</label>
                <input type="text" id="tags" name="tags" placeholder="e.g., Java, Spring, Hibernate" required>
            </div>

            <button type="submit">Submit</button>
        </form>


    </div>

</div>


<script>
    // Script to open and close sidebar
    function w3_open() {
        document.getElementById("mySidebar").style.display = "block";
        document.getElementById("myOverlay").style.display = "block";
    }

    function w3_close() {
        document.getElementById("mySidebar").style.display = "none";
        document.getElementById("myOverlay").style.display = "none";
    }

    // Modal Image Gallery
    function onClick(element) {
        document.getElementById("img01").src = element.src;
        document.getElementById("modal01").style.display = "block";
        var captionText = document.getElementById("caption");
        captionText.innerHTML = element.alt;
    }
</script>

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
                console.log('Blog created successfully', response);
                successMessage.textContent = 'Blog created successfully';

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

</body>
</html>