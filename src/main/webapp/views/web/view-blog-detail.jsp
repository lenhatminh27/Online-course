<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Courses | Education</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="assets/img/favicon.ico">

    <!-- CSS here -->
    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="../../assets/css/slicknav.css">
    <link rel="stylesheet" href="../../assets/css/animate.min.css">
    <link rel="stylesheet" href="../../assets/css/hamburgers.min.css">
    <link rel="stylesheet" href="../../assets/css/magnific-popup.css">
    <link rel="stylesheet" href="../../assets/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="../../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../../assets/css/themify-icons.css">
    <link rel="stylesheet" href="../../assets/css/slick.css">
    <link rel="stylesheet" href="../../assets/css/nice-select.css">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <link rel="stylesheet" href="../../assets/css/responsive.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Mulish', 'Josefin Sans', sans-serif, 'Arial', 'Helvetica', 'Times New Roman', 'serif' !important;
        }

        .blog-info-link li {
            font-size: 15px; /* Tăng kích thước chữ */
            font-weight: bold; /* Làm chữ đậm */
            padding: 12px 18px; /* Tăng khoảng cách cho từng mục */
            border-radius: 8px; /* Bo góc nhẹ */
            background-color: #f8f9fa; /* Màu nền nhẹ */
            display: inline-block;
            cursor: pointer;
            transition: all 0.3s ease-in-out;
        }

        /* Hiệu ứng khi hover */
        .blog-info-link li:hover {
            background-color: #e9ecef; /* Màu nền khi hover */
            transform: scale(1.05); /* Hiệu ứng phóng to nhẹ */
        }

        /* Tăng kích thước icon */
        .blog-info-link li i {
            font-size: 24px;
            margin-right: 8px;
        }


        .like-info {
            cursor: pointer;
            transition: transform 0.2s ease-in-out;
        }

        .like-info:hover {
            transform: scale(1.2); /* Slight zoom effect */
        }

        .like-info i {
            transition: color 0.2s ease-in-out;
        }

        .like-info:hover i {
            color: red; /* Changes color on hover */
        }

        .my-btn {
            border: none;
            background-color: transparent;
        }
        .my-btn i{
            color: black !important;
        }


    </style>
</head>
<body>
<!--? Preloader Start -->
<div id="preloader-active">
    <div class="preloader d-flex align-items-center justify-content-center">
        <div class="preloader-inner position-relative">
            <div class="preloader-circle"></div>
            <div class="preloader-img pere-text">
                <img src="../../assets/img/logo/loder.png" alt="">
            </div>
        </div>
    </div>
</div>
<!-- Preloader Start -->
<!-- Header Start -->
<%@include file="../../common/web/header.jsp" %>
<!-- Header End -->
<main>
    <!--? slider Area Start-->
    <!--? Khu vực Slider Bắt đầu-->
    <section class="slider-area slider-area2">
        <div class="slider-active">
            <!-- Single Slider -->
            <div class="single-slider slider-height2">
                <div class="container">
                    <div class="row">
                        <div class="col-xl-8 col-lg-11 col-md-12">
                            <div class="hero__caption hero__caption2">
                                <h1 data-animation="bounceIn" data-delay="0.2s">Thông tin công ty</h1>
                                <!-- breadcrumb Bắt đầu-->
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="index.html">Trang chủ</a></li>
                                        <li class="breadcrumb-item"><a href="#">Blog</a></li>
                                    </ol>
                                </nav>
                                <!-- breadcrumb Kết thúc -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--? Khu vực Slider Kết thúc-->

    <!--? Blog Area Start-->
    <section class="blog_area single-post-area section-padding">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 posts-list">
                    <div class="single-post">
                        <div class="feature-img">
                            <img class="img-fluid" src="assets/img/blog/single_blog_1.png" alt="">
                        </div>
                        <div class="blog_details" id="blog_details">
                        </div>
                    </div>
                    <div class="navigation-top">
                        <div class="d-sm-flex justify-content-between text-center">
                            <p class="like-info" id="isLike"></p>
                            <div class="col-sm-4 text-center my-2 my-sm-0">
                                <!-- <p class="comment-count"><span class="align-middle"><i class="fa fa-comment"></i></span> 06 Comments</p> -->
                            </div>
                            <ul class="social-icons">
                                <li><a href="https://www.facebook.com/sai4ull"><i class="fab fa-facebook-f"></i></a>
                                </li>
                                <li><a href="#"><i class="fab fa-twitter"></i></a></li>
                                <li><a href="#"><i class="fab fa-dribbble"></i></a></li>
                                <li><a href="#"><i class="fab fa-behance"></i></a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="comment-form">
                        <h4>Bình luận</h4>
                        <form class="form-contact comment_form" action="#" id="commentForm">
                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group">
                     <textarea class="form-control form-control-sm w-100" name="comment" id="comment" cols="16" rows="6"
                               placeholder="Nhập nội dung bình luận"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="button button-contactForm btn_1 boxed-btn">Gửi bình luận
                                </button>
                            </div>
                        </form>
                    </div>
                    <div class="comments-area" id="comments-area">
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="blog_right_sidebar">

                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Blog Area End -->
