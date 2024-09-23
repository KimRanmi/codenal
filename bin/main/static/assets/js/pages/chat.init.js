 (function () {
    var dummyUserImage = "/assets/images/users/user-dummy-img.jpg";
    var dummyMultiUserImage = "/assets/images/users/multi-user.jpg";
    var isreplyMessage = false;



 
    // toggleSelected
    function toggleSelected() {
        var userChatElement = document.querySelectorAll(".user-chat");
        Array.from(document.querySelectorAll(".chat-user-list li a")).forEach(function (item) {
            item.addEventListener("click", function (event) {
                userChatElement.forEach(function (elm) {
                    elm.classList.add("user-chat-show");
                });

                // chat user list link active
                var chatUserList = document.querySelector(".chat-user-list li.active");
                if (chatUserList) chatUserList.classList.remove("active");
                this.parentNode.classList.add("active");
            });
        });

        // user-chat-remove
        document.querySelectorAll(".user-chat-remove").forEach(function (item) {
            item.addEventListener("click", function (event) {
                userChatElement.forEach(function (elm) {
                    elm.classList.remove("user-chat-show");
                });
            });
        });
    }

     //User current Id
    var currentChatId = "users-chat";
    var currentSelectedChat = "users";
    var url = "assets/json/";
    var usersList = "";
    var userChatId = 1;

    scrollToBottom(currentChatId);
 
     
     
 
     // get User list
    getJSON("chat-users-list.json", function (err, data) {
        if (err !== null) {
            console.log("Something went wrong: " + err);
        } else {
            // set users message list
            var users = data[0].users;
            users.forEach(function (userData, index) {
                var isUserProfile = userData.profile ? '<img src="/' + userData.profile + '" class="rounded-circle img-fluid userprofile" alt=""><span class="user-status"></span>'
                    : '<div class="avatar-title rounded-circle bg-primary text-white fs-10">' + userData.nickname + '</div><span class="user-status"></span>';

                var isMessageCount = userData.messagecount ? '<div class="ms-auto"><span class="badge badge-soft-dark rounded p-1">' +
                    userData.messagecount +
                    "</span></div>"
                    : "";

                var messageCount = userData.messagecount ? '<a href="javascript: void(0);" class="unread-msg-user">' : '<a href="javascript: void(0);">'
                var activeClass = userData.id === 1 ? "active" : "";
                document.getElementById("userList").innerHTML +=
                    '<li id="contact-id-' + userData.id + '" data-name="direct-message" class="' + activeClass + '">\
                '+ messageCount + ' \
                <div class="d-flex align-items-center">\
                    <div class="flex-shrink-0 chat-user-img ' + userData.status + ' align-self-center me-2 ms-0">\
                        <div class="avatar-xxs">\
                        ' + isUserProfile + '\
                        </div>\
                    </div>\
                    <div class="flex-grow-1 overflow-hidden">\
                        <p class="text-truncate mb-0">' + userData.name + "</p>\
                    </div>\
                    " + isMessageCount + "\
                </div>\
            </a>\
        </li>";
            });


            // set channels list
            var channelsData = data[0].channels;
            channelsData.forEach(function (isChannel, index) {
                var isMessage = isChannel.messagecount
                    ? '<div class="flex-shrink-0 ms-2"><span class="badge badge-soft-dark rounded p-1">' +
                    isChannel.messagecount +
                    "</span></div>"
                    : "";
                var isMessageCount = isChannel.messagecount ? '<div class="ms-auto"><span class="badge badge-soft-dark rounded p-1">' +
                    isChannel.messagecount +
                    "</span></div>"
                    : "";
                var messageCount = isChannel.messagecount ? '<a href="javascript: void(0);" class="unread-msg-user">' : '<a href="javascript: void(0);">'
                document.getElementById("channelList").innerHTML +=
                    '<li id="contact-id-' + isChannel.id + '" data-name="channel">\
            '+ messageCount + ' \
                <div class="d-flex align-items-center">\
                    <div class="flex-shrink-0 chat-user-img align-self-center me-2 ms-0">\
                        <div class="avatar-xxs">\
                            <div class="avatar-title bg-light rounded-circle text-body">#</div>\
                        </div>\
                    </div>\
                    <div class="flex-grow-1 overflow-hidden">\
                        <p class="text-truncate mb-0">' + isChannel.name + "</p>\
                    </div>\
                    <div>" + isMessage + "</div>\
                    </div>\
            </a>\
        </li>";
            });
        }
        toggleSelected();
        chatSwap();
    });



		function updateSelectedChat() {
		        if (currentSelectedChat == "users") {
		            document.getElementById("channel-chat").style.display = "none";
		            document.getElementById("users-chat").style.display = "block";
		            getChatMessages(url + "chats.json");
		        } else {
		            document.getElementById("channel-chat").style.display = "block";
		            document.getElementById("users-chat").style.display = "none";
		            getChatMessages(url + "chats.json");
		        }
		    }
		    updateSelectedChat();
 
 
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
                    msgHTML += getMsg(isChat.id, isChat.msg, isChat.has_images, isChat.has_files, isChat.has_dropDown);
                    if (chatsData[index + 1] && isChat.from_id == chatsData[index + 1]["from_id"]) {
                        isContinue = getNextMsgCounts(chatsData, index, isChat.from_id);
                        msgHTML += getNextMsgs(chatsData, index, isChat.from_id, isContinue);
                    }

                    msgHTML +=
                        '<div class="conversation-name"><span class="d-none name">' + user.name + '</span><small class="text-muted time">' + isChat.datetime +
                        '</small> <span class="text-success check-message-icon"><i class="bx bx-check-double"></i></span></div>';
                    msgHTML += "</div>\
                </div>\
            </li>";

                    document.getElementById(currentSelectedChat + "-conversation").innerHTML += msgHTML;
                });
            }
