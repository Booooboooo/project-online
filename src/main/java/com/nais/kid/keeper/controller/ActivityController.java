package com.nais.kid.keeper.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nais.constant.EduLevel;
import com.nais.domain.ViewTask;
import com.nais.helper.DateHelper;
import com.nais.response.GeneralResponse;
import com.nais.service.ViewTaskService;
import com.nais.vo.ActiveViewTaskVo;
import com.nais.vo.PageVo;

@Controller
@RequestMapping("activity")
public class ActivityController extends BaseController{
	
	@Autowired
	private ViewTaskService viewTaskService;
	
	@RequestMapping("list")
	public String entry() {
		return "activity/list";
	}
	
	@RequestMapping("delete.json")
	@ResponseBody
	public GeneralResponse<String> delete(@RequestParam(value="id", required=false, defaultValue="null")Long id){
		
		try {

			if (id == null) {
				return GeneralResponse.failure("必须删除指定活动！");
			}
			
			viewTaskService.deleteViewTask(id);
			return GeneralResponse.success("删除成功！");
		} catch (Exception e) {
			logger.info("删除活动失败！", e);
			return GeneralResponse.failure("删除活动失败！");
		}
		
	}
	
	@RequestMapping(value = "insert.json",method = {RequestMethod.POST})
	@ResponseBody
	public GeneralResponse<String> insert(ActiveViewTaskVo activeViewTaskVo){
		
		try {
		
			logger.info("creating active " + ToStringBuilder.reflectionToString(activeViewTaskVo));
			
			ViewTask task = new ViewTask();
			BeanUtils.copyProperties(task, activeViewTaskVo);
			
			Long id = viewTaskService.addActiveTask(task, 
						activeViewTaskVo.getTaskBriefResourceUrl(), 
						activeViewTaskVo.getTaskBriefResourceUrl(), 
						getOnlyUser().getId());
			if (id != null) {
				return GeneralResponse.success(String.format("活动%s已经成功创建！", id));
			}

		} catch (Exception e) {
			logger.info("创建活动失败！", e);
		}
		return GeneralResponse.failure("创建活动失败！","创建活动失败！");
		
	}
	
	
	@RequestMapping("query.json")
	@ResponseBody
	public PageVo<ActiveManageVo> queryList(
			@RequestParam(value="activeName", required=false, defaultValue="")String activeName, 
			@RequestParam(value="startTime", required=false, defaultValue="")@DateTimeFormat(pattern="yyyy-MM-dd")Date starTime, 
			@RequestParam(value="endTime", required=false, defaultValue="")@DateTimeFormat(pattern="yyyy-MM-dd")Date endTime, 
			@RequestParam(value="page", required=false, defaultValue="1")Integer page, 
			@RequestParam(value="rows", required=false, defaultValue="10")Integer rows){
		
		PageVo<ActiveManageVo> pageVoList = new PageVo<>();
		try {
			PageVo<ActiveViewTaskVo> activeTaskPage = viewTaskService.getActiveViewTaskVoByPage(activeName, starTime, endTime, page, rows);
			
			List<ActiveViewTaskVo> taskList = activeTaskPage.getRows();
			
			pageVoList.setPageCount(activeTaskPage.getPageCount());
			pageVoList.setPageNo(activeTaskPage.getPageNo());
			pageVoList.setPageSize(activeTaskPage.getPageSize());
			pageVoList.setTotal(activeTaskPage.getTotal());
			
			List<ActiveManageVo> activeList = new ArrayList<>();
			
			if (CollectionUtils.isNotEmpty(taskList)) {

				for (ActiveViewTaskVo activeViewTaskVo : taskList) {
					
					ActiveManageVo manageVo = new ActiveManageVo();
					
					BeanUtils.copyProperties(manageVo, activeViewTaskVo);
					
					manageVo.setId(activeViewTaskVo.getId());
					manageVo.setTaskName(activeViewTaskVo.getTaskName());
					manageVo.setTaskTitle(activeViewTaskVo.getTaskTitle());
					manageVo.setViewArea(activeViewTaskVo.getViewArea());
					manageVo.setViewLeader(activeViewTaskVo.getViewLeader());
					manageVo.setStartTime(DateHelper.toDateStr(activeViewTaskVo.getTaskStartTime()));
					manageVo.setEndTime(DateHelper.toDateStr(activeViewTaskVo.getTaskEndTime()));
					manageVo.setActiveLevel(EduLevel.retriveByIndex(activeViewTaskVo.getViewLevel()).getSymbol());
					Date now = new Date();
					if (now.before(activeViewTaskVo.getTaskStartTime())) {
						manageVo.setActiveStatusDesc("未开始");
					}
					
					if (now.after(activeViewTaskVo.getTaskEndTime())) {
						manageVo.setActiveStatusDesc("已结束");
					}
					
					if (now.after(activeViewTaskVo.getTaskStartTime()) 
							&& now.before(activeViewTaskVo.getTaskEndTime())) {
						manageVo.setActiveStatusDesc("正在进行");
					}
					
					activeList.add(manageVo);
				}
			}
			
			pageVoList.setRows(activeList);
			
			return pageVoList;
		} catch (Exception e) {
			logger.info("查询活动发生异常！", e);
			return pageVoList;
		}
		
		
	}
	
	
	class ActiveManageVo extends ActiveViewTaskVo{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -828901711703072656L;

		String activeLevel;
		
		String startTime;
		
		String endTime; 
		
		public String getActiveLevel() {
			return activeLevel;
		}

		public void setActiveLevel(String activeLevel) {
			this.activeLevel = activeLevel;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

	}
	
}
