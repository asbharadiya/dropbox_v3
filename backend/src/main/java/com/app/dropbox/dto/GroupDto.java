package com.app.dropbox.dto;

import java.util.HashSet;
import java.util.Set;

public class GroupDto {

    private Long id;
    private String name;

    private Set<UserDto> members = new HashSet<>();

    public GroupDto(){}

    public GroupDto(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDto> getMembers() {
        return members;
    }

    public void setMembers(Set<UserDto> members) {
        this.members = members;
    }
}
