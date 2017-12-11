package com.demo.demo.controller;

import com.demo.demo.service.AssetService;
import com.demo.demo.dto.ResponseDto;
import com.demo.demo.viewmodels.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/api") // This means URL's start with /demo (after Application path)
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping(value = "/add_asset")
    public ResponseEntity<?> addAsset(@RequestParam("file") MultipartFile file, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401L);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            //invoke add asset service
            assetService.saveAsset((Long)session.getAttribute("userid"),file);
            res.setStatus(200L);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch (Exception e){
            res.setStatus(500L);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get_assets")
    public ResponseEntity<?> getAssets(HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401L);
            res.setMessage("Not authorized");
            return new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
        }
        try {
            //invoke get assets service and set data
            res.setData(assetService.getAssetsByOwner((Long)session.getAttribute("userid")));
            res.setStatus(200L);
            res.setMessage("Success");
            return new ResponseEntity(res,HttpStatus.OK);
        } catch (Exception e){
            res.setStatus(500L);
            res.setMessage(e.getMessage());
            return new ResponseEntity(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
