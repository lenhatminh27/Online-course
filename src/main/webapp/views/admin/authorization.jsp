<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">

<head>
    <meta charset="utf-8">
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
    <link href="../../assets/admin/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />

    <!-- Customized Bootstrap Stylesheet -->
    <link href="../../assets/admin/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="../../assets/admin/css/style.css" rel="stylesheet">
</head>

<body>
<div class="container-xxl position-relative bg-white d-flex p-0">
    <!-- Spinner Start -->
    <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
        <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
    <!-- Spinner End -->


    <!-- Sidebar Start -->
    <%@include file="../../common/admin/menu.jsp"%>
    <!-- Sidebar End -->


    <!-- Nội dung Bắt đầu -->
    <div class="content">
        <%@include file="../../common/admin/header.jsp"%>

        <div class="container mt-4">
            <h3>Gán quyền cho vai trò</h3>
            <form id="roleForm" method="POST">
                <div class="row">
                    <!-- Cột Chọn Vai trò (4) -->
                    <div class="col-md-4">
                        <div class="form-group mb-4">
                            <label for="roleSelect">Chọn Vai trò</label>
                            <select class="form-control" id="roleSelect" name="role">
                                <option value="" selected disabled>Chọn một vai trò</option>
                                <!-- Các tùy chọn sẽ được điền động từ API -->
                            </select>
                        </div>
                    </div>

                    <!-- Cột Quyền (7) -->
                    <div class="col-md-7">
                        <div class="form-group" id="form-group">
                            <label for="permissions">Quyền</label><br>
                            <!-- Các nút sẽ được điền động từ API -->
                        </div>
                    </div>
                </div>

                <button type="button" id="submitBtn" class="btn btn-primary mt-3">Gửi</button>
            </form>
        </div>

        <!-- Footer Bắt đầu -->
        <%@include file="../../common/admin/footer.jsp"%>
        <!-- Footer Kết thúc -->
    </div>
    <!-- Nội dung Kết thúc -->



    <!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
</div>
<script type="module">

    import { apiRequestWithToken } from '../../assets/config/service.js';
    import { environment } from '../../assets/config/env.js';

    function getRoles() {
        apiRequestWithToken(environment.apiUrl + '/api/roles', { method: 'GET' })
            .then(roles => {
                console.log(roles);
                const roleSelect = document.getElementById('roleSelect');
                roles.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role.id;
                    option.textContent = role.name;
                    roleSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error fetching roles:', error);
            });
    }

    function getPermissions(roleId) {
        apiRequestWithToken(environment.apiUrl + `/api/permissions/` + roleId, { method: 'GET' })
            .then(permissions => {
                console.log(permissions);
                const permissionsContainer = document.querySelector('#form-group');
                permissionsContainer.innerHTML = '<label for="permissions">Permissions</label><br>';
                permissions.forEach(permission => {
                    const button = document.createElement('button');
                    button.type = 'button';
                    button.classList.add('btn', 'btn-outline-primary');
                    if (permission.isTick) {
                        button.style.backgroundColor = '#28a745';
                        button.style.color = 'white';
                    }
                    button.setAttribute('data-id', permission.id);
                    button.textContent = permission.name;
                    button.addEventListener('click', function() {
                        if (button.style.backgroundColor === 'rgb(40, 167, 69)') {
                            button.style.backgroundColor = '';
                            button.style.color = '';
                        } else {
                            button.style.backgroundColor = '#28a745';
                            button.style.color = 'white';
                        }
                    });
                    permissionsContainer.appendChild(button);
                });
            })
            .catch(error => {
                console.error('Error fetching permissions:', error);
            });
    }

    function handleRoleChange(event) {
        const roleId = event.target.value;
        if (roleId) {
            getPermissions(roleId);
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        getRoles();
        const roleSelect = document.getElementById('roleSelect');
        roleSelect.addEventListener('change', handleRoleChange);
        const form = document.getElementById('roleForm');
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            const roleId = document.getElementById('roleSelect').value;
            console.log('Form submitted with Role ID:', roleId);
        });
        const submitBtn = document.getElementById('submitBtn');
        submitBtn.addEventListener('click', function () {
            const roleId = document.getElementById('roleSelect').value;
            const selectedPermissions = [];
            const permissionButtons = document.querySelectorAll('#form-group button');
            permissionButtons.forEach(button => {
                if (button.style.backgroundColor === 'rgb(40, 167, 69)') {
                    selectedPermissions.push(button.getAttribute('data-id'));
                }
            });
            if (roleId && selectedPermissions.length > 0) {
                const requestData = {
                    permissionIds: selectedPermissions
                };
                apiRequestWithToken(environment.apiUrl + `/api/permissions/` + roleId, {
                    method: 'POST',
                    body: JSON.stringify(requestData),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(data => {
                        alert("Update permission oke");
                    })
                    .catch(error => {
                        console.error('Error submitting data:', error);
                    });
            } else {
                alert("Please select a role and permissions.");
            }
        });
    });
</script>
<!-- JavaScript Libraries -->
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