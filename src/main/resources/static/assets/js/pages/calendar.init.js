/*
Template Name: Velzon - Admin & Dashboard Template
Author: Themesbrand
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Calendar init js
*/


var start_date = document.getElementById("event-start-date");
var timepicker1 = document.getElementById("timepicker1");
var timepicker2 = document.getElementById("timepicker2");
var date_range = null;
var T_check = null;
document.addEventListener("DOMContentLoaded", function () {
    flatPickrInit();
    var addEvent = new bootstrap.Modal(document.getElementById('event-modal'), {
        keyboard: false
    });
    document.getElementById('event-modal');
    var modalTitle = document.getElementById('modal-title');
    var formEvent = document.getElementById('form-event');
    var selectedEvent = null;
    var forms = document.getElementsByClassName('needs-validation');
    /* initialize the calendar */

    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    var Draggable = FullCalendar.Draggable;
    var externalEventContainerEl = document.getElementById('external-events');
	var defaultEvents = [];
	
	const dataList=(result)=>{
		
	}
    fetch('/eventList',{
	        	method:'POST',
	            headers: {
					"Content-Type": "application/json;charset=utf-8", 
					"Accept": "application/json"
				}
	        })
	        .then(response => response.json())
	        .then(data=>{
	        	/*alert(data.res_msg);*/
	        	console.log(data.eventList[0]);
	        	dataList = data.eventList;
	        	console.log(dataList);
	        	console.log(dataList[0].calendar_schedule_no);
	        })
	console.log(dataList);
	
    /*for(let i=0; i<1; i++){
		defaultEvents[i] = {
					id: dataList[0].calendar_schedule_no,
					category: dataList[i].calendar_schedule_category,
			        title: dataList[i].calendar_schedule_title,
			        start: '2024-09-16',
			        end: '2024-09-18',
			        className: 'bg-soft-primary',
			        location: dataList[i].calendar_schedule_location,
			        description: dataList[i].calendar_schedule_content,
			        writer: dataList[i].calendar_schedule_writer,
			        color: dataList[i].calendar_schedule_color
				}
	};*/

    // init draggable
    new Draggable(externalEventContainerEl, {
        itemSelector: '.external-event',
        eventData: function (eventEl) {
            return {
                id: Math.floor(Math.random() * 11000),
                title: eventEl.innerText,
                allDay: true,
                start: new Date(),
                className: eventEl.getAttribute('data-class')
            };
        }
    });

    var calendarEl = document.getElementById('calendar');

    function addNewEvent(info) {
        document.getElementById('form-event').reset();
        document.getElementById('btn-delete-event').setAttribute('hidden', true);
        addEvent.show();
        formEvent.classList.remove("was-validated");
        formEvent.reset();
        selectedEvent = null;
        modalTitle.innerText = '일정 추가';
        newEventData = info;
        document.getElementById("edit-event-btn").setAttribute("data-id", "new-event");
        document.getElementById('edit-event-btn').click();
        document.getElementById("edit-event-btn").setAttribute("hidden", true);
    }

    function getInitialView() {
        if (window.innerWidth >= 768 && window.innerWidth < 1200) {
            return 'timeGridWeek';
        } else if (window.innerWidth <= 768) {
            return 'listMonth';
        } else {
            return 'dayGridMonth';
        }
    }

    var eventCategoryChoice = new Choices("#event-category", {
        searchEnabled: false
    });

    var calendar = new FullCalendar.Calendar(calendarEl, {
        timeZone: 'local',
        editable: true,
        droppable: true,
        selectable: true,
        navLinks: true,
        initialView: getInitialView(),
        themeSystem: 'bootstrap',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
        },
        windowResize: function (view) {
            var newView = getInitialView();
            calendar.changeView(newView);
        },
        eventResize: function(info) {
            var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                return x.id == info.event.id
            });
            if (defaultEvents[indexOfSelectedEvent]) {
				defaultEvents[indexOfSelectedEvent].category = info.event.category;
                defaultEvents[indexOfSelectedEvent].title = info.event.title;
                defaultEvents[indexOfSelectedEvent].start = info.event.start;
                defaultEvents[indexOfSelectedEvent].end = (info.event.end) ? info.event.end : null;
                defaultEvents[indexOfSelectedEvent].allDay = info.event.allDay;
                defaultEvents[indexOfSelectedEvent].className = info.event.classNames[0];
                defaultEvents[indexOfSelectedEvent].description = (info.event._def.extendedProps.description) ? info.event._def.extendedProps.description : '';
                defaultEvents[indexOfSelectedEvent].location = (info.event._def.extendedProps.location) ? info.event._def.extendedProps.location : '';
                defaultEvents[indexOfSelectedEvent].writer = info.event.writer;
                defaultEvents[indexOfSelectedEvent].color = info.event.color;
            }
            upcomingEvent(defaultEvents);
        },
        eventClick: function (info) {
            document.getElementById("edit-event-btn").removeAttribute("hidden");
            document.getElementById('btn-save-event').setAttribute("hidden", true);
            document.getElementById("edit-event-btn").setAttribute("data-id", "edit-event");
            document.getElementById("edit-event-btn").innerHTML = "수정";
            eventClicked();
            flatPickrInit();
            flatpicekrValueClear();
            addEvent.show();
            formEvent.reset();
            selectedEvent = info.event;

            // First Modal
            document.getElementById("modal-title").innerHTML = "";
            document.getElementById("event-location-tag").innerHTML = selectedEvent.extendedProps.location === undefined ? "장소 없음" : selectedEvent.extendedProps.location;
            document.getElementById("event-description-tag").innerHTML = selectedEvent.extendedProps.description === undefined ? "내용 없음" : selectedEvent.extendedProps.description;

            // Edit Modal
            document.getElementById("event-title").value = selectedEvent.title;
            document.getElementById("event-location").value = selectedEvent.extendedProps.location === undefined ? "장소 없음" : selectedEvent.extendedProps.location;
            document.getElementById("event-description").value = selectedEvent.extendedProps.description === undefined ? "내용 없음" : selectedEvent.extendedProps.description;
            document.getElementById("eventid").value = selectedEvent.id;

            if (selectedEvent.classNames[0]) {
                eventCategoryChoice.destroy();
                eventCategoryChoice = new Choices("#event-category", {
                    searchEnabled: false
                });
                eventCategoryChoice.setChoiceByValue(selectedEvent.classNames[0]);
            }
            var st_date = selectedEvent.start;
            var ed_date = selectedEvent.end;

            var date_r = function formatDate(date) {
                var d = new Date(date),
                    month = '' + (d.getMonth() + 1),
                    day = '' + d.getDate(),
                    year = d.getFullYear();
                if (month.length < 2)
                    month = '0' + month;
                if (day.length < 2)
                    day = '0' + day;
                return [year, month, day].join('-');
            };
            var updateDay = null
            if(ed_date != null){
                var endUpdateDay = new Date(ed_date);
                updateDay = endUpdateDay.setDate(endUpdateDay.getDate() - 1);
            }
            
            var r_date = ed_date == null ? (str_dt(st_date)) : (str_dt(st_date)) + ' to ' + (str_dt(updateDay));
            var er_date = ed_date == null ? (date_r(st_date)) : (date_r(st_date)) + ' to ' + (date_r(updateDay));

            flatpickr(start_date, {
                defaultDate: er_date,
                altInput: true,
                altFormat: "j F Y",
                dateFormat: "Y-m-d",
                mode: ed_date !== null ? "range" : "range",
                onChange: function (selectedDates, dateStr, instance) {
                    var date_range = dateStr;
                    var dates = date_range.split("to");
                    if (dates.length > 1) {
                        document.getElementById('event-time').setAttribute("hidden", true);
                    } else {
                        document.getElementById("timepicker1").parentNode.classList.remove("d-none");
                        document.getElementById("timepicker1").classList.replace("d-none", "d-block");
                        document.getElementById("timepicker2").parentNode.classList.remove("d-none");
                        document.getElementById("timepicker2").classList.replace("d-none", "d-block");
                        document.getElementById('event-time').removeAttribute("hidden");
                    }
                },
            });
            document.getElementById("event-start-date-tag").innerHTML = r_date;

            var gt_time = getTime(selectedEvent.start);
            var ed_time = getTime(selectedEvent.end);

            if (gt_time == ed_time) {
                document.getElementById('event-time').setAttribute("hidden", true);
                flatpickr(document.getElementById("timepicker1"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                });
                flatpickr(document.getElementById("timepicker2"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                });
            } else {
                document.getElementById('event-time').removeAttribute("hidden");
                flatpickr(document.getElementById("timepicker1"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                    defaultDate: gt_time
                });

                flatpickr(document.getElementById("timepicker2"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                    defaultDate: ed_time
                });
                document.getElementById("event-timepicker1-tag").innerHTML = tConvert(gt_time);
                document.getElementById("event-timepicker2-tag").innerHTML = tConvert(ed_time);
            }
            newEventData = null;
            modalTitle.innerText = selectedEvent.title;

            // formEvent.classList.add("view-event");
            document.getElementById('btn-delete-event').removeAttribute('hidden');
        },
        dateClick: function (info) {
            addNewEvent(info);
        },
        events: defaultEvents,
        eventReceive: function (info) {
            var newid = parseInt(info.event.id);
            var newEvent = {
                id: newid,
                title: info.event.title,
                start: info.event.start,
                allDay: info.event.allDay,
                className: info.event.classNames[0]
            };
            defaultEvents.push(newEvent);
            upcomingEvent(defaultEvents);
        },
        eventDrop: function (info) {
            var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                return x.id == info.event.id
            });
            if (defaultEvents[indexOfSelectedEvent]) {
				defaultEvents[indexOfSelectedEvent].category = info.event.category;
                defaultEvents[indexOfSelectedEvent].title = info.event.title;
                defaultEvents[indexOfSelectedEvent].start = info.event.start;
                defaultEvents[indexOfSelectedEvent].end = (info.event.end) ? info.event.end : null;
                defaultEvents[indexOfSelectedEvent].allDay = info.event.allDay;
                defaultEvents[indexOfSelectedEvent].className = info.event.classNames[0];
                defaultEvents[indexOfSelectedEvent].description = (info.event._def.extendedProps.description) ? info.event._def.extendedProps.description : '';
                defaultEvents[indexOfSelectedEvent].location = (info.event._def.extendedProps.location) ? info.event._def.extendedProps.location : '';
                defaultEvents[indexOfSelectedEvent].writer = info.event.writer;
                defaultEvents[indexOfSelectedEvent].color = info.event.color;
            }
            upcomingEvent(defaultEvents);
        }
    });

    calendar.render();

    upcomingEvent(defaultEvents);
    /*Add new event*/
    // Form to add new event
    formEvent.addEventListener('submit', function (ev) {
        ev.preventDefault();
        
        var updatedTitle = document.getElementById("event-title").value;
        var updatedCategory = document.getElementById('event-category').value;
        
        var categoryNo = 0;
        switch(updatedCategory){
			case 'bg-soft-success' : categoryNo = 1; break; /*개인일정*/
			case 'bg-soft-primary' : categoryNo = 2; break; /*부서일정*/
			case 'bg-soft-info' : categoryNo = 3; break; /*전체일정*/
		}
		
        var start_date = (document.getElementById("event-start-date").value).split("to");
        var updateStartDate = new Date(start_date[0].trim());

        var newdate = new Date(start_date[1]);
        newdate.setDate(newdate.getDate() + 1);

        var updateEndDate = (start_date[1]) ? newdate : '';

        var end_date = null;
        var event_location = document.getElementById("event-location").value;
        var eventDescription = document.getElementById("event-description").value;
        var eventid = document.getElementById("eventid").value;
        var all_day = false;
        if (start_date.length > 1) {
            var end_date = new Date(start_date[1]);
            end_date.setDate(end_date.getDate() + 1);
            start_date = new Date(start_date[0]);
            all_day = true;
        } else {
            var e_date = start_date;
            var start_time = (document.getElementById("timepicker1").value).trim();
            var end_time = (document.getElementById("timepicker2").value).trim();
            start_date = new Date(start_date + "T" + start_time);
            end_date = new Date(e_date + "T" + end_time);
        }
        var e_id = defaultEvents.length + 1;
         
         	
        	/*const payload = new FormData();
        	payload.append('calendar_schedule_category' , categoryNo);
        	payload.append('calendar_schedule_title' , updatedTitle);
        	payload.append('calendar_schedule_location' , event_location);
        	payload.append('calendar_schedule_content' , eventDescription);
        	payload.append('calendar_schedule_writer' , 1);
        	payload.append('calendar_schedule_start_date' , updateStartDate);
        	payload.append('calendar_schedule_end_date' , updateEndDate);
	    	payload;*/
	    	
	        const payload = {
				calendar_schedule_category : categoryNo,
				calendar_schedule_title : updatedTitle,
				calendar_schedule_location : event_location,
				calendar_schedule_content : eventDescription,
				calendar_schedule_writer : 1,
				calendar_schedule_start_date : updateStartDate,
				calendar_schedule_end_date : updateEndDate
			}
	    	console.log(payload);
	    	
	    	
            // JSON 객체 생성
            /*let obj = {
                calendar_schedule_title: updatedTitle
            };
            let jsonData = JSON.stringify(obj);
            // fetch를 사용하여 서버에 POST 요청 보내기
            fetch('/create/event', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                alert('전송 성공: ' + JSON.stringify(data));
            })
            .catch(error => {
                console.error('There was a problem with your fetch operation:', error);
                alert('전송 실패: ' + error.message);
            });*/
        
	    	
			/*const data = {
				method:'POST',
	            body:payload
			}*/
			
			const jsonData = JSON.stringify(updatedTitle);
			const ttt = "yyy";
	        fetch('/create/event',{
	        	method:'POST',
	            body:payload,
	            headers: {
					"Content-Type": "application/json;charset=utf-8", 
					"Accept": "application/json"
				}
	        })
	        .then(response => response.json())
	        .then(data=>{
	        	alert(data.res_msg);
	        })
	        
	        
				/*const xhr = new XMLHttpRequest();
				xhr.open("post","/create/event",true);
				xhr.onreadystatechange = function() {
					if(xhr.readyState == 4 && xhr.status == 200){ // 200: 정상적으로 작동한다는 뜻
						alert('성공');
					}
				}
				let writer = 12345678;
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");*/
				/*xhr.send("category="+categoryNo+"& title="+updatedTitle+"& location="+event_location
				+"& content="+eventDescription+"& writer="+writer+"& start_date="+updateStartDate+"& end_date="+updateEndDate);*/
				/*xhr.send(JSON.stringify(payload));*/

        // validation
        if (forms[0].checkValidity() === false) {
            forms[0].classList.add('was-validated');
        } else {
            if (selectedEvent) {
                selectedEvent.setProp("id", eventid);
                selectedEvent.setProp("category", categoryNo);
                selectedEvent.setProp("title", updatedTitle);
                selectedEvent.setProp("classNames", [updatedCategory]);
                selectedEvent.setStart(updateStartDate);
                selectedEvent.setEnd(updateEndDate);
                selectedEvent.setAllDay(all_day);
                selectedEvent.setExtendedProp("description", eventDescription);
                selectedEvent.setExtendedProp("location", event_location);
                var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                    return x.id == selectedEvent.id
                });
                if (defaultEvents[indexOfSelectedEvent]) {
                    defaultEvents[indexOfSelectedEvent].title = updatedTitle;
                    defaultEvents[indexOfSelectedEvent].start = updateStartDate;
                    defaultEvents[indexOfSelectedEvent].end = updateEndDate;
                    defaultEvents[indexOfSelectedEvent].allDay = all_day;
                    defaultEvents[indexOfSelectedEvent].className = updatedCategory;
                    defaultEvents[indexOfSelectedEvent].description = eventDescription;
                    defaultEvents[indexOfSelectedEvent].location = event_location;
                }
                calendar.render();
                // default
            } else {
                var newEvent = {
                    id: e_id,
                    title: updatedTitle,
                    start: start_date,
                    end: end_date,
                    allDay: all_day,
                    className: updatedCategory,
                    description: eventDescription,
                    location: event_location
                };
                calendar.addEvent(newEvent);
                defaultEvents.push(newEvent);
            }
            addEvent.hide();
            upcomingEvent(defaultEvents);
        }
    });

    document.getElementById("btn-delete-event").addEventListener("click", function (e) {
        if (selectedEvent) {
            for (var i = 0; i < defaultEvents.length; i++) {
                if (defaultEvents[i].id == selectedEvent.id) {
                    defaultEvents.splice(i, 1);
                    i--;
                }
            }
            upcomingEvent(defaultEvents);
            selectedEvent.remove();
            selectedEvent = null;
            addEvent.hide();
        }
    });
    document.getElementById("btn-new-event").addEventListener("click", function (e) {
        flatpicekrValueClear();
        flatPickrInit();
        addNewEvent();
        document.getElementById("edit-event-btn").setAttribute("data-id", "new-event");
        document.getElementById('edit-event-btn').click();
        document.getElementById("edit-event-btn").setAttribute("hidden", true);
    });
});


