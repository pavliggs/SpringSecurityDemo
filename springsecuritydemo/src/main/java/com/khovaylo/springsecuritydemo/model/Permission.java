package com.khovaylo.springsecuritydemo.model;

/**
 * @author Pavel Khovaylo
 */
public enum Permission {
    DEVELOPER_READ("developer:read"),
    DEVELOPER_WRITE("developer:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
