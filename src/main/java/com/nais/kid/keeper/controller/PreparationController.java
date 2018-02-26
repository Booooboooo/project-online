package com.nais.kid.keeper.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.nais.domain.ViewTask;
import com.nais.kid.keeper.vo.PreparationVo;
import com.nais.kid.utils.AuthorityContext;
import com.nais.response.GeneralResponse;
import com.nais.service.ViewTaskService;
import com.nais.vo.PageVo;
import com.nais.vo.ViewTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller()
@RequestMapping("/preparations")
public class PreparationController {

    private Logger LOGGER = LoggerFactory.getLogger(PreparationController.class);

    @Autowired
    private ViewTaskService viewTaskService;

    @RequestMapping("list")
    public String entry() {
        return "preparation/list";
    }

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping()
    public @ResponseBody
    GeneralResponse<String> addPreparation(PreparationVo preparationVo) {
        LOGGER.info("receive addPreparation: {}", JSON.toJSONString(preparationVo));
        String comment = checkPreparationVo(preparationVo);
        if (!comment.equals("")) {
            return GeneralResponse.failure(comment);
        }
        ViewTask task = new ViewTask();
        task.setTaskName(preparationVo.getTaskName());
        task.setTaskDetailResource(preparationVo.getTaskDetailResourceUrl());
        task.setViewLevel(preparationVo.getViewLevel());
        try {
            task.setTaskStartTime(dateTimeFormat.parse(preparationVo.getTaskStartTime()));
            task.setTaskEndTime(dateTimeFormat.parse(preparationVo.getTaskEndTime()));
        } catch (Exception e) {
            LOGGER.info("DataTime format error, taskStartTime: {}， taskEndTime: {}",
                    preparationVo.getTaskStartTime(), preparationVo.getTaskEndTime());
            return GeneralResponse.failure("时间格式不正确","时间格式不正确！");
        }
        Long id = viewTaskService.addPreviewTask(task, preparationVo.getTaskDetailResourceUrl(),
                preparationVo.getTaskDetailResourceUrl(), AuthorityContext.getUser().getId());

        if (id != null) {
            return GeneralResponse.success(String.format("活动%s已经成功创建！", id));
        }
        return GeneralResponse.failure("创建预习内容失败！","创建预习内容失败！");
    }

    private String checkPreparationVo(PreparationVo preparationVo) {
        if (Strings.isNullOrEmpty(preparationVo.getTaskName())) {
            return "请填写预习名称";
        }
        if (Strings.isNullOrEmpty(preparationVo.getTaskDetailResourceUrl())) {
            return "请上传预习详情";
        }
        return "";
    }

    @GetMapping()
    public @ResponseBody
    PageVo<ViewTaskVo> queryPreparation(String taskName, String taskStartTimeStr, String taskEndTimeStr,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) {

        LOGGER.info("receive queryPreparation, taskName:{}, taskStartTimeStr:{}, taskEndTimeStr:{}, page:{}, rows:{}"
                , taskName, taskStartTimeStr, taskEndTimeStr, page, rows);
        Date taskStartTime = null;
        Date taskEndTime = null;
        try {
            if (!Strings.isNullOrEmpty(taskStartTimeStr))
                taskStartTime = dateTimeFormat.parse(taskStartTimeStr);
        } catch (Exception e) {
            LOGGER.info("", e);
        }
        try {
            if (!Strings.isNullOrEmpty(taskEndTimeStr))
                taskEndTime = dateTimeFormat.parse(taskEndTimeStr);
        } catch (Exception e) {
            LOGGER.info("", e);
        }
        PageVo<ViewTaskVo> pageView = viewTaskService.getPreViewTaskVoByPage(taskName, taskStartTime, taskEndTime, page, rows);
        return pageView;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    GeneralResponse<Void> deletePreparation(@PathVariable Long id) {
        LOGGER.info("request deletePreparation: {}", id);
        String comment = null;
        if (id == null) {
            comment = "Id不能为空";
        }

        int effectRow = viewTaskService.deleteViewTask(id);
        if (effectRow != 1) {
            comment = "id 不存在";
        }

        if (comment != null) {
            return GeneralResponse.failure(comment);
        }

        return GeneralResponse.success(null);
    }

}