function flatPickrInit() {
    var config = {
        enableTime: true,
        noCalendar: true,
    };
    var date_range = flatpickr(
        start_date, {
            enableTime: false,
            mode: "range",
            minDate: "today",
            onChange: function (selectedDates, dateStr, instance) {
                var date_range = dateStr;
                var dates = date_range.split("to");
                if (dates.length > 1) {
                    document.getElementById('event-time').setAttribute("hidden", true);
                } else {
                    document.getElementById("timepicker1").parentNode.classList.remove("d-none");
                    document.getElementById("timepicker1").classList.replace("d-none", "d-block");
                    document.getElementById("timepicker2").parentNode.classList.remove("d-none");
                    document.getElementById("timepicker2").classList.replace("d-none", "d-block");
                    document.getElementById('event-time').removeAttribute("hidden");
                }
            },
        });
    flatpickr(timepicker1, config);
    flatpickr(timepicker2, config);

}

function flatpicekrValueClear() {
    start_date.flatpickr().clear();
    timepicker1.flatpickr().clear();
    timepicker2.flatpickr().clear();
}


function eventClicked() {
    document.getElementById('form-event').classList.add("view-event");
    document.getElementById("event-title").classList.replace("d-block", "d-none");
    document.getElementById("event-category").classList.replace("d-block", "d-none");
    document.getElementById("event-start-date").parentNode.classList.add("d-none");
    document.getElementById("event-start-date").classList.replace("d-block", "d-none");
    document.getElementById('event-time').setAttribute("hidden", true);
    document.getElementById("timepicker1").parentNode.classList.add("d-none");
    document.getElementById("timepicker1").classList.replace("d-block", "d-none");
    document.getElementById("timepicker2").parentNode.classList.add("d-none");
    document.getElementById("timepicker2").classList.replace("d-block", "d-none");
    document.getElementById("event-location").classList.replace("d-block", "d-none");
    document.getElementById("event-description").classList.replace("d-block", "d-none");
    document.getElementById("event-start-date-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-timepicker1-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-timepicker2-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-location-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-description-tag").classList.replace("d-none", "d-block");
    document.getElementById('btn-save-event').setAttribute("hidden", true);
}

