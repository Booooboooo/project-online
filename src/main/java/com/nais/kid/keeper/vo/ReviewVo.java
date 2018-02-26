package com.nais.kid.keeper.vo;

public class ReviewVo {
    private String taskName;
    private String taskDetailResourceUrl;
    private Integer viewLevel;
    private String taskStartTime;
    private String taskEndTime;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDetailResourceUrl() {
        return taskDetailResourceUrl;
    }

    public void setTaskDetailResourceUrl(String taskDetailResourceUrl) {
        this.taskDetailResourceUrl = taskDetailResourceUrl;
    }

    public Integer getViewLevel() {
        return viewLevel;
    }

    public void setViewLevel(Integer viewLevel) {
        this.viewLevel = viewLevel;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }
}
