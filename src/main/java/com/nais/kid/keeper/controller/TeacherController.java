package com.nais.kid.keeper.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.nais.response.GeneralResponse;
import com.nais.service.TeacherService;
import com.nais.vo.TeacherVo;

@Controller
public class TeacherController {
	
	@Autowired 
	private TeacherService teacherService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/teachers")
	@ResponseBody
	public GeneralResponse<TeacherVo> addTeacher(TeacherVo vo) {
		try {
			logger.info(JSON.toJSONString(vo));
		}catch(Exception e) {
			
		}
		return GeneralResponse.failure(null);
	}

	@GetMapping("/teacher")
	public String teacher() {
		return "teacher-add";
	}
}
