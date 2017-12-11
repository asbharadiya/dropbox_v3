package com.demo.demo.service;

import com.demo.demo.entity.User;
import com.demo.demo.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public JSONObject signin(JSONObject jsonData) throws Exception {
        User user = userRepository.findByEmail(jsonData.getString("email"));
        if(user == null) {
            throw new Exception("User not found");
        }
        if(!user.getPassword().equals(jsonData.getString("password"))){
            throw new Exception("Invalid credentials");
        }
        JSONObject resObj = new JSONObject();
        resObj.put("id",user.getId());
        resObj.put("username",user.getFirstName()+" "+user.getLastName());
        return resObj;
    }

    public JSONObject signup(JSONObject jsonData) throws Exception {
        User user = userRepository.findByEmail(jsonData.getString("email"));
        if(user != null) {
            throw new Exception("User already exists");
        }
        user = new User();
        user.setFirstName(jsonData.getString("firstName"));
        user.setLastName(jsonData.getString("lastName"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        user.setDateOfBirth(formatter.parse(jsonData.getString("dateOfBirth")));
        user.setEmail(jsonData.getString("email"));
        user.setPassword(jsonData.getString("password"));
        user = userRepository.save(user);
        JSONObject resObj = new JSONObject();
        resObj.put("id",user.getId());
        resObj.put("username",user.getFirstName()+" "+user.getLastName());
        return resObj;
    }

}
