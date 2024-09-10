//package com.codenal.employee.controller;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.codenal.employee.domain.Employee;
//import com.codenal.employee.service.EmployeeService;
//
//@Controller
//public class EmployeeController {
//
//    private final EmployeeService employeeService;
//
//    public EmployeeController(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//    }
//
//    @GetMapping("/mypage")
//    public String showMyPage(Model model, Authentication authentication) {
//        // 로그인한 사용자의 ID를 가져온다고 가정 (이 예에서는 인증 객체에서 사용자 ID를 얻음)
//        int empId = Integer.parseInt(authentication.getName());
//
//        // 서비스에서 직원 정보 조회
//        Employee employee = employeeService.getEmployeeById(empId);
//
//        // 모델에 직원 정보 추가
//        model.addAttribute("employee", employee);
//
//        return "pages/mypage";  
//    }
//   
//    @PostMapping("/mypage/updateProfile")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> updateProfile(
//                                @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
//                                @RequestParam(value = "empName", required = false) String empName,
//                                @RequestParam(value = "empPhone", required = false) String empPhone,
//                                @RequestParam(value = "empAddress", required = false) String empAddress,
//                                @RequestParam(value = "empAddressDetail", required = false) String empAddressDetail,
//                                @RequestParam(value = "signatureImage",required = false) String signatureImage,
//                                Authentication authentication) {
//        int empId = Integer.parseInt(authentication.getName());
//        Map<String, Object> response = new HashMap<>();
//        
//        try {
//            // 직원 정보 가져오기
//            Employee employee = employeeService.getEmployeeById(empId);
//            
//            // 프로필 이미지 처리
//            if (profileImage != null && !profileImage.isEmpty()) {
//                String uploadDir = "uploads/profileImages/";
//                String fileName = profileImage.getOriginalFilename();
//                Path uploadPath = Paths.get(uploadDir);
//
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//
//                Path filePath = uploadPath.resolve(fileName);
//                Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                String fileDownloadUri = "/" + uploadDir + fileName;
//                employee.setEmpProfilePicture(fileDownloadUri);
//            }
//
//            // 서명 이미지 처리
//            if (signatureImage != null && !signatureImage.isEmpty()) {
//                try {
//                    // Base64 디코딩
//                    String base64Image = signatureImage.split(",")[1];
//                    byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Image);
//
//                    String signFileName = "signature_" + empId + ".png";
//                    String signUploadDir = "uploads/signatures/";
//                    Path signUploadPath = Paths.get(signUploadDir);
//
//                    if (!Files.exists(signUploadPath)) {
//                        Files.createDirectories(signUploadPath);
//                    }
//
//                    Path signFilePath = signUploadPath.resolve(signFileName);
//                    Files.write(signFilePath, decodedBytes);
//
//                    String signFileDownloadUri = "/" + signUploadDir + signFileName;
//                    employee.setEmpSignImage(signFileDownloadUri);
//
//                    response.put("filePath", signFileDownloadUri);
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                    response.put("success", false);
//                    response.put("error", "Invalid Base64 format for signature image.");
//                    return ResponseEntity.status(500).body(response);
//                }
//            }
//
//            employee.setEmpName(empName);
//            employee.setEmpPhone(empPhone);
//            employee.setEmpAddress(empAddress);
//            employee.setEmpAddressDetail(empAddressDetail);
//
//            employeeService.saveEmployee(employee);
//
//            response.put("success", true);
//            return ResponseEntity.ok(response);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.put("success", false);
//            response.put("error", "Failed to save profile or signature image.");
//            return ResponseEntity.status(500).body(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("success", false);
//            response.put("error", "Unexpected error occurred.");	
//            return ResponseEntity.status(500).body(response);
//        }
//    }
//}