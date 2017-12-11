package com.app.dropbox.service;

import com.app.dropbox.dto.GroupDto;
import com.app.dropbox.dto.UserDto;
import com.app.dropbox.entity.Group;
import com.app.dropbox.entity.User;
import com.app.dropbox.entity.UserActivity;
import com.app.dropbox.repository.GroupRepository;
import com.app.dropbox.repository.UserActivityRepository;
import com.app.dropbox.repository.UserRepository;
import com.app.dropbox.utils.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    public void createGroup(Long userId, JSONObject jsonData) throws Exception {
        if(jsonData.getString("name") == null || jsonData.getString("name").length() == 0){
            throw new CustomException("Required fields missing",400);
        }
        Group g = new Group();
        g.setName(jsonData.getString("name"));
        User u = userRepository.findOne(userId);
        g.setOwner(u);
        groupRepository.save(g);
        UserActivity act = new UserActivity();
        act.setAction("New group created");
        act.setCreatedDate(new Date());
        act.setOwner(u);
        userActivityRepository.save(act);
    }

    public void updateGroup(JSONObject jsonData) throws Exception {
        if(jsonData.getString("name") == null || jsonData.getString("name").length() == 0 || jsonData.getLong("groupId") == 0){
            throw new CustomException("Required fields missing",400);
        }
        Group g = groupRepository.findOne(jsonData.getLong("groupId"));
        g.setName(jsonData.getString("name"));
        groupRepository.save(g);
    }

    public List<GroupDto> getGroups(Long userId) throws Exception {
        User user = userRepository.findOne(userId);
        List<GroupDto> groups = groupRepository.getGroupsByOwner(user);
        return groups;
    }

    public GroupDto getGroupById(Long id) throws Exception {
        Group group = groupRepository.findOne(id);
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        Set<UserDto> membersDto = new HashSet<>();
        for(User u:group.getMembers()){
            UserDto udto = new UserDto();
            udto.setId(u.getId());
            udto.setUsername(u.getFirstName()+" "+u.getLastName());
            udto.setEmail(u.getEmail());
            membersDto.add(udto);
        }
        dto.setMembers(membersDto);
        return dto;
    }

    public void addRemoveMemberGroup(JSONObject jsonData) throws Exception {
        if(jsonData.getString("action") == null || jsonData.getString("action").length() == 0 || jsonData.getLong("groupId") == 0 || jsonData.getLong("memberId") == 0){
            throw new CustomException("Required fields missing",400);
        }
        Group g = groupRepository.findOne(jsonData.getLong("groupId"));
        User u = userRepository.findOne(jsonData.getLong("memberId"));
        Set<User> members = g.getMembers();
        if(jsonData.getString("action").equals("ADD")){
            members.add(u);
        } else {
            members.remove(u);
        }
        g.setMembers(members);
        groupRepository.save(g);
    }

    public void deleteGroup(JSONObject jsonData) throws Exception {
        if(jsonData.getLong("groupId") == 0){
            throw new CustomException("Required fields missing",400);
        }
        groupRepository.delete(jsonData.getLong("groupId"));
    }

}
