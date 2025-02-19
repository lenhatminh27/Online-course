<%@ page contentType="text/html; charset=UTF-8" %>
<nav class="w3-sidebar w3-red w3-collapse w3-top w3-large w3-padding" style="z-index:3;width:300px;font-weight:bold;"
     id="mySidebar"><br>
  <a href="javascript:void(0)" onclick="w3_close()" class="w3-button w3-hide-large w3-display-topleft"
     style="width:100%;font-size:22px">Đóng Menu</a>
  <div class="w3-container">
    <h3 onclick="location.href='/home'"><b><i class="fa-solid fa-arrow-left"></i> Quay lại Học viên</b></h3>
  </div>
  <div class="w3-container">
    <h3><b>OCMS</b></h3>
  </div>
  <div class="w3-bar-block">
    <a href="/instructor" onclick="w3_close()" class="w3-bar-item w3-button w3-hover-white">Trang chủ</a>
    <a href="/instructor/course" onclick="w3_close()" class="w3-bar-item w3-button w3-hover-white">Khóa học</a>
    <a href="/instructor/blogs" onclick="w3_close()" class="w3-bar-item w3-button w3-hover-white">Blog</a>
  </div>
</nav>
