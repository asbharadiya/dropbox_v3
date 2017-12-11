package com.app.dropbox.controller;

import com.app.dropbox.dto.ResponseDto;
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
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path="/user_profile",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestBody String user, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(user);
            userService.updateProfile((Long)session.getAttribute("userid"),jsonObject);
            res.setStatus(200);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch(CustomException e) {
            res.setStatus(e.getCode());
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.valueOf(e.getCode()));
        } catch (Exception e){
            res.setStatus(500);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path="/user_profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
        }
        try {
            res.setData(userService.getProfile((Long)session.getAttribute("userid")));
            res.setStatus(200);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch(CustomException e) {
            res.setStatus(e.getCode());
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.valueOf(e.getCode()));
        } catch (Exception e){
            res.setStatus(500);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path="/user_activity")
    public ResponseEntity<?> getUserActivity(HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
        }
        try {
            res.setData(userService.getUserActivity((Long)session.getAttribute("userid")));
            res.setStatus(200);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch(CustomException e) {
            res.setStatus(e.getCode());
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.valueOf(e.getCode()));
        } catch (Exception e){
            res.setStatus(500);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path="/search_users")
    public ResponseEntity<?> searchUsers(@RequestParam("q") String q, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
        }
        try {
            res.setData(userService.searchUsers((Long)session.getAttribute("userid"),q));
            res.setStatus(200);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch(CustomException e) {
            res.setStatus(e.getCode());
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.valueOf(e.getCode()));
        } catch (Exception e){
            res.setStatus(500);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
