package ra.entity;

import java.io.Serializable;

public class Account implements Serializable {
    private int id;
    private String userName;
    private String password;
    private boolean permission;
    private String emp_id;
    private boolean status;

    public Account() {
    }

    public Account(int id, String userName, String password, boolean permission, String emp_id, boolean status) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.permission = permission;
        this.emp_id = emp_id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
