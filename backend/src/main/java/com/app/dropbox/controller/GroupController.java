package com.app.dropbox.controller;

import com.app.dropbox.dto.ResponseDto;
import com.app.dropbox.service.GroupService;
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
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping(path="/create_group",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGroup(@RequestBody String group, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(group);
            groupService.createGroup((Long)session.getAttribute("userid"),jsonObject);
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

    @PostMapping(path="/update_group",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGroup(@RequestBody String group, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(group);
            groupService.updateGroup(jsonObject);
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

    @GetMapping(path="/get_groups")
    public ResponseEntity<?> getGroups(HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            res.setData(groupService.getGroups((Long)session.getAttribute("userid")));
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

    @GetMapping(path="/get_group_by_id")
    public ResponseEntity<?> getGroupById(@RequestParam(value="id", required = true) Long id, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            res.setData(groupService.getGroupById(id));
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

    @PostMapping(path="/add_remove_member_group",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRemoveMemberGroup(@RequestBody String group, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(group);
            groupService.addRemoveMemberGroup(jsonObject);
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

    @PostMapping(path="/delete_group",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGroup(@RequestBody String group, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(group);
            groupService.deleteGroup(jsonObject);
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
