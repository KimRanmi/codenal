 (function () {
    var dummyUserImage = "/assets/images/users/user-dummy-img.jpg";
    var dummyMultiUserImage = "/assets/images/users/multi-user.jpg";
    var isreplyMessage = false;



	const csrfToken = document.getElementById("csrf_token").value;
	
	const selectEmployeeForm = document.querySelectorAll();
    //user list by json
	var getJSON = function (jsonurl, callback) {
	    fetch("/chatList/chatRoom/create", {
	        method: 'POST',
	        headers: {
            'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 포함
        },
	    })
	    .then(response => {
	        if (response.ok) {
	            return response.json(); // JSON 응답을 반환
	        } else {
	            throw new Error(response.status); // 에러 상태 코드
	        }
	    })
	    .then(data => callback(null, data))
	    .catch(error => callback(error.message, null));
	};


});