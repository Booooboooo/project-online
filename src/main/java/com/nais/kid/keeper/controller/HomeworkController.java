package com.nais.kid.keeper.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.nais.domain.HomeworkTask;
import com.nais.kid.keeper.vo.HomeworkVo;
import com.nais.kid.utils.AuthorityContext;
import com.nais.response.GeneralResponse;
import com.nais.service.HomeWorkTaskService;
import com.nais.vo.HomeWorkTaskVo;
import com.nais.vo.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller()
@RequestMapping("/homework")
public class HomeworkController {

    private Logger LOGGER = LoggerFactory.getLogger(HomeworkController.class);

    @Autowired
    private HomeWorkTaskService homeWorkTaskService;

    @RequestMapping("list")
    public String entry() {
        return "homework/list";
    }

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping()
    public @ResponseBody
    GeneralResponse<String> addHomework(HomeworkVo homeworkVo) {
        LOGGER.info("receive addHomework: {}", JSON.toJSONString(homeworkVo));
        String comment = checkHomeworkVo(homeworkVo);
        if (!comment.equals("")) {
            return GeneralResponse.failure(comment);
        }
        HomeworkTask task = new HomeworkTask();
        task.setTaskName(homeworkVo.getTaskName());
        task.setTaskDetailResource(homeworkVo.getTaskDetailResourceUrl());
        task.setTaskLevel(homeworkVo.getViewLevel());
        try {
            task.setTaskStartTime(dateTimeFormat.parse(homeworkVo.getTaskStartTime()));
            task.setTaskEndTime(dateTimeFormat.parse(homeworkVo.getTaskEndTime()));
        } catch (Exception e) {
            LOGGER.info("DataTime format error, taskStartTime: {}， taskEndTime: {}",
                    homeworkVo.getTaskStartTime(), homeworkVo.getTaskEndTime());
            return GeneralResponse.failure("时间格式不正确","时间格式不正确！");
        }
        Long id = homeWorkTaskService.addHomework(task, homeworkVo.getTaskDetailResourceUrl(),
                homeworkVo.getTaskDetailResourceUrl(), AuthorityContext.getUser().getId());

        if (id != null) {
            return GeneralResponse.success(String.format("作业%s已经成功创建！", id));
        }
        return GeneralResponse.failure("创建作业内容失败！","创建作业内容失败！");
    }

    private String checkHomeworkVo(HomeworkVo homeworkVo) {
        if (Strings.isNullOrEmpty(homeworkVo.getTaskName())) {
            return "请填写作业名称";
        }
        if (Strings.isNullOrEmpty(homeworkVo.getTaskDetailResourceUrl())) {
            return "请上传作业详情";
        }
        return "";
    }

    @GetMapping()
    public @ResponseBody
    PageVo<HomeWorkTaskVo> queryHomework(String taskName, String taskStartTimeStr, String taskEndTimeStr,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) {

        LOGGER.info("receive queryHomework, taskName:{}, taskStartTimeStr:{}, taskEndTimeStr:{}, page:{}, rows:{}"
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
        PageVo<HomeWorkTaskVo> pageView = homeWorkTaskService.getHomeworkListByPage(taskName, null, taskStartTime, taskEndTime, page, rows);
        return pageView;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    GeneralResponse<Void> deleteHomework(@PathVariable Long id) {
        LOGGER.info("request deleteHomework: {}", id);
        String comment = null;
        if (id == null) {
            comment = "Id不能为空";
        }

        int effectRow = homeWorkTaskService.deleteHomeworkById(id);
        if (effectRow != 1) {
            comment = "id 不存在";
        }

        if (comment != null) {
            return GeneralResponse.failure(comment);
        }

        return GeneralResponse.success(null);
    }

}
