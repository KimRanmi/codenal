/*
Template Name: Velzon - Admin & Dashboard Template
Author: Themesbrand
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Project create init js
*/


// Dropzone
var dropzonePreviewNode = document.querySelector("#dropzone-preview-list");
if (dropzonePreviewNode) {
    dropzonePreviewNode.id = "";
    var previewTemplate = dropzonePreviewNode.parentNode.innerHTML;
    dropzonePreviewNode.parentNode.removeChild(dropzonePreviewNode);
    var dropzone = new Dropzone(".dropzone", {
        url: 'https://httpbin.org/post',
        method: "post",
        previewTemplate: previewTemplate,
        previewsContainer: "#dropzone-preview",
    });


function announceCreate() {
    // 입력 필드의 값을 가져오기
    let aTitle = document.getElementById('project-title-input').value;
    let aContent = document.getElementById('editor').value;
    let readAuth = document.querySelector('input[name="read-auth"]:checked').value;
    
    // JSON 객체 생성
    let obj = {
        announce_title: aTitle,
        announce_content: editor.getHTML(),
        read_authority_status: readAuth
    };
    let jsonData = JSON.stringify(obj);

	const csrfToken = document.getElementById("csrf_token").value;

    // fetch를 사용하여 서버에 POST 요청 보내기
    fetch('/announce/createEnd', {
        method: 'POST',
        headers: {
           "Content-Type": "application/json;charset=utf-8",
	       "Accept": "application/json",
	       'X-CSRF-TOKEN': csrfToken

        },
        body: jsonData
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                throw new Error('Network response was not ok: ' + response.statusText + ' - ' + JSON.stringify(errorData));
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('서버 응답 데이터:', data);
        alert('전송 성공: ' + JSON.stringify(data));
    })
    .catch(error => {
        console.error('There was a problem with your fetch operation:', error);
        alert('전송 실패: ' + error.message);
    });
}


}