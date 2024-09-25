package com.codenal.meeting.service;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MeetingRoomImgFileService {
	
	public String Img(MultipartFile img) {
		
		String newFileName = "";
		
		try {
			
			String fileOriName = img.getOriginalFilename();
			
			String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."),fileOriName.length());
			
			UUID uuid = UUID.randomUUID();
			
			String uniqueName = uuid.toString().replaceAll("-", "");
			
			newFileName = uniqueName+fileExtension;
			
//			String uploadDir = "src\\main\\resources\\static\\assets\\images";
			String uploadDir = "C:\\meetingRoomImg\\upload";
			
			File saveFile = new File(uploadDir+"\\"+uniqueName+fileExtension);
			
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			
			img.transferTo(saveFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newFileName;
		
	}

}
