<%@ page contentType="text/html; charset=UTF-8" %>
<header class="w3-container w3-large w3-display-container" style="height: 70px; border-bottom: red solid 2px">
    <div class="w3-display-right">
        <img src="https://www.w3schools.com/w3images/avatar2.png" alt="Avatar"
             class="w3-circle w3-margin-right" style="width:40px; height:40px;">
        <a id="logoutBtns" class="w3-button w3-white w3-border w3-round-large">Đăng xuất</a>
    </div>
</header>

<script type="module">
    import {STORAGE_KEY, environment} from '../../assets/config/env.js';
    import {apiRequestWithToken} from '../../assets/config/service.js';
    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
    if (userCurrent) {
        const user = JSON.parse(userCurrent);
        if(!user.roles.includes('INSTRUCTOR')){
            window.location.replace('/403');
        }
        const userAvatar = user.avatar;
        const email = user.email;
        document.getElementById('userAvatar').src = userAvatar;
        document.getElementById('emailHeader').innerHTML = email;
    } else {
        window.location.replace('/login');
        document.getElementById('userAvatar').style.display = 'none';
        document.getElementById('emailHeader').style.display = 'none';
    }
    document.getElementById('logoutBtns').addEventListener('click', function() {
        apiRequestWithToken(environment.apiUrl + '/api/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({refreshToken: localStorage.getItem(STORAGE_KEY.refreshToken)})
        })
            .then(response => {
                console.log(response);
            })
            .catch(error => console.error('Error:', error))
            .finally(() => {
                localStorage.removeItem(STORAGE_KEY.userCurrent);
                localStorage.removeItem(STORAGE_KEY.accessToken);
                localStorage.removeItem(STORAGE_KEY.refreshToken);
                // Redirect to the login page
                window.location.href = '/login';
            });
    });
</script>