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
        
		jQuery(document).ready(function () {				
		    // 트리 메뉴 데이터
		    $.ajax({
		        url: '/employee/addressBook/tree-menu',
		        method: 'GET',
		        dataType: 'json',
		        success: function (data) {		
		        	
		           /*  if (!data || data.length === 0) {
		                console.error('Received data is empty or undefined.');
		                return;
		            } */

		            // TreeMenuDto
		            const formatDataForJsTree = (nodes) => {
		                return nodes.map(node => {
		                    return {
		                        id: node.nodeId,
		                        text: node.nodeName,
		                        state: {
		                            opened: false	// 직원은 처음에 접어둠
		                        },
		                        children: node.nodeChildren ? formatDataForJsTree(node.nodeChildren) : [],						                    
		                        icon: node.nodeChildren && node.nodeChildren.length > 0 ? 'ri-team-fill' : 'ri-user-2-fill'
		                        		// 부서 아이콘 : 직원 아이콘
		                    };
		                });
		            };

		            const formattedData = formatDataForJsTree(data);

		            // 그룹웨어 회사 이름
		            const companyNode = {
		                id: 'companyName',
		                text: 'withXwork', // 위드워크 회사명
		                state: {
		                    opened: true
		                },
		                children: formattedData, 
		                icon: 'ri-building-fill' // 회사 아이콘
		            };

		            // jsTree 초기화
		            $('#treeMenu').jstree({
		                'core': {
		                    "animation": 0,
		                    "check_callback": false,
		                    'data': [companyNode] 
		                },
		                "plugins": ["types"],
		                "types": {
		                    "default": {
		                        "icon": "ri-team-fill"	// 부서 아이콘
		                    },
		                    "file": {
		                        "icon": "ri-user-2-fill"	// 직원 아이콘
		                    }
		                }
		            });
		        },
		       /*  error: function (xhr, status, error) {
		            console.error('Error loading tree menu data:', error);
		        } */
		    });
		});
		
        /*loadOrganizationChart(); // 조직도 불러오기*/
    }
});

/*// 조직도 불러오는 함수
function loadOrganizationChart() {
	// 여기에 조직도를 불러오는 AJAX 요청 또는 다른 로직을 추가
	// AJAX 요청을 통해 조직도 데이터를 가져와서 organizationChart div에 추가

	// 조직도 HTML을 삽입하는 코드
	organizationChart.innerHTML = `
	<h3>조직도</h3>
	<p>부서 및 직급을 선택하세요.</p>
	`;



}
*/

});



