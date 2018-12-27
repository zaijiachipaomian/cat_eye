package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.entity.Photo;

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
        
        //String path = "F:/test" ;
        
        String path = System.getProperty("user.dir");
        File dest = new File(path + "/images/" + fileName);
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
    /**
     * 当上传失败时返回null，否则返回一个List，里面是各个上传文件的路径
     * @param files
     * @return
     */
    public static List<String> multifileUpload(List<MultipartFile> files) {
    	ArrayList<String> urlList = new ArrayList<>();
        if(files.isEmpty()){
            return urlList;
        }
        String path = System.getProperty("user.dir");
        for(MultipartFile file:files){
            int fileType = file.getOriginalFilename().lastIndexOf(".");
            String fileTypeStr = file.getOriginalFilename().substring(fileType, file.getOriginalFilename().length());
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 9).toLowerCase() + fileTypeStr;
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);
            if(file.isEmpty()){
                return null;
            }else{        
            	 File dest = new File(path + "/images/" + fileName);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                    urlList.add(dest.getAbsolutePath());
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                } 
            }
        }
        return urlList;
    }
}
