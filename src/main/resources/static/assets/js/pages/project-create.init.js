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

}

document.addEventListener('DOMContentLoaded', () => {

const form = document.getElementById("announceForm");
const fileInput = document.getElementById("file");

form.addEventListener('submit', (e) => {
    e.preventDefault(); // 기본 폼 제출 동작 방지

    // FormData 객체를 생성하여 폼 데이터를 가져옴
    const formData = new FormData(form);

    // 추가적으로 필요한 데이터를 FormData에 추가
    formData.append('announce_writer', document.getElementById('announce_writer').value);
    formData.append('announce_title', document.getElementById('announce_title').value);
    formData.append('announce_content', editor.getHTML());
    formData.append('read_authority_status', document.querySelector('input[name="read_authority_status"]:checked')?.value);
	formData.append('file', fileInput);
	
    const csrfToken = document.getElementById("csrf_token").value;

    fetch('/announce/createEnd', {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 포함
        },
        body: formData // FormData 객체를 body에 포함하여 전송
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
        if (data.res_code === '200') {
            Swal.fire({
                icon: 'success',
                title: '성공',
                text: data.res_msg
            }).then((result) => {
                location.href = "/announce";
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: '실패',
                text: data.res_msg
            });
        }
    })
    .catch(error => console.error('Error:', error));
});
});