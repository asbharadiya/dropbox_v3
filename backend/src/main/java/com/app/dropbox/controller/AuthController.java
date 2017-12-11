package com.app.dropbox.controller;

import com.app.dropbox.dto.ResponseDto;
import com.app.dropbox.entity.User;
import com.app.dropbox.service.UserService;
import com.app.dropbox.utils.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/api") // This means URL's start with /demo (after Application path)
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(path="/signin",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signin(@RequestBody String user, HttpSession session) {
        ResponseDto res = new ResponseDto();
        try {
            JSONObject jsonObject = new JSONObject(user);
            JSONObject resObj = userService.signin(jsonObject);
            session.setAttribute("userid",resObj.getLong("id"));
            session.setAttribute("username",resObj.getString("username"));
            res.setStatus(200);
            res.setMessage("Success");
            return new ResponseEntity(res, HttpStatus.OK);
        } catch(CustomException e) {
            res.setStatus(e.getCode());
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.valueOf(e.getCode()));
        } catch(Exception e) {
            res.setStatus(500);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/signup",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody String user, HttpSession session) {
        ResponseDto res = new ResponseDto();
        try {
            JSONObject jsonObject = new JSONObject(user);
            JSONObject resObj = userService.signup(jsonObject);
            session.setAttribute("userid",resObj.getLong("id"));
            session.setAttribute("username",resObj.getString("username"));
            res.setStatus(200);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch(CustomException e) {
            res.setStatus(e.getCode());
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.valueOf(e.getCode()));
        } catch(Exception e) {
            res.setStatus(500);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        ResponseDto res = new ResponseDto();
        session.invalidate();
        res.setStatus(200);
        res.setMessage("Success");
        return new ResponseEntity(res,HttpStatus.OK);
    }

    @GetMapping(value = "/check_session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
        }
        User user = new User();
        user.setUsername(session.getAttribute("username").toString());
        res.setStatus(200);
        res.setMessage("Success");
        res.setData(user);
        return new ResponseEntity(res,HttpStatus.OK);
    }
}