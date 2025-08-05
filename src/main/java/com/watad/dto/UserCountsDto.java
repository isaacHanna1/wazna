package com.watad.dto;

public class UserCountsDto {

    private Long totalProfiles;
    private Long activeProfiles;
    private Long inactiveProfiles;

    public UserCountsDto(Long totalProfiles, Long activeProfiles, Long inactiveProfiles) {
        this.totalProfiles = totalProfiles;
        this.activeProfiles = activeProfiles;
        this.inactiveProfiles = inactiveProfiles;
    }

    public Long getTotalProfiles() {
        return totalProfiles;
    }

    public void setTotalProfiles(Long totalProfiles) {
        this.totalProfiles = totalProfiles;
    }

    public Long getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(Long activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    public Long getInactiveProfiles() {
        return inactiveProfiles;
    }

    public void setInactiveProfiles(Long inactiveProfiles) {
        this.inactiveProfiles = inactiveProfiles;
    }
}
