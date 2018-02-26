package com.nais.kid.keeper.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nais.response.GeneralResponse;
import com.nais.service.CourseService;
import com.nais.vo.CourseVo;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private @Autowired CourseService 			courseService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);
	
	
	@RequestMapping(value="", method= {RequestMethod.GET}) 
	@ResponseBody
	public GeneralResponse<List<CourseVo>> allCourses() {
		List<CourseVo> cs = null;
		try {
			cs = courseService.getAllCourse();
		}
		catch(Exception e){
			LOGGER.info("获取课程发生错误" + e);
			return GeneralResponse.failure(e.toString());
		}
		return GeneralResponse.success(cs);
	}
	
	@RequestMapping(value="", method= {RequestMethod.POST})
	@ResponseBody
	public GeneralResponse<Void> addCourse () {
		return GeneralResponse.failure("方法暂未实现");
	}
	
	@RequestMapping(value="{id}", method= {RequestMethod.DELETE})
	@ResponseBody
	public GeneralResponse<Void> delCourse(@PathVariable("id") Long id) {
		return GeneralResponse.failure("方法暂未实现");
	}
	
}
