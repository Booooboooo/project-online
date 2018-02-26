package com.nais.kid.keeper.controller;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.nais.constant.ResourceType;
import com.nais.helper.EncryptionUtil;
import com.nais.kid.keeper.helper.IOHelper;
import com.nais.response.GeneralResponse;

@Controller
@RequestMapping("file")
public class FileUploadController {
	
	private static ResourceBundle FILE_BUNDLE = ResourceBundle.getBundle("file");
	
	private static final String LOCAL_FILE_PATH_ROOT = FILE_BUNDLE.getString("local.file.path.root");
	
	private static final String IMG_SUFFIX = FILE_BUNDLE.getString("img.suffix");
	
	private static final String HTML_SUFFIX = FILE_BUNDLE.getString("html.suffix");
	
	private static final String RESOURCE_DOMIN = FILE_BUNDLE.getString("nginx.domin.name");
	
	private static final String FILE_NAME_ENC_SIGN = FILE_BUNDLE.getString("file.name.enc.sign");
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody GeneralResponse<String> upload(@RequestParam("fileCat")String fileCat, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   
        MultipartFile file = multipartRequest.getFile(fileCat);   
		return uploadHttpFile(file, request);
    }
	
	private GeneralResponse<String> uploadHttpFile(MultipartFile file, HttpServletRequest request) {
		
		if (file != null && !file.isEmpty()) {
            try {
                
            	String fileName = file.getOriginalFilename();
            	
            	//获取文件夹名
            	String folderName = getFolderWithFileName(fileName);
            	
            	//本地存储路径
                String fileDirPath = LOCAL_FILE_PATH_ROOT  + folderName;
                
                String systemFileName = EncryptionUtil.getToBeSignedStr(FILE_NAME_ENC_SIGN, fileName, String.valueOf(new Date().getTime())) + "." +fileName.substring(fileName.lastIndexOf(".") + 1);
                
                //网络访问路径
                String url = RESOURCE_DOMIN + folderName + systemFileName;
                
                IOHelper.uploadFile(file.getBytes(), fileDirPath, systemFileName);
                
                return GeneralResponse.success(url);
            } catch (Exception e) {
            	
            	logger.error("文件上传失败",e);
            	return GeneralResponse.failure("上传失败," + e.getMessage(), "上传出现异常");
            } 
        } else {
        	return GeneralResponse.failure("上传失败，因为文件是空的", "文件不能为空");
        }
	}
	
	private String getFolderWithFileName(String fileName) {
		
		try {
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (StringUtils.isNotBlank(prefix)) {
				
				if (StringUtils.containsIgnoreCase(IMG_SUFFIX, prefix)) {
					return ResourceType.IMAGE.getTag() + "/";
				}
				
				if (StringUtils.containsIgnoreCase(HTML_SUFFIX, prefix)) {
					return ResourceType.HTML.getTag()  + "/";
				}
				
			}
		} catch(Exception e) {
			logger.info("cannot get file type", fileName, e);
		}
		
		return ResourceType.IMAGE.getTag()  + "/";
	}
	
}
