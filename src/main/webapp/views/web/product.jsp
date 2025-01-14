<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container mt-5">
    <h2>Product Form</h2>
    <form id="productForm">
        <!-- Product ID -->
        <div class="mb-3">
            <label for="productId" class="form-label">Product ID</label>
            <input type="number" id="productId" name="id" class="form-control" placeholder="Enter product ID" required>
        </div>
        <div class="mb-3">
            <label for="productName" class="form-label">Product Name</label>
            <input type="text" id="productName" name="name" class="form-control" placeholder="Enter product name" required>
        </div>
        <div class="mb-3">
            <label for="productPrice" class="form-label">Product Price</label>
            <input type="number" id="productPrice" name="price" class="form-control" placeholder="Enter product price" step="0.01" required>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

    <!-- Area to display the result -->
    <div id="result" class="mt-4"></div>
</div>

<script>

</script>
</body>
</html>
