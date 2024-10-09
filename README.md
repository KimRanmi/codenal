# WithXWork 그룹웨어 사이트
![스크린샷 2024-10-10 020547](https://github.com/user-attachments/assets/d47064ac-ae02-446a-aa68-38d2a6b2a08d)

## 프로젝트 소개
* 위드X워크는 회사 내 직원들이 원활하게 협업할 수 있도록 설계된 그룹웨어 사이트입니다.
* 직원들은 개인 업무에 도움될 뿐만 아니라 팀원들과 소통할 수 있는 다양한 서비스들을 이용할 수 있습니다.
* 관리자는 직원들의 원활한 업무를 위해 서비스 설정에 권한을 가지고, 전 직원들을 관리할 수 있습니다.

<br>

## 팀원 구성
```
Give examples
```
<br>

## 📺 개발환경
![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![](https://img.shields.io/badge/HTML-239120?style=for-the-badge&logo=html5&logoColor=white)
![](https://img.shields.io/badge/CSS-239120?&style=for-the-badge&logo=css3&logoColor=white)
![](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)
![](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![](https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white)
<br>

## 프로젝트 기간
![프로젝트 기간](https://github.com/user-attachments/assets/3a18540f-930f-4c3f-b2d6-5b0d01d91e07)
<br>
<br>

## 주요 기능
* 로그인/ 마이페이지
* 캘린더
* 문서함
* 전자결재 / 관리
* 주소록
* 채팅
* 근태 관리
* 회의실 관리/ 예약
* 직원 관리
* 부서 관리
* 알림
<br>

## DB 설계
![image](https://github.com/user-attachments/assets/668b066d-b9c2-47f3-80da-a8b06f9cac28)
<br>
<br> 


# 기능 구현 (직원)
## [로그인]
![로그인](https://github.com/user-attachments/assets/c22b7b4d-8779-4d2f-a3e7-6e02cd924fcd)
* 실행 시 처음 보이는 로그인 페이지입니다.
* ID 저장과 로그인 유지 기능을 사용할 수 있습니다.
<br>
  
## [메인 페이지]
![메인페이지](https://github.com/user-attachments/assets/5b6a1f54-771d-4e31-9eab-1219ce438299)
* 로그인을 하면 나오는 메인 페이지 입니다. 
<br>

## [마이 페이지]
![마이페이지1](https://github.com/user-attachments/assets/2d0d6427-30e0-4c0f-92dc-466921aaa4cb)
* 개인 정보 페이지이며 간단한 정보 수정을 할 수 있습니다.
* 또한, 서명이미지를 캔버스를 활용해 첨부가 가능합니다.
<br>

![마이페이지2](https://github.com/user-attachments/assets/6a334e08-d2dd-4b52-acfe-cf1d25469710)
* 비밀번호 변경도 가능합니다.
<br>

## [캘린더]
![20241010_000608406](https://github.com/user-attachments/assets/a987f395-b795-4d02-8425-1454a190c82b)
* 캘린더에서는 크게 전체, 부서, 개인으로 나누어 일정을 등록할 수 있습니다.
* 또한, 일정 수정과 삭제를 할 수 있습니다.
<br>

![캘린더 2](https://github.com/user-attachments/assets/543e2bb5-981d-42c9-a338-578b34806b52)
* 등록한 일정은 드래그를 통해 이동할 수 있으며, 월*주*일 단위로 확인할 수 있습니다.
* 메인 페이지에 있는 캘린더를 통해 일정을 확인할 수도 이동할 수도 있습니다.
<br>
  
## [문서함]
![문서함1](https://github.com/user-attachments/assets/61705d2a-1ba6-4895-b59b-8ba6f8280bf2)
* 파일을 등록할 수 있는 문서함입니다.
* 등록한 파일을 삭제하면 휴지통으로 이동합니다.
* 휴지통으로 이동한 파일은 복원이 가능하며, 완전히 삭제할 수도 있습니다.
<br>

![문서함2](https://github.com/user-attachments/assets/03c75d7d-7b93-41b6-9882-3f0cac7c74a1)
* 파일은 다운로드가 가능합니다.
* 또한, 우측 아이콘을 누르면 즐겨찾기가 됩니다.
<br>

![문서함3](https://github.com/user-attachments/assets/15ede557-4d26-4616-b805-2285b1f99270)
* 파일 등록 시 공유할 상대를 지정할 수 있고 이러한 파일은 공유 문서함으로 이동합니다.
* 공유한 직원은 공유한 문서함에 공유 받은 직원은 공유 받은 문서함으로 조회됩니다.
<br>

## [전자결재]
전자결재 시스템은 근태신청서, 요청서, 품의서를 기안할 수 있습니다.

<br>

![전자결재1](https://github.com/user-attachments/assets/a2a0c39d-5f13-404d-85cb-6d1b18503790)
* 먼저 근태 신청서는 연차와 반차는 사용할 총 일수가 표시되며, 이때 공휴일과 주말은 제외되어 계산됩니다.
* 만약 연차와 반차를 신청하는 날보다 보유한 연차가 부족하다면 "연차 부족" alert이 생성됩니다.
* 필수 항목을 빠짐없이 입력했다면 기안함으로 이동합니다.

<br>

![전자결재2](https://github.com/user-attachments/assets/f083c596-7163-4099-9efc-422bec520c99)
* 모든 기안서는 관리자가 설정한 양식에 따라 제출할 수 있는 내용이 달라집니다.
<br>

![전자결재3](https://github.com/user-attachments/assets/bb10d253-46f7-4e3b-82f2-7442d20d2ec4)
* 결재자가 반려버튼을 누를 경우 반려 사유를 작성할 수 있습니다.
* 상신자는 알림을 통해 반려 사실을 확인할 수 있으며, 반려 버튼 클릭 시 반려 사유를 확인할 수 있습니다.
<br>

![전자결재 알림](https://github.com/user-attachments/assets/f8d34122-af95-45c1-a3d2-65efc8064477)
* 결재해야 할 사람과 다음 결재자 그리고 마지막 결재자인 상신자에게 현 결재 상황에 대한 실시간 알림이 전송됩니다.
<br>

## [주소록]
![주소록](https://github.com/user-attachments/assets/a6f74d81-1a68-445c-9aa6-5178498ed418)
* 회사 내의 조직도와 직원들의 연락망을 볼 수 있는 목록 페이지입니다.
<br>

## [채팅]

<br>

## [근태관리]
![근태관리1](https://github.com/user-attachments/assets/a32ab353-0c74-4181-8211-cc19b07f6624)
* 기간 별로 혹은 월 별로 출퇴근 시간과 근무 시간, 근무 상태를 조회할 수 있습니다.
<br>

![근태관리2](https://github.com/user-attachments/assets/ded53b0a-974c-4837-ab2c-65c0fba7cfc2)
* 메인페이지에 있는 출퇴근 버튼을 누르면 출퇴근 관리에 반영됩니다.
* 근무내역 관리에서는 날짜별 초과 근무 시간, 총 근무시간을 조회할 수 있습니다.
<br>

![근태관리3](https://github.com/user-attachments/assets/4121912b-d96d-4e6a-8ba9-77f2a81c3190)
* 근태 신청서가 승인되면 근태관리에서 자신의 연차 정보를 확인할 수 있습니다.
<br>

## [게시판]

<br>

## [회의실 예약]
![회의실 예약1](https://github.com/user-attachments/assets/8c2d134f-1cca-440c-a630-f3c3a2a507a1)
* 관리자가 설정해 놓은 회의실을 직원이 예약할 수 있습니다.
* 원하는 회의실 장소와 시간대를 설정하여 예약을 합니다.
<br>

![회의실 예약2](https://github.com/user-attachments/assets/d57ba1d1-4537-4137-8790-ffa28f732c10)
* 예약 내역에서 시간대를 변경할 수도 예약 취소도 가능합니다.
<br>



# 기능구현 (관리자)
## [직원 관리]
![신규직원](https://github.com/user-attachments/assets/1625c38f-69ac-4661-8326-93b1f16ce6cf)
* 관리자 신규 직원을 등록하면 직원은 로그인 계정이 생성됩니다.
<br>

![직원 상세](https://github.com/user-attachments/assets/2d2f5982-000a-45cd-8590-1680d58406ff)
* 목록에서 특정 직원을 선택하면 상세 정보 페이지가 나옵니다.
* 임시 비밀번호 발급을 통해 기본 비밀번호 'work1234'로 변경됩니다.
* 수정 버튼을 통해 직원의 이름, 부서, 직급을 수정할 수 있습니다.
<br>

![퇴사관리](https://github.com/user-attachments/assets/cc28c0bd-db31-451b-9177-8e78f08a5979)
* 퇴사 관리 탭을 누르면 직원을 퇴사 처리할 수 있습니다.
* 퇴사일은 입사일 이전 날짜로 선택할 수 없도록 설정했습니다.
<br>

## [부서 관리]
![부서관리](https://github.com/user-attachments/assets/f69bd916-b1cd-48ab-ab5a-d2fa74f210df)
* 부서를 추가하거나 삭제하고, 부서명 수정이 가능한 페이지입니다.
* 부서를 삭제할 때는 부서 내에 직원이 한 명도 없어야 가능합니다.
<br>

## [회의실 관리]
![회의실 관리1](https://github.com/user-attachments/assets/1adb7f15-5d1a-4f6a-b6ba-da841fd62b43)
* 직원들이 예약할 수 있도록 회의실 장소와 시간대를 지정하여 추가합니다.
<br>

![회의실 관리2](https://github.com/user-attachments/assets/b610742d-fc21-4160-ae50-7acb23370d4a)
* 이미 등록한 회의실 정보를 수정하거나 삭제할 수 있습니다.

## [전자결재  관리]

<br>