//            deleteMessage();
//            deleteChannelMessage();
//            deleteImage();
//            replyMessage();
//            replyChannelMessage();
//            copyMessage();
//            copyChannelMessage();
//            copyClipboard();
//            scrollToBottom("users-chat");
//            updateLightbox();
        });
    }
 
 
    
 
    //user Name and user Profile change on click
    function chatSwap() {
        document.querySelectorAll("#userList li").forEach(function (item) {
            item.addEventListener("click", function () {
                currentSelectedChat = "users";
                updateSelectedChat();
                currentChatId = "users-chat";
                var contactId = item.getAttribute("id");
                var username = item.querySelector(".text-truncate").innerHTML;

                document.querySelector(".user-chat-topbar .text-truncate .username").innerHTML = username;
                document.querySelector(".profile-offcanvas .username").innerHTML = username;

                if (isreplyMessage == true) {
                    isreplyMessage = false;
                    document.querySelector(".replyCard").classList.remove("show");
                }

                if (document.getElementById(contactId).querySelector(".userprofile")) {
                    var userProfile = document.getElementById(contactId).querySelector(".userprofile").getAttribute("src");
                    document.querySelector(".user-chat-topbar .avatar-xs").setAttribute("src", userProfile);
                    document.querySelector(".profile-offcanvas .avatar-lg").setAttribute("src", userProfile);
                } else {
                    document.querySelector(".user-chat-topbar .avatar-xs").setAttribute("src", dummyUserImage);
                    document.querySelector(".profile-offcanvas .avatar-lg").setAttribute("src", dummyUserImage);
                }

                var conversationImg = document.getElementById("users-conversation");
                conversationImg.querySelectorAll(".left .chat-avatar").forEach(function (item) {
                    if (userProfile) {
                        item.querySelector("img").setAttribute("src", userProfile);
                    } else {
                        item.querySelector("img").setAttribute("src", dummyUserImage);
                    }
                });
                window.stop();
            });
        });

        //channel Name and channel Profile change on click
        document.querySelectorAll("#channelList li").forEach(function (item) {
            item.addEventListener("click", function () {
                currentChatId = "channel-chat";
                currentSelectedChat = "channel";
                updateSelectedChat();
                var channelname = item.querySelector(".text-truncate").innerHTML;
                var changeChannelName = document.getElementById("channel-chat");
                changeChannelName.querySelector(".user-chat-topbar .text-truncate .username").innerHTML = channelname;
                document.querySelector(".profile-offcanvas .username").innerHTML = channelname;

                changeChannelName.querySelector(".user-chat-topbar .avatar-xs").setAttribute("src", dummyMultiUserImage);
                document.querySelector(".profile-offcanvas .avatar-lg").setAttribute("src", dummyMultiUserImage);
                if (isreplyMessage == true) {
                    isreplyMessage = false;
                    document.querySelector(".replyCard").classList.remove("show");
                }
            });
        });
    };

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

    function currentTime() {
        var ampm = new Date().getHours() >= 12 ? "오후" : "오전";
        var hour =
            new Date().getHours() > 12 ?
                new Date().getHours() % 12 :
                new Date().getHours();
        var minute =
            new Date().getMinutes() < 10 ?
                "0" + new Date().getMinutes() :
                new Date().getMinutes();
        if (hour < 10) {
            return "0" + hour + ":" + minute + " " + ampm;
        } else {
            return hour + ":" + minute + " " + ampm;
        }
    }
    setInterval(currentTime, 1000);

    var messageIds = 0;

    if (chatForm) {
        //add an item to the List, including to local storage
        chatForm.addEventListener("submit", function (e) {
            e.preventDefault();

            var chatId = currentChatId;
            var chatReplyId = currentChatId;

            var chatInputValue = chatInput.value

            if (chatInputValue.length === 0) {
                chatInputfeedback.classList.add("show");
                setTimeout(function () {
                    chatInputfeedback.classList.remove("show");
                }, 2000);
            } else {
                if (isreplyMessage == true) {
                    getReplyChatList(chatReplyId, chatInputValue);
                    isreplyMessage = false;
                } else {
                    getChatList(chatId, chatInputValue);
                }
                scrollToBottom(chatId || chatReplyId);
            }
            chatInput.value = "";

            //reply msg remove textarea
            document.getElementById("close_toggle").click();
        })
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

})();

//Search Message
function searchMessages() {
    var searchInput, searchFilter, searchUL, searchLI, a, i, txtValue;
    searchInput = document.getElementById("searchMessage");
    searchFilter = searchInput.value.toUpperCase();
    searchUL = document.getElementById("users-conversation");
    searchLI = searchUL.getElementsByTagName("li");
    searchLI.forEach(function (search) {
        a = search.getElementsByTagName("p")[0] ? search.getElementsByTagName("p")[0] : '';
        txtValue = a.textContent || a.innerText ? a.textContent || a.innerText : '';
        if (txtValue.toUpperCase().indexOf(searchFilter) > -1) {
            search.style.display = "";
        } else {
            search.style.display = "none";
        }
    });
};