</main>
<%@include file="../../common/web/footer.jsp" %>

<!-- Scroll Up -->
<div id="back-top">
    <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>
<!-- JS here -->
<script type="module">
    import {environment, avatarDefault, STORAGE_KEY} from "../../assets/config/env.js";
    import {apiRequestWithToken} from "../../assets/config/service.js";

    document.addEventListener('DOMContentLoaded', async function () {
        const path = window.location.pathname;
        const slug = path.split('/').pop();

        async function loadDetailBlog(slug) {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/blog-post/' + slug, {
                    method: 'GET',
                });

                if (response) {
                    displayBlogDetails(response);
                }
            } catch (error) {
                console.log(error.response?.status);
                if (error.response?.status === 404) {
                    window.location.assign('/404');
                }
                console.log(error.data);
            }
        }

        function createCommentHTML(comment, isChild = false) {
            var childCommentsHTML = "";

            // Process child comments recursively
            if (comment.childrenComments && comment.childrenComments.length > 0) {
                var childWrapper = '<div class="replies">'; // Ensures sibling comments are properly grouped
                for (var i = 0; i < comment.childrenComments.length; i++) {
                    childWrapper += createCommentHTML(comment.childrenComments[i], true);
                }
                childWrapper += '</div>';
                childCommentsHTML = childWrapper;
            }

            var singleStyle = isChild ? ' style="border-left: 1px solid #8963ff; margin-left: 30px; padding-left: 20px"' : '';
            var replyFormId = 'reply-form-' + comment.id;
            var replyLinkId = 'reply-link-' + comment.id;
            var updateFormId = 'update-form-' + comment.id;

            let htmls =  '<div class="comment-list" style="margin-left: 30px; margin-top: 20px; padding-bottom: 0px">' +
                '<div class="single-comment justify-content-between d-flex" ' + singleStyle + '>' +
                '<div class="user justify-content-between d-flex">' +
                '<div style="margin-right: 10px" class="thumb">' +
                '<img style="width: 30px" src="' + avatarDefault + '" alt="User">' +
                '</div>' +
                '<div style="border-radius: 10px; background-color: #f2f4f7; padding-left: 10px; padding-top: 5px" class="desc">' +
                '<div class="d-flex justify-content-between">' +
                '<div class="d-flex align-items-center">' +
                '<h5>' +
                '<a href="#">' + comment.accountResponse.email + '</a>' +
                '</h5>' +
                '<p class="date">' + new Date(comment.createdAt).toLocaleString() + '</p>' +
                '</div>' +
                '<div class="reply-btn">' +
                '<a href="#" class="btn-reply text-uppercase" id="' + replyLinkId + '">Reply</a>' +
                '</div>' ;

             const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
             if (userCurrent) {
                 const user = JSON.parse(userCurrent);
                 if (user.email === comment.accountResponse?.email) {
                     htmls += '<div class="dropdown">' +
                                 '<button class="my-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">' +
                                    '<i class="fa-solid fa-ellipsis-vertical"></i>' +
                                 '</button>' +
                                 '<ul class="dropdown-menu">' +
                                     '<li><a class="dropdown-item-update" onclick="">Cập nhật bình luận</a></li>' +
                                     '<input type="hidden" id="update-comment-content-' + comment.id + '" value="' + comment.content + '">' +
                                     '<li><a class="dropdown-item-delete">Xóa bình luận</a></li>' +
                                     '<input type="hidden" id="delete-comment-' + comment.id + '" value="' + comment.id + '">' +
                     '</ul>' +
                             '</div>';
                 }
             }
                htmls +=
                '</div>' +
                '<p style="color: #70777f" class="comment">' + comment.content + '</p>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="' + replyFormId + '" class="reply-form" style="margin-top: 10px; display: none;">' +
                '<input type="text" id="accountReply' + comment.id + '" placeholder="Write a reply..." ' +
                'style="width: 100%; padding: 8px; margin-bottom: 10px; border-radius: 5px; border: 1px solid #ccc;">' +
                '<button id="post-btn-' + comment.id + '" ' +
                'style="padding: 8px 16px; border-radius: 5px; background-color: #4CAF50; color: white; border: none;">Gửi</button>' +
                '</div>' +
                '<div id="' + updateFormId + '" class="update-form" style="margin-top: 10px; display: none;">' +
                '<input type="text" id="accountUpdate' + comment.id + '" placeholder="Write a reply..." ' +
                'style="width: 100%; padding: 8px; margin-bottom: 10px; border-radius: 5px; border: 1px solid #ccc;">' +
                '<button id="update-btn-' + comment.id + '" ' +
                'style="padding: 8px 16px; border-radius: 5px; background-color: #4CAF50; color: white; border: none;">Cập nhật</button>' +
                '</div>' +
                childCommentsHTML + // Ensuring child comments render correctly
                '</div>';
                return htmls;
        }

        function attachUpdateCommentEventListeners() {
            var updateLinks = document.querySelectorAll('.dropdown-item-update'); // Target update comment button

            updateLinks.forEach(function (link) {
                link.addEventListener('click', function (event) {
                    event.preventDefault();

                    var dropdownMenu = link.closest('.dropdown'); // Find the closest dropdown container
                    var commentInput = dropdownMenu.querySelector('input[id^="update-comment-content-"]'); // Get the hidden input

                    if (!commentInput) {
                        console.error("Comment input not found!");
                        return;
                    }

                    var commentId = commentInput.id.replace('update-comment-content-', ''); // Extract the numeric ID
                    var replyFormId = 'update-form-' + commentId;
                    var replyForm = document.getElementById(replyFormId);

                    if (replyForm) {
                        replyForm.style.display = (replyForm.style.display === 'none' || !replyForm.style.display) ? 'block' : 'none';

                        var updateInput = replyForm.querySelector('input[type="text"]');
                        if (updateInput) {
                            updateInput.value = commentInput.value; // Correctly set the value
                        }
                    }
                });
            });
        }

        function attachUpdateEventListeners() {
            document.querySelectorAll('button[id^="update-btn-"]').forEach(function (button) {
                button.addEventListener('click', function () {
                    var commentId = this.id.replace('update-btn-', '');
                    var replyFormId = 'update-form-' + commentId;
                    var replyInput = document.getElementById(replyFormId).querySelector('input[type="text"]');
                    updateComment(commentId, replyInput.value);
                    replyInput.value = ""; // Clear the input after sending
                });
            });
        }

        async function updateComment(comment, content) {
            if (!content) {
                alert('Vui lòng nhập comment.');
                return;
            }

            const blogId = document.getElementById('blogId').value;
            console.log(blogId)
            const data = {
                content: content,
                id: comment
            };
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/blog-comment', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                })
                if (response) {
                    document.getElementById("comment").value = "";
                    await loadComment(slug);
                }
            } catch (error) {
                console.log(error.data)
            }
        }

        function deleteCommentEventListeners() {
            var deleteLinks = document.querySelectorAll('.dropdown-item-delete'); // Select delete buttons

            deleteLinks.forEach(function (link) {
                link.addEventListener('click', function (event) {
                    event.preventDefault();
                    var dropdownMenu = link.closest('.dropdown'); // Find dropdown container
                    var commentInput = dropdownMenu.querySelector('input[id^="delete-comment-"]'); // Find hidden input
                    if (!commentInput) {
                        console.error("Comment input not found!");
                        return;
                    }
                    var commentId = commentInput.value; // Extract the comment ID
                    Swal.fire({
                        title: "Bạn có chắc muốn xóa bình luận không?",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Có",
                        cancelButtonText: "Hủy",
                    }).then(async (result) => {
                        if (result.isConfirmed) {
                            try {
                                await deleteCommentFromServer(commentId); // Send request to delete comment
                                var commentElement = document.getElementById('comment-' + commentId);
                                if (commentElement) {
                                    commentElement.remove();
                                }
                                Swal.fire("Xóa thành công!", "Bình luận đã được xóa.", "success");
                            } catch (error) {
                                console.error("Lỗi khi xóa bình luận:", error);
                                Swal.fire("Lỗi!", "Không thể xóa bình luận. Vui lòng thử lại!", "error");
                            }
                        }
                    });
                });
            });
        }


        async function deleteCommentFromServer(commentId) {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/blog-comment/' + commentId, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    }
                });

                if (response) {
                    console.log("Bình luận đã được xóa thành công.");
                    await loadComment(slug);
                } else {
                    console.error("Xóa bình luận thất bại.");
                    await loadComment(slug);
                }
            } catch (error) {
                console.error("Lỗi API khi xóa bình luận:", error);
            }
        }


        function attachReplyEventListeners() {
            var replyLinks = document.querySelectorAll('[id^="reply-link-"]');
            replyLinks.forEach(function (link) {
                var replyFormId = 'reply-form-' + link.id.replace('reply-link-', '');
                link.addEventListener('click', function (event) {
                    event.preventDefault();
                    console.log(link.id)
                    console.log(replyFormId)
                    var replyForm = document.getElementById(replyFormId);
                    replyForm.style.display = (replyForm.style.display === 'none' || !replyForm.style.display) ? 'block' : 'none';
                });
            });
        }

        function attachPostReplyEventListeners() {
            document.querySelectorAll('button[id^="post-btn-"]').forEach(function (button) {
                button.addEventListener('click', function () {
                    var commentId = this.id.replace('post-btn-', '');
                    var replyFormId = 'reply-form-' + commentId;
                    var replyInput = document.getElementById(replyFormId).querySelector('input[type="text"]');
                    postReply(commentId, replyInput.value);
                    replyInput.value = ""; // Clear the input after sending
                });
            });
        }

        async function postReply(commentId, content) {
            if (!content) {
                alert('Vui lòng nhập comment.');
                return;
            }

            const blogId = document.getElementById('blogId').value;
            console.log(blogId)
            const data = {
                content: content,
                blogId: blogId,
                parentId: commentId
            };
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/blog-comment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                })
                if (response) {
                    document.getElementById("comment").value = "";
                    await loadComment(slug);
                }
            } catch (error) {
                console.log(error.data)
            }
        }

        function countChildren(comment) {
            let count = 0
            if (comment.childrenComments.length !== 0) {
                count += comment.childrenComments.length;
                for (let i = 0; i < comment.childrenComments.length; i++) {
                    count += countChildren(comment.childrenComments[i])
                }
                return count
            }
            return 0
        }

        function displayCommentResponse(comments) {
            const commentArea = document.querySelector('#comments-area');
            let count = comments.length;
            comments.forEach(comment => {
                count += countChildren(comment)
            })
            commentArea.innerHTML = `<h4>` + count + `
            Comments </h4>`;

            comments.forEach(comment => {
                const commentHTML = createCommentHTML(comment);
                commentArea.innerHTML += commentHTML;
            });
            attachReplyEventListeners();
            attachPostReplyEventListeners();
            attachUpdateCommentEventListeners();
            attachUpdateEventListeners();
            deleteCommentEventListeners()

        }

        async function loadComment(slug) {
            try {
                const response = await apiRequestWithToken(environment.apiUrl + '/api/blog-comment/' + slug, {
                    method: 'GET',
                });
                console.log(response)
                if (response) {
                    displayCommentResponse(response);
                }
            } catch (error) {
                console.log(error.data);
            }
        }

        function displayBlogDetails(blog) {
            const blogDetails = document.getElementById('blog_details');
            if (!blogDetails) return;
            blogDetails.innerHTML = "";
            var titleElement = document.createElement("h2");
            titleElement.style.color = "#2d2d2d";
            titleElement.textContent = blog.title;
            blogDetails.appendChild(titleElement);
            let ulElement = document.createElement("ul");
            ulElement.className = "blog-info-link mt-3 mb-4";

            let liAuthor = document.createElement("li");
            let authorLink = document.createElement("a");
            let authorIcon = document.createElement("i");
            authorIcon.className = "fa fa-user";
            authorLink.appendChild(authorIcon);
            authorLink.innerHTML += " " + (blog.accountResponse?.email || "Unknown");
            liAuthor.appendChild(authorLink);
            ulElement.appendChild(liAuthor);

            let liComments = document.createElement("li");
            let commentLink = document.createElement("a");
            let commentIcon = document.createElement("i");
            commentIcon.className = "fa fa-comments";
            commentLink.appendChild(commentIcon);
            commentLink.innerHTML += " " + blog.commentsCount + " Comments";
            liComments.appendChild(commentLink);
            ulElement.appendChild(liComments);

            // Likes Section (isLike and countLikes)
            let liLikes = document.createElement("li");
            let likeButton = document.createElement("a");
            likeButton.id = "likeButton";
            let likeIcon = document.createElement("i");
            likeIcon.className = blog.isLike ? "fa fa-heart text-danger" : "fa-regular fa-heart";
            likeButton.appendChild(likeIcon);
            likeButton.innerHTML += `<span id="likeCount">` + blog.likesCount + `</span> Likes`;
            liLikes.appendChild(likeButton);
            ulElement.appendChild(liLikes);

            likeButton.addEventListener("click", async function (event) {
                event.preventDefault();
                const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
                if (!userCurrent) {
                    window.location.assign('/login');
                    return;
                }
                try {
                    const url = environment.apiUrl + "/api/blogs-statistic/" + blog.id;
                    const method = blog.isLike ? "DELETE" : "POST";
                    const response = await apiRequestWithToken(url, {
                        method: method,
                        headers: {
                            "Content-Type": "application/json",
                        },
                    });
                    if (!response) {
                        throw new Error("Failed to update like");
                    }
                    const path = window.location.pathname;
                    const slug = path.split('/').pop();
                    await loadDetailBlog(slug);
                } catch (error) {
                    console.error("Error updating like:", error);
                }
            });


            blogDetails.appendChild(ulElement);
            // Tạo input ẩn chứa blog ID
            const hiddenInput = document.createElement("input");
            hiddenInput.type = "hidden";
            hiddenInput.id = "blogId";
            hiddenInput.value = blog.id;
            blogDetails.appendChild(hiddenInput);

            var contentElement = document.createElement("p");
            contentElement.className = "excert";
            contentElement.innerHTML = blog.content;
            blogDetails.appendChild(contentElement);
            var quoteWrapper = document.createElement("div");
            quoteWrapper.className = "quote-wrapper";

            var quoteContent = document.createElement("div");
            quoteContent.className = "quotes";
            quoteContent.textContent = "Bài viết này có " + blog.viewsCount + " lượt xem và " + blog.likesCount + " lượt thích.";
            quoteWrapper.appendChild(quoteContent);
            blogDetails.appendChild(quoteWrapper);
            var tagContainer = document.createElement("p");
            var strongTag = document.createElement("strong");
            strongTag.textContent = "Tags: ";
            tagContainer.appendChild(strongTag);
            if (blog.tagResponses && blog.tagResponses.length > 0) {
                for (let i = 0; i < blog.tagResponses.length; i++) {
                    let tagSpan = document.createElement("span");
                    tagSpan.className = "badge bg-secondary";
                    tagSpan.style.marginRight = "5px";
                    tagSpan.textContent = blog.tagResponses[i].name;
                    tagContainer.appendChild(tagSpan);
                }
            }
            blogDetails.appendChild(tagContainer);
        }

        await loadDetailBlog(slug);

        await loadComment(slug);

        const commentForm = document.getElementById("commentForm");

        commentForm.addEventListener("submit", async function (event) {
            event.preventDefault();
            const commentInput = document.getElementById("comment").value.trim();
            if (!commentInput) {
                alert("Vui lòng nhập nội dung bình luận!");
                return;
            }
            const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
            if (!userCurrent) {
                window.location.assign('/login');
                return;
            }
            const id = document.querySelector('#blogId')?.value;
            console.log("Blog ID:", id);

            if (id) {
                try {
                    const response = await apiRequestWithToken(
                        environment.apiUrl + "/api/blog-comment/" + id,
                        {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                            },
                            body: JSON.stringify({content: commentInput, blogId: id}),
                        }
                    );
                    if (response) {
                        document.getElementById("comment").value = "";
                        await loadComment(slug);
                    }
                } catch (error) {
                    console.error("Lỗi khi gửi bình luận:", error);
                    alert("Có lỗi xảy ra, vui lòng thử lại sau!");
                }
            }
        });


    });