function editEvent(data) {
    var data_id = data.getAttribute("data-id");
    if (data_id == 'new-event') {
        document.getElementById('modal-title').innerHTML = "";
        document.getElementById('modal-title').innerHTML = "일정 추가";
        document.getElementById("btn-save-event").innerHTML = "일정 추가";
        eventTyped();
    } else if (data_id == 'edit-event') {
        data.innerHTML = "Cancel";
        data.setAttribute("data-id", 'cancel-event');
        document.getElementById("btn-save-event").innerHTML = "일정 수정";
        data.removeAttribute("hidden");
        eventTyped();
    } else {
        data.innerHTML = "수정";
        data.setAttribute("data-id", 'edit-event');
        eventClicked();
    }
}

function eventTyped() {
    document.getElementById('form-event').classList.remove("view-event");
    document.getElementById("event-title").classList.replace("d-none", "d-block");
    document.getElementById("event-category").classList.replace("d-none", "d-block");
    document.getElementById("event-start-date").parentNode.classList.remove("d-none");
    document.getElementById("event-start-date").classList.replace("d-none", "d-block");
    document.getElementById("timepicker1").parentNode.classList.remove("d-none");
    document.getElementById("timepicker1").classList.replace("d-none", "d-block");
    document.getElementById("timepicker2").parentNode.classList.remove("d-none");
    document.getElementById("timepicker2").classList.replace("d-none", "d-block");
    document.getElementById("event-location").classList.replace("d-none", "d-block");
    document.getElementById("event-description").classList.replace("d-none", "d-block");
    document.getElementById("event-start-date-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-timepicker1-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-timepicker2-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-location-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-description-tag").classList.replace("d-block", "d-none");
    document.getElementById('btn-save-event').removeAttribute("hidden");
}

