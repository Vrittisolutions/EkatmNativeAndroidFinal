package com.vritti.vwblib.Beans;

/**
 * Created by 300151 on 10/17/2016.
 */
public class MyWorkspaceBean {
    String ProjectName;
    String ProjectId;
    String OnTime;
    String OpenActivities;
    String OnTimePerc;

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getOnTime() {
        return OnTime;
    }

    public void setOnTime(String onTime) {
        OnTime = onTime;
    }

    public String getOpenActivities() {
        return OpenActivities;
    }

    public void setOpenActivities(String openActivities) {
        OpenActivities = openActivities;
    }

    public String getOnTimePerc() {
        return OnTimePerc;
    }

    public void setOnTimePerc(String onTimePerc) {
        OnTimePerc = onTimePerc;
    }
}
