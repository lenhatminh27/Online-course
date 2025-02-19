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
        <a href="/instructor/create-course">
            Tạo khóa học
        </a>
    </div>

</div>

</body>
</html>