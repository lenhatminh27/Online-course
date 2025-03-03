<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Quan li Category</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" rel="stylesheet">
    <!-- Libraries Stylesheet -->
    <link href="../../assets/admin/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="../../assets/admin/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet"/>

    <!-- Customized Bootstrap Stylesheet -->
    <link href="../../assets/admin/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="../../assets/admin/css/style.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <%--Kiểm tra quyền truy cập--%>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        document.addEventListener('DOMContentLoaded', function () {
            const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
            if (!userCurrent) {
                window.location.replace('/403');
            } else {
                const user = JSON.parse(userCurrent);
                const isInstructor = user?.roles?.includes('ADMIN');

                if (!isInstructor) {
                    window.location.replace('/403');
                }
            }
        });
    </script>

    <%--Sử dụng Alpine.js framework--%>
    <script type="module">
        import {environment, STORAGE_KEY, avatarDefault} from '../../assets/config/env.js';
        import {apiRequestWithToken} from '../../assets/config/service.js';

        //Set state
        document.addEventListener('alpine:init', function () {
            Alpine.store('categories', {
                categories: [],
                search: '',
                totalPages: 0,
                page: 1,
                categoriesNoParent: [],
                createName: "",
                createDescription: "",
                createParentId: 0,
                updateId: 0,
                updateName: "",
                updateDescription: "",
                updateParentId: 0,

                async loadCategories() {
                    try {
                        const response = await apiRequestWithToken(
                            environment.apiUrl + '/api/category?page=' + this.page + '&search=' + this.search
                        );
                        this.categories = response.data;
                        this.totalPages = response.totalPages;
                        this.page = response.page;
                    } catch (error) {
                        console.error('Lỗi khi tải khóa học:', error);
                    }
                },

                async loadCategoriesNoParent() {
                    try {
                        const response = await apiRequestWithToken(
                            environment.apiUrl + '/api/category/no-parent'
                        );
                        this.categoriesNoParent = response;
                    } catch (error) {
                        console.error('Lỗi khi tải khóa học:', error);
                    }
                },
                nextPage() {
                    if (this.page < this.totalPages) {
                        this.page++;
                        this.loadCategories();
                    }
                },
                prevPage() {
                    if (this.page > 1) {
                        this.page--;
                        this.loadCategories();
                    }
                },
                goToPage(p) {
                    this.page = p;
                    this.loadCategories();
                },
                changeSort(newSort) {
                    this.sort = newSort;
                    this.page = 1; // Reset về trang đầu tiên
                    this.loadCategories();
                },

                async deleteCategory(categoryId) {
                    try {
                        const confirmResult = await Swal.fire({
                            title: "Bạn có chắc chắn muốn xoá thể loại này?",
                            text: "Hành động này không thể hoàn tác!",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#d33",
                            cancelButtonColor: "#3085d6",
                            confirmButtonText: "Xoá",
                            cancelButtonText: "Huỷ"
                        });

                        if (confirmResult.isConfirmed) {
                            const response = await apiRequestWithToken(environment.apiUrl + "/api/category", {
                                method: 'DELETE',
                                headers: {'Content-Type': 'application/json'},
                                body: JSON.stringify({
                                    categoryId: categoryId
                                })
                            });

                            if (response) {
                                await this.loadCategories();
                                Swal.fire({
                                    title: "Xoá thể loại thành công!",
                                    icon: "success",
                                    confirmButtonText: "OK"
                                });
                            } else {
                                await this.loadCategories();
                                Swal.fire({
                                    icon: "error",
                                    title: "Không thể xoá thể loại vì đang được sử dụng trong khoá học",
                                    text: errorMess,
                                });
                            }
                        }
                    } catch (error) {
                        console.error('Lỗi khi xoá thể loại:', error);
                        let errorMessage = error.response?.data?.message || error.message || 'A network error occurred.';
                        Swal.fire({
                            icon: "error",
                            title: "Xoá thể loại thất bại",
                            text: errorMessage
                        });
                    }
                },


                async createCategory() {
                    if (!this.createName.trim()) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi!",
                            text: "Tên thể loại là bắt buộc và không được để trống.",
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + "/api/category", {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({
                                name: this.createName,
                                description: this.createDescription,
                                parentCategoryId: this.createParentId
                            })
                        });
                        if (response) {
                            await this.loadCategories();
                            Swal.fire({
                                title: "Tạo thể loại thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                            this.createName = "";
                            this.createDescription = "";
                            this.createParentId = "";
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
                                title: "Tạo thể loại thất bại",
                                text: errorMess,
                            });
                        } else {
                            let errorMessage = error.message || 'A network error occurred.';
                            console.error('Error:', error);
                            Swal.fire({
                                icon: "error",
                                title: "Tạo thể loại thất bại",
                                text: errorMessage,
                            });
                        }
                    }
                },

                async updateCategory(categoryId, name, description, parentCategoryId) {
                    if (!name.trim()) {
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi!",
                            text: "Tên thể loại là bắt buộc và không được để trống.",
                        });
                        return;
                    }
                    try {
                        const response = await apiRequestWithToken(environment.apiUrl + "/api/category", {
                            method: 'PUT',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({
                                categoryId: categoryId,
                                name: name,
                                description: description,
                                parentCategoryId: parentCategoryId
                            })
                        });

                        if (response) {
                            await this.loadCategories();
                            Swal.fire({
                                title: "Cập nhật thể loại thành công!",
                                icon: "success",
                                confirmButtonText: "OK"
                            });
                        }
                    } catch (error) {
                        console.error('Lỗi khi cập nhật thể loại:', error);
                        let errorMessage = error.response?.data?.message || error.message || 'A network error occurred.';
                        Swal.fire({
                            icon: "error",
                            title: "Cập nhật thể loại thất bại",
                            text: errorMessage
                        });
                    }
                }
            });
            Alpine.store('categories').loadCategories();
            Alpine.store('categories').loadCategoriesNoParent();
        });

    </script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
