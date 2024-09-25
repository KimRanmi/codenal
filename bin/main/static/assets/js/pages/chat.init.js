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

    //Chat Message
    function getChatMessages(jsonFileUrl) {
        getJSONFile(jsonFileUrl, function (err, data) {
            if (err !== null) {
                console.log("Something went wrong: " + err);
            } else {
                var chatsData =
                    currentSelectedChat == "users" ? data[0].chats : data[0].channel_chat;

                document.getElementById(
                    currentSelectedChat + "-conversation"
                ).innerHTML = "";
                var isContinue = 0;
                chatsData.forEach(function (isChat, index) {

                    if (isContinue > 0) {
                        isContinue = isContinue - 1;
                        return;
                    }
                    var isAlighn = isChat.from_id == userChatId ? " right" : " left";

                    var user = usersList.find(function (list) {
                        return list.id == isChat.to_id;
                    });

                    var msgHTML = '<li class="chat-list' + isAlighn + '" id=' + isChat.id + '>\
                        <div class="conversation-list">';
                    if (userChatId != isChat.from_id)
                        msgHTML += '<div class="chat-avatar"><img src="/' + user.profile + '" alt=""></div>';

                    msgHTML += '<div class="user-chat-content">';

                    msgHTML +=
                        '<div class="conversation-name"><span class="d-none name">' + user.name + '</span><small class="text-muted time">' + isChat.datetime +
                        '</small> <span class="text-success check-message-icon"><i class="bx bx-check-double"></i></span></div>';
                    msgHTML += "</div>\
                </div>\
            </li>";

                    document.getElementById(currentSelectedChat + "-conversation").innerHTML += msgHTML;
                });
            }
            deleteMessage();
            deleteChannelMessage();
            deleteImage();
            replyMessage();
            replyChannelMessage();
            copyMessage();
            copyChannelMessage();
            copyClipboard();
            scrollToBottom("users-chat");
            updateLightbox();
        });
    }






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