package com.nais.kid.keeper.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.nais.domain.ViewTask;
import com.nais.kid.keeper.vo.ReviewVo;
import com.nais.kid.utils.AuthorityContext;
import com.nais.response.GeneralResponse;
import com.nais.service.ViewTaskService;
import com.nais.vo.PageVo;
import com.nais.vo.ViewTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller()
@RequestMapping("/reviews")
public class ReviewController {

    private Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ViewTaskService viewTaskService;

    @RequestMapping("list")
    public String entry() {
        return "review/list";
    }

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping()
    public @ResponseBody
    GeneralResponse<String> addReview(ReviewVo ReviewVo) {
        LOGGER.info("receive addReview: {}", JSON.toJSONString(ReviewVo));
        String comment = checkReviewVo(ReviewVo);
        if (!comment.equals("")) {
            return GeneralResponse.failure(comment);
        }
        ViewTask task = new ViewTask();
        task.setTaskName(ReviewVo.getTaskName());
        task.setTaskDetailResource(ReviewVo.getTaskDetailResourceUrl());
        task.setViewLevel(ReviewVo.getViewLevel());
        try {
            task.setTaskStartTime(dateTimeFormat.parse(ReviewVo.getTaskStartTime()));
            task.setTaskEndTime(dateTimeFormat.parse(ReviewVo.getTaskEndTime()));
        } catch (Exception e) {
            LOGGER.info("DataTime format error, taskStartTime: {}， taskEndTime: {}",
                    ReviewVo.getTaskStartTime(), ReviewVo.getTaskEndTime());
            return GeneralResponse.failure("时间格式不正确","时间格式不正确！");
        }
        Long id = viewTaskService.addReviewTask(task, ReviewVo.getTaskDetailResourceUrl(),
                ReviewVo.getTaskDetailResourceUrl(), AuthorityContext.getUser().getId());

        if (id != null) {
            return GeneralResponse.success(String.format("活动%s已经成功创建！", id));
        }
        return GeneralResponse.failure("创建复习内容失败！","创建复习内容失败！");
    }

    private String checkReviewVo(ReviewVo ReviewVo) {
        if (Strings.isNullOrEmpty(ReviewVo.getTaskName())) {
            return "请填写复习名称";
        }
        if (Strings.isNullOrEmpty(ReviewVo.getTaskDetailResourceUrl())) {
            return "请上传复习详情";
        }
        return "";
    }

    @GetMapping()
    public @ResponseBody
    PageVo<ViewTaskVo> queryReview(String taskName, String taskStartTimeStr, String taskEndTimeStr,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) {

        LOGGER.info("receive queryReview, taskName:{}, taskStartTimeStr:{}, taskEndTimeStr:{}, page:{}, rows:{}"
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
        PageVo<ViewTaskVo> pageView = viewTaskService.getReViewTaskVoByPage(taskName, taskStartTime, taskEndTime, page, rows);
        return pageView;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    GeneralResponse<Void> deleteReview(@PathVariable Long id) {
        LOGGER.info("request deleteReview: {}", id);
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
