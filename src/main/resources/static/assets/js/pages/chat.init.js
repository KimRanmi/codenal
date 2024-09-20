 (function () {

	function scrollToBottom(id) {
	        setTimeout(function () {
	            var simpleBar = document.getElementById(id).querySelector("#chat-conversation .simplebar-content-wrapper");
	            var offsetHeight = document.getElementsByClassName("chat-conversation-list")[0] ? document.getElementById(id).getElementsByClassName("chat-conversation-list")[0].scrollHeight - window.innerHeight + 370 : 0;
	            if(offsetHeight) {
	                simpleBar.scrollTo({ top: offsetHeight, behavior: "smooth" });
	            }
	        }, 100);
	    }

    //chat form
    var chatForm = document.querySelector("#chatinput-form");
    var chatInput = document.querySelector("#chat-input");
    var chatInputfeedback = document.querySelector(".chat-input-feedback");
    

/*	const csrfToken = document.getElementById("csrf_token").value;
	
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
	};*/


    var emojiPicker = new FgEmojiPicker({
        trigger: [".emoji-btn"],
        removeOnSelection: false,
        closeButton: true,
        position: ["top", "right"],
        preFetch: true,
        dir: "assets/js/pages/plugins/json",
        insertInto: document.querySelector(".chat-input"),
    });


    // emojiPicker position
    var emojiBtn = document.getElementById("emoji-btn");
    emojiBtn.addEventListener("click", function () {
        setTimeout(function () {
            var fgEmojiPicker = document.getElementsByClassName("fg-emoji-picker")[0];
            if (fgEmojiPicker) {
                var leftEmoji = window.getComputedStyle(fgEmojiPicker) ? window.getComputedStyle(fgEmojiPicker).getPropertyValue("left") : "";
                if (leftEmoji) {
                    leftEmoji = leftEmoji.replace("px", "");
                    leftEmoji = leftEmoji - 40 + "px";
                    fgEmojiPicker.style.left = leftEmoji;
                }
            }
        }, 0);
    });

});