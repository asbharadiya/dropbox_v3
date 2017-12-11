package com.app.dropbox.service;

import com.app.dropbox.dto.UserActivityDto;
import com.app.dropbox.dto.UserDto;
import com.app.dropbox.entity.User;
import com.app.dropbox.entity.UserActivity;
import com.app.dropbox.repository.UserActivityRepository;
import com.app.dropbox.repository.UserRepository;
import com.app.dropbox.utils.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    public JSONObject signin(JSONObject jsonData) throws Exception {
        if(!jsonData.has("email") || jsonData.getString("email") == null || jsonData.getString("email").length() == 0
                || !jsonData.has("password") || jsonData.getString("password") == null || jsonData.getString("password").length() == 0) {
            throw new CustomException("Required fields missing", 400);
        }
        User user = userRepository.findByEmail(jsonData.getString("email"));
        if(user == null) {
            throw new CustomException("User not found", 401);
        }
        if(!user.getPassword().equals(jsonData.getString("password"))){
            throw new CustomException("Invalid credentials", 401);
        }
        JSONObject resObj = new JSONObject();
        resObj.put("id",user.getId());
        resObj.put("username",user.getFirstName()+" "+user.getLastName());
        return resObj;
    }

    public JSONObject signup(JSONObject jsonData) throws Exception {
        if(!jsonData.has("email") || jsonData.getString("email") == null || jsonData.getString("email").length() == 0
                || !jsonData.has("password") || jsonData.getString("password") == null || jsonData.getString("password").length() == 0
                || !jsonData.has("firstName") || jsonData.getString("firstName") == null || jsonData.getString("firstName").length() == 0
                || !jsonData.has("lastName") || jsonData.getString("lastName") == null || jsonData.getString("lastName").length() == 0) {
            throw new CustomException("Required fields missing", 400);
        }
        User user = userRepository.findByEmail(jsonData.getString("email"));
        if(user != null) {
            throw new CustomException("User already exists", 409);
        }
        user = new User();
        user.setFirstName(jsonData.getString("firstName"));
        user.setLastName(jsonData.getString("lastName"));
        user.setEmail(jsonData.getString("email"));
        user.setPassword(jsonData.getString("password"));
        user = userRepository.save(user);
        JSONObject resObj = new JSONObject();
        resObj.put("id",user.getId());
        resObj.put("username",user.getFirstName()+" "+user.getLastName());
        return resObj;
    }

    public void updateProfile(Long userId, JSONObject jsonData) throws Exception {
        if(!jsonData.has("firstName") || jsonData.getString("firstName") == null || jsonData.getString("firstName").length() == 0
                || !jsonData.has("lastName") || jsonData.getString("lastName") == null || jsonData.getString("lastName").length() == 0) {
            throw new CustomException("Required fields missing", 400);
        }
        User user = userRepository.findOne(userId);
        user.setFirstName(jsonData.getString("firstName"));
        user.setLastName(jsonData.getString("lastName"));
        if(jsonData.has("about"))
            user.setAbout(jsonData.getString("about"));
        if(jsonData.has("contactNo"))
            user.setContactNo(jsonData.getString("contactNo"));
        if(jsonData.has("education"))
            user.setEducation(jsonData.getString("education"));
        if(jsonData.has("occupation"))
            user.setOccupation(jsonData.getString("occupation"));
        userRepository.save(user);
        UserActivity act = new UserActivity();
        act.setAction("Profile updated");
        act.setCreatedDate(new Date());
        act.setOwner(user);
        userActivityRepository.save(act);
    }

    public UserDto getProfile(Long userId) throws Exception {
        User user = userRepository.findOne(userId);
        UserDto dto = new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setContactNo(user.getContactNo());
        dto.setAbout(user.getAbout());
        dto.setEducation(user.getEducation());
        dto.setOccupation(user.getOccupation());
        return dto;
    }

    public List<UserActivityDto> getUserActivity(Long userId) throws Exception {
        List<UserActivityDto> activity = new ArrayList<>();
        User user = userRepository.findOne(userId);
        for(UserActivity a:user.getUserActivity()){
            UserActivityDto dto = new UserActivityDto();
            dto.setId(a.getId());
            dto.setAction(a.getAction());
            dto.setCreatedDate(a.getCreatedDate());
            activity.add(dto);
        }
        return activity;
    }

    public List<UserDto> searchUsers(Long userId, String q) throws Exception {
        List<UserDto> users = userRepository.searchUsers(userId);
        return users;
    }

}