</script>

<script src="../../assets/js/vendor/modernizr-3.5.0.min.js"></script>
<!-- Jquery, Popper, Bootstrap -->
<script src="../../assets/js/vendor/jquery-1.12.4.min.js"></script>
<script src="../../assets/js/popper.min.js"></script>
<script src="../../assets/js/bootstrap.min.js"></script>
<!-- Jquery Mobile Menu -->
<script src="../../assets/js/jquery.slicknav.min.js"></script>

<!-- Jquery Slick , Owl-Carousel Plugins -->
<script src="../../assets/js/owl.carousel.min.js"></script>
<script src="../../assets/js/slick.min.js"></script>
<!-- One Page, Animated-HeadLin -->
<script src="../../assets/js/wow.min.js"></script>
<script src="../../assets/js/animated.headline.js"></script>
<script src="../../assets/js/jquery.magnific-popup.js"></script>

<!-- Date Picker -->
<script src="../../assets/js/gijgo.min.js"></script>
<!-- Nice-select, sticky -->
<script src="../../assets/js/jquery.nice-select.min.js"></script>
<script src="../../assets/js/jquery.sticky.js"></script>

<!-- counter , waypoint,Hover Direction -->
<script src="../../assets/js/jquery.counterup.min.js"></script>
<script src="../../assets/js/waypoints.min.js"></script>
<script src="../../assets/js/jquery.countdown.min.js"></script>
<script src="../../assets/js/hover-direction-snake.min.js"></script>

<!-- contact js -->
<script src="../../assets/js/contact.js"></script>
<script src="../../assets/js/jquery.form.js"></script>
<script src="../../assets/js/jquery.validate.min.js"></script>
<script src="../../assets/js/mail-script.js"></script>
<script src="../../assets/js/jquery.ajaxchimp.min.js"></script>

<!-- Jquery Plugins, main Jquery -->
<script src="../../assets/js/plugins.js"></script>
<script src="../../assets/js/main.js"></script>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>

</body>
</html>