document.addEventListener('DOMContentLoaded', () => {

const form = document.getElementById("announceForm");

form.addEventListener('submit', (e) => {
    e.preventDefault(); // 기본 폼 제출 동작 방지
    // 유효성 검사
	if (editor.getMarkdown().length < 1) {
	    alert('에디터 내용을 입력해 주세요.');
	    throw new Error('editor content is required!');
	}
	
    // FormData 객체를 생성하여 폼 데이터를 가져옴
    const formData = new FormData(form);

    // 추가적으로 필요한 데이터를 FormData에 추가
    formData.append('announce_writer', document.getElementById('announce_writer').value);
    formData.append('announce_title', document.getElementById('announce_title').value);
    formData.append('announce_content', editor.getHTML());
    formData.append('read_authority_status', document.querySelector('input[name="read_authority_status"]:checked')?.value);

	
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


/*                      전자결재                      */
/*모달 관련 js*/
const loginId = document.getElementById('emp_id').value;
document.addEventListener('DOMContentLoaded',function(){
    const selectArray = [];
    let buttonId = '';

    $('#button_modal1, #button_modal2, #references').click(function() {
        buttonId = this.id;
        console.log(buttonId);

        // 선택 배열 초기화
        selectArray.length = 0;

        // jsTree 선택 초기화
        if ($('#treeMenu').data('jstree')) {
            $('#treeMenu').jstree("deselect_all");
        }

        $.ajax({
            url: '/approval/modal',
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                console.log(data);

                // jsTree 데이터 형식 변환
                const formatDataForJsTree = (nodes) => {
                    return nodes
                        .filter(node => node.nodeId.toString() !== loginId)
                        .map(node => ({
                            id: node.nodeId,
                            text: node.nodeName,
                            state: { opened: true },
                            children: node.nodeChildren ? formatDataForJsTree(node.nodeChildren) : [],
                            icon: node.nodeChildren && node.nodeChildren.length > 0 ? 'ri-team-fill' : 'ri-user-2-fill'
                        }));
                };

                const formattedData = formatDataForJsTree(data);
                const companyNode = {
                    id: 'companyName',
                    text: 'withXwork',
                    state: { opened: true },
                    children: formattedData,
                    icon: 'ri-building-fill'
                };

                // jsTree 초기화
                if ($('#treeMenu').data('jstree')) {
                    $('#treeMenu').jstree("destroy");
                }

                $('#treeMenu').jstree({
                    'core': {
                        animation: 0,
                        check_callback: false,
                        data: [companyNode]
                    },
                    'plugins': ['checkbox'],
                    "types": {
                        default: { icon: "ri-team-fill" },
                        file: { icon: "ri-user-2-fill" }
                    }
                }).on('select_node.jstree', function(e, data) {
                    const selectedNodes = $('#treeMenu').jstree("get_selected", true);
                    selectArray.length = 0; // 배열 초기화

                    selectedNodes.forEach(node => {
                        if (node.parent !== 'companyName') {
                            selectArray.push({ id: node.id, text: node.text });
                            console.log(node);
                        }
                    });
                });

                // 모달 표시
                const modalElement = document.getElementById('staticBackdrop');
                if (modalElement) {
                    const modal = new bootstrap.Modal(modalElement, {
                        backdrop: 'static',
                        keyboard: false 
                    });
                    modal.show(); 
                    
                    // 기존 클릭 이벤트 해제
                    $(document).off('click', '#successBtn'); 
                    
                    // 성공 버튼 클릭 이벤트 등록
                    $(document).on('click', '#successBtn', function() {
                        if (buttonId === "references") {
                            const selectedNames = selectArray.map(item => item.text).join(' '); // 선택된 이름을 결합
                            const selectNameElement = document.getElementById('references'); 

                            selectNameElement.value = selectedNames; // 결합된 문자열 설정
                            selectNameElement.setAttribute("data-id", selectArray.map(item => item.id).join(' '));  // json 데이터로 보낼 때 쓸 data-id 값도 넣어주기
                        } else {
                            const selectNameElements = buttonId === "button_modal1" 
                                ? document.querySelectorAll('.selectedName') 
                                : document.querySelectorAll('.selectedName2');

                            const length = Math.max(selectNameElements.length, selectArray.length); // 최대 길이로 설정

                            for (let index = 0; index < length; index++) {
                                if (index < selectArray.length) {
                                    // 배열에 값이 있을 때
                                    selectNameElements[index].textContent = selectArray[index].text; // 선택된 이름 설정
                                    selectNameElements[index].setAttribute('data-name', selectArray[index].id); // data-name 설정
                                } else {
                                    // 배열에 값이 없을 때
                                    selectNameElements[index].textContent = ''; // 비워두기
                                    selectNameElements[index].removeAttribute('data-name'); // data-name 제거
                                }
                            }
                        }
                        modal.hide(); // 모달 닫기
                    });
                } else {
                    console.error("모달 요소를 찾을 수 없습니다.");
                }
            }
        })
    })
	});
// 날짜 생성 관련 로직
    const select = document.getElementById('category-select');
    const content = document.getElementById('dateContent');
	
	document.addEventListener('DOMContentLoaded',function(){
    	select.addEventListener('change', function() {
        const selectedValue = select.value;

        // 날짜 입력 필드 생성
        if (selectedValue == '1') {
            content.innerHTML = `
                <input type="date" style="width:200px;" id="startDate" name="start_date"> - 
                <select style="margin-left:10px; width:150px;" name="time_period"> 
                    <option>선택하세요</option>
                    <option>오전</option>
                    <option>오후</option>
                </select>
                <input type="number" step="0.1" id="totalDay" name="totalDay" style="width:50px;">`;
        } else {
            content.innerHTML = `
                <input type="date" style="width:200px;" id="startDate" name="start_date"> - 
                <input type="date" style="width:200px;" id="endDate" name="end_date"> 
                <input type="number" id="totalDay" step="0.1" name="totalDay" style="width:50px;">`;
        }

        // 날짜 선택 이벤트 리스너 추가
        setupDateListeners();
    });

    function setupDateListeners() {
        const start = document.getElementById('startDate');
        const end = document.getElementById('endDate');

        if (start) {
            start.removeEventListener('change', calculateWorkDays);
            start.addEventListener('change', calculateWorkDays);
        }
        if (end) {
            end.removeEventListener('change', calculateWorkDays);
            end.addEventListener('change', calculateWorkDays);
        }
    }

    function calculateWorkDays() {
        const startDateValue = document.getElementById('startDate').value;
        const endDateValue = document.getElementById('endDate') ? document.getElementById('endDate').value : null;

        if (!startDateValue) {
            document.getElementById('totalDay').value = '';
            return;
        }

        const selectedValue = select.value;
        if (selectedValue == '1') {
            document.getElementById('totalDay').value = '0.5';
            return;
        }

        const startDate = new Date(startDateValue);
        const endDate = endDateValue ? new Date(endDateValue) : new Date();

        if (endDateValue && startDate > endDate) {
			Swal.fire({
             icon: 'info',
             title: '실패',
             text: '종료일이 시작일보다 빠릅니다.'
         });
            
        document.getElementById('endDate').value = '';
        return;
    	}	
		
		if (selectedValue == '3' || selectedValue == '4') {
		            document.getElementById('totalDay').value = '0';
		            return;
		}

        const year = startDate.getFullYear();
        const holidays = new Set();

        fetch(`https://date.nager.at/api/v2/PublicHolidays/${year}/KR`)
            .then(response => response.json())
            .then(data => {
                data.forEach(item => holidays.add(item.date));
                const annualDays = countWorkDays(startDate, endDate, holidays);
                document.getElementById('totalDay').value = annualDays;
            });
    }

    function countWorkDays(start, end, holidays) {
        let totalDays = 0;
        let currentDate = new Date(start);

        while (currentDate <= end) {
            const formattedDate = currentDate.toISOString().split('T')[0];
            const isWeekend = currentDate.getDay() === 0 || currentDate.getDay() === 6;

            if (!isWeekend && !holidays.has(formattedDate)) {
                totalDays++;
            }

            currentDate.setDate(currentDate.getDate() + 1);
        }
        return totalDays;
    };
});
	
   