// upcoming Event
function upcomingEvent(a) {
    a.sort(function (o1, o2) {
        return (new Date(o1.start)) - (new Date(o2.start));
    });
    document.getElementById("upcoming-event-list").innerHTML = null;
    Array.from(a).forEach(function (element) {
        var title = element.title;
        if (element.end) {
            endUpdatedDay = new Date(element.end);
            var updatedDay = endUpdatedDay.setDate(endUpdatedDay.getDate() - 1);
          }
        var e_dt = updatedDay ? updatedDay : undefined;
        if (e_dt == "Invalid Date" || e_dt == undefined) {
            e_dt = null;
        } else {
            const newDate = new Date(e_dt).toLocaleDateString('ko', { year: 'numeric', month: 'numeric', day: 'numeric' });
            e_dt = new Date(newDate)
              .toLocaleDateString("ko-KR", {
                day: "numeric",
                month: "short",
                year: "numeric",
              })
              .split(" ")
              .join(" ");
        }
        var st_date = element.start ? str_dt(element.start) : null;
        var ed_date = updatedDay ? str_dt(updatedDay) : null;
        if (st_date === ed_date) {
            e_dt = null;
        }
        var startDate = element.start;
        if (startDate === "Invalid Date" || startDate === undefined) {
            startDate = null;
        } else {
            const newDate = new Date(startDate).toLocaleDateString('ko', { year: 'numeric', month: 'numeric', day: 'numeric' });
            startDate = new Date(newDate)
              .toLocaleDateString("ko-KR", {
                day: "numeric",
                month: "short",
                year: "numeric",
              })
              .split(" ")
              .join(" ");
        }

        var end_dt = (e_dt) ? " ~ " + e_dt : '';
        var category = (element.className).split("-");
        var description = (element.description) ? element.description : "";
        var e_time_s = tConvert(getTime(element.start));
        var e_time_e = tConvert(getTime(updatedDay));
        if (e_time_s == e_time_e) {
            var e_time_s = "Full day event";
            var e_time_e = null;
        }
        var e_time_e = (e_time_e) ? " ~ " + e_time_e : "";
        let nowDate = new Date();
        nowDate = nowDate.getFullYear()+'-'+(nowDate.getMonth()+1)+'-'+nowDate.getDate();
        let toDay = new Date(nowDate);
		if(e_dt == null && element.start >= toDay || e_dt != null && element.end >= toDay){
			
        u_event = "<div class='card mb-3'>\
                        <div class='card-body'>\
                            <div class='d-flex mb-3'>\
                                <div class='flex-grow-1'><i class='mdi mdi-checkbox-blank-circle me-2 text-" + category[2] + "'></i><span class='fw-medium'>" + startDate + end_dt + " </span></div>\
                                <div class='flex-shrink-0'><small class='badge badge-soft-primary ms-auto'>" + e_time_s + e_time_e + "</small></div>\
                            </div>\
                            <h6 class='card-title fs-16'> " + title + "</h6>\
                            <p class='text-muted text-truncate-two-lines mb-0'> " + description + "</p>\
                        </div>\
                    </div>";
        document.getElementById("upcoming-event-list").innerHTML += u_event;
		}
    });
};

function getTime(params) {
    params = new Date(params);
    if (params.getHours() != null) {
        var hour = params.getHours();
        var minute = (params.getMinutes()) ? params.getMinutes() : 0;
        return hour + ":" + minute;
    }
}

function tConvert(time) {
    var t = time.split(":");
    var hours = t[0];
    var minutes = t[1];
    var newformat = hours >= 12 ? '오전' : '오후';
    hours = hours % 12;
    hours = hours ? hours : 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return (newformat + ' ' + hours + ':' + minutes);
}

var str_dt = function formatDate(date) {
    var monthNames = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
    var d = new Date(date),
        month = '' + monthNames[(d.getMonth())],
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    return [day + " " + month, year].join(',');
};