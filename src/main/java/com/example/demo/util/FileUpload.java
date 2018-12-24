package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
	
    public static String fileUpload(MultipartFile file){
        if(file.isEmpty()){
            return "false";
        }
        int fileType = file.getOriginalFilename().lastIndexOf(".");
        String fileTypeStr = file.getOriginalFilename().substring(fileType, file.getOriginalFilename().length());
        String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 9).toLowerCase() + fileTypeStr;
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);
        
        String path = "F:/test" ;
        File dest = new File(path + "/" + fileName);
        //System.out.println(dest.getAbsolutePath());
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return dest.getAbsolutePath();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }

}
