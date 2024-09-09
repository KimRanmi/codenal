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
    // 라디오 버튼 요소 가져오기
    const allRadio = document.getElementById('all');
    const selectRadio = document.getElementById('select');
    const organizationChart = document.getElementById('organization-chart');

    // 라디오 버튼의 변경 이벤트 리스너 등록
    allRadio.addEventListener('change', () => {
        if (allRadio.checked) {
            organizationChart.style.display = 'none';
        }
    });

    selectRadio.addEventListener('change', () => {
        if (selectRadio.checked) {
            organizationChart.style.display = 'block';
            loadOrganizationChart(); // 조직도 불러오기
        }
    });

    // 조직도 불러오는 함수
    function loadOrganizationChart() {
        // 여기에 조직도를 불러오는 AJAX 요청 또는 다른 로직을 추가
        // AJAX 요청을 통해 조직도 데이터를 가져와서 organizationChart div에 추가

        // 조직도 HTML을 삽입하는 코드
        organizationChart.innerHTML = `
            <h3>조직도</h3>
            <p>부서 및 직급을 선택하세요.</p>
        `;
    }
});



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
     .then(response => response.json())
     .then(data => {
        if (data.res_code === '200') {
            alert('성공적으로 생성이 완료되었습니다.');
            location.href = `/announce/detail/${data.announceNo}`; // 서버에서 받은 `announceNo`로 URL을 설정합니다.
        } else {
            alert('실패: ' + data.res_msg);
        }
     })
     .catch(error => console.error('Error:', error));
});
});