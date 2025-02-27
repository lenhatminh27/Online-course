<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WebSocket Client</title>
</head>
<body>
<h2>WebSocket Test</h2>
<button id="connectBtn">Connect WebSocket</button>
<button id="sendTestBtn">Test Server Send</button>
<div id="messages"></div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        let socket;
        const userId = "123";  // Thay đổi userId nếu cần
        const apiUrl = "http://localhost:8080"; // Thay bằng API URL thực tế của bạn

        function connectWebSocket() {
            socket = new WebSocket(`ws://localhost:8080/payment/result/` + userId);

            socket.onopen = function () {
                console.log("WebSocket connected");
                document.getElementById("messages").innerHTML += "<p>Connected to WebSocket</p>";
            };

            socket.onmessage = function (event) {
                console.log("Received:", event.data);
                document.getElementById("messages").innerHTML += `<p>Server:` + event.data + `</p>`;
            };

            socket.onclose = function () {
                console.log("WebSocket closed");
                document.getElementById("messages").innerHTML += "<p>Disconnected</p>";
            };
        }

        function testServerSend() {
            fetch(apiUrl + `/test-websocket?userId=` + userId)
                .then(response => response.text())
                .then(data => {
                    console.log(data);
                    document.getElementById("messages").innerHTML += `<p>Test message sent: ${data}</p>`;
                })
                .catch(error => console.error("Error:", error));
        }

        // Gắn sự kiện cho nút sau khi DOM đã sẵn sàng
        document.getElementById("connectBtn").addEventListener("click", connectWebSocket);
        document.getElementById("sendTestBtn").addEventListener("click", testServerSend);
    });
</script>

</body>
</html>
