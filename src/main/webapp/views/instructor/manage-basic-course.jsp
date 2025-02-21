<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang tổng quan khóa học</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
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
        .content {
            flex: 1;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<nav class="navbar navbar-dark bg-dark p-3">
    <a class="navbar-brand" href="#">Khóa học lập trình Java 3</a>
    <button class="btn btn-outline-light">Xem trước</button>
</nav>
<div class="container d-flex mt-4">
    <div class="sidebar">
        <h5 class="mb-3">Lên kế hoạch cho khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('target.html')">
            <label class="form-check-label">Học viên mục tiêu</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" checked onclick="redirectToPage('/basic')">
            <label class="form-check-label">Cấu trúc khóa học</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('studio.html')">
            <label class="form-check-label">Thiết lập studio và tạo video thử nghiệm</label>
        </div>

        <h5 class="mt-4">Tạo nội dung của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('filming.html')">
            <label class="form-check-label">Quay phim & chỉnh sửa</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('/curriculum')">
            <label class="form-check-label">Chương trình giảng dạy</label>
        </div>

        <h5 class="mt-4">Xuất bản khóa học của bạn</h5>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" onclick="redirectToPage('promotion.html')">
            <label class="form-check-label">Khuyến mại</label>
        </div>

        <button class="btn btn-primary w-100 mt-3" onclick="redirectToPage('review.html')">Gửi đi để xem xét</button>
    </div>
    <div class="content ms-4">
        <h2>Trang tổng quan khóa học</h2>
        <p>Trang tổng quan khóa học của bạn rất quan trọng đối với thành công trên Udemy.</p>
        <div class="mb-3">
            <label class="form-label">Tiêu đề khóa học</label>
            <input type="text" class="form-control" value="Khóa học lập trình Java 3">
        </div>
        <div class="mb-3">
            <label class="form-label">Phụ đề khóa học</label>
            <input type="text" class="form-control" placeholder="Chèn phụ đề khóa học">
        </div>
        <div class="mb-3">
            <label class="form-label">Mô tả khóa học</label>
            <textarea class="form-control" rows="4" placeholder="Chèn mô tả khóa học"></textarea>
        </div>
        <button class="btn btn-primary">Lưu</button>
    </div>
</div>
</body>
<script>
    function redirectToPage(path) {
        let baseUrl = window.location.origin + window.location.pathname.split('/').slice(0, -1).join('/');
        window.location.href = baseUrl + path;
    }
</script>
</html>
