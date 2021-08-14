package com.vritti.vwblib.Beans;

/**
 * Created by 300151 on 10/24/2016.
 */
public class WorkspaceBean {
    String ProjectName;
    String PrjRoleId;
    String MembersCanCreate;
    String PrjEmpId;
    String EmployeeId;
    String ProjectId;
    String IsActive;
    String IsDeleted;

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getPrjRoleId() {
        return PrjRoleId;
    }

    public void setPrjRoleId(String prjRoleId) {
        PrjRoleId = prjRoleId;
    }

    public String getMembersCanCreate() {
        return MembersCanCreate;
    }

    public void setMembersCanCreate(String membersCanCreate) {
        MembersCanCreate = membersCanCreate;
    }

    public String getPrjEmpId() {
        return PrjEmpId;
    }

    public void setPrjEmpId(String prjEmpId) {
        PrjEmpId = prjEmpId;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }
}