</head>
<body>
<div class="container-xxl position-relative bg-white d-flex p-0">
    <!-- Sidebar Start -->
    <%@include file="../../common/admin/menu.jsp" %>
    <!-- Sidebar End -->

    <!-- Nội dung Bắt đầu -->
    <div class="content">
        <%@include file="../../common/admin/header.jsp" %>

        <div class="container mt-4">
            <h2>Quản lý thể loại</h2>
            <div id="alert-box" class="alert d-none"></div>

            <!-- Search and Add Category Section -->
            <div class="d-flex justify-content-between align-items-center mb-3" x-data>
                <div class="d-flex">
                    <input type="text" class="form-control me-2 search-input" id="search-input"
                           placeholder="Tìm kiếm thể loại"
                           x-model="$store.categories.search"
                           @input.debounce.500ms="$store.categories.loadCategories()"
                    >
                </div>
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#categoryModal">
                    <i class="fas fa-plus"></i> Tạo thể loại
                </button>


                <!-- Create Category Modal -->
                <div class="modal fade" id="categoryModal" tabindex="-1" aria-labelledby="categoryModalLabel"
                     aria-hidden="true" x-data>
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="categoryModalLabel">Tạo thể loại mới</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="category-name" class="form-label">Tên thể loại<span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="category-name"
                                           x-model="$store.categories.createName"
                                           placeholder="Tên thể loại ngắn gọn dễ hiểu và không chứa kí tự đặc biệt"
                                           required>
                                </div>
                                <div class="mb-3">
                                    <label for="category-description" class="form-label">Miêu tả</label>
                                    <textarea class="form-control" id="category-description"
                                              x-model="$store.categories.createDescription"
                                              placeholder="Không bắt buộc"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label for="category-parent" class="form-label">Thể loại cha <span
                                            class="text-muted"
                                            style="font-size: 0.9em;">(không bắt buộc)</span></label>
                                    <select class="form-select" id="category-parent"
                                            x-model="$store.categories.createParentId">
                                        <option value="" @click="$store.categories.createParentId = null">-- Chọn thể
                                            loại cha --
                                        </option>
                                        <template x-for="parent in $store.categories.categoriesNoParent"
                                                  :key="parent.id">
                                            <option :value="parent.id" x-text="parent.name"></option>
                                        </template>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                <button type="button" class="btn btn-primary"
                                        @click="$store.categories.createCategory()">Tạo
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Update Category Modal -->
        <div class="modal fade" id="updateCategoryModal" tabindex="-1" aria-labelledby="updateCategoryModalLabel"
             aria-hidden="true" x-data>
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updateCategoryModalLabel">Cập nhật thể loại</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" x-model="$store.categories.updateId">

                        <div class="mb-3">
                            <label for="update-category-name" class="form-label">Tên thể loại<span
                                    class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="update-category-name"
                                   x-model="$store.categories.updateName" placeholder="Tên thể loại ngắn gọn dễ hiểu"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label for="update-category-description" class="form-label">Miêu tả</label>
                            <textarea class="form-control" id="update-category-description"
                                      x-model="$store.categories.updateDescription"
                                      placeholder="Không bắt buộc"></textarea>
                        </div>

                        <div class="mb-3">
                            <label for="update-category-parent" class="form-label">Thể loại cha <span class="text-muted"
                                                                                                      style="font-size: 0.9em;">(không bắt buộc)</span></label>
                            <select class="form-select" id="update-category-parent"
                                    x-model="$store.categories.updateParentId">
                                <option value="" @click="$store.categories.updateParentId = null">-- Chọn thể loại cha
                                    --
                                </option>
                                <template x-for="parent in $store.categories.categoriesNoParent"
                                          :key="parent.id">
                                    <option :value="parent.id" x-text="parent.name"></option>
                                </template>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="button" class="btn btn-primary"
                                @click="$store.categories.updateCategory($store.categories.updateId, $store.categories.updateName, $store.categories.updateDescription, $store.categories.updateParentId)">
                            Cập nhật
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Category Table -->
        <table class="table table-striped table-bordered" x-data>
            <thead style="background-color: #077097; color: white;">
            <tr>
                <th>ID</th>
                <th>Tên thể loại</th>
                <th>Thể loại cha</th>
                <th>Miêu tả</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody id="categoryTableBody">
            <template x-for="category in $store.categories.categories" :key="category.id">
                <tr>
                    <td x-text="category.id"></td>
                    <td x-text="category.name"></td>
                    <td x-text="category.parentName || 'Không có'"></td>
                    <td x-text="category.description"></td>
                    <td>
                        <button data-bs-toggle="modal" data-bs-target="#updateCategoryModal"
                                @click="$store.categories.updateId = category.id;
                                        $store.categories.updateName = category.name;
                                        $store.categories.updateDescription = category.description;
                                        $store.categories.updateParentId = category.parentId;">
                            Sửa
                        </button>
                        <button @click="$store.categories.deleteCategory(category.id)">Xoá</button>
                    </td>
                </tr>
            </template>
            </tbody>
        </table>

        <div class="d-flex justify-content-center mt-3" x-data>
            <template x-for="p in $store.categories.totalPages" :key="p">
                <button class="btn btn-sm mx-1" :class="p === $store.categories.page ? 'btn-primary' : 'btn-secondary'"
                        @click="$store.categories.goToPage(p)">
                    <span x-text="p"></span>
                </button>
            </template>
        </div>

    </div>


    <!-- Footer Bắt đầu -->
    <%@include file="../../common/admin/footer.jsp" %>
    <!-- Footer Kết thúc -->
</div>
<!-- Nội dung Kết thúc -->


<!-- Back to Top -->
<a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
</div>
</body>
</html>