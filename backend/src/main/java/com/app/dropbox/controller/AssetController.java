package com.app.dropbox.controller;

import com.app.dropbox.dto.ResponseDto;
import com.app.dropbox.service.AssetService;
import com.app.dropbox.utils.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/api") // This means URL's start with /demo (after Application path)
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping(path="/add_asset",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAsset(@RequestPart("body") String body, @RequestPart(value="file",required=false) MultipartFile file, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(body);
            assetService.addAsset((Long)session.getAttribute("userid"),jsonObject,file);
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

    @PostMapping(path="/get_assets",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAssets(@RequestBody String jsonData, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            res.setData(assetService.getAssets((Long)session.getAttribute("userid"),jsonObject));
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

    @PostMapping(path="/delete_asset",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAsset(@RequestBody String jsonData, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            assetService.deleteAsset((Long)session.getAttribute("userid"),jsonObject);
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

    @PostMapping(path="/star_asset",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> starAsset(@RequestBody String jsonData, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            assetService.starAsset((Long)session.getAttribute("userid"),jsonObject);
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

    @PostMapping(path="/share_asset",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareAsset(@RequestBody String jsonData, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            assetService.shareAsset((Long)session.getAttribute("userid"),jsonObject);
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

    @GetMapping(path="/download_asset/{id}")
    public ResponseEntity<Resource> shareAsset(@PathVariable("id") Long id, HttpSession session) {
        ResponseDto res = new ResponseDto();
        if(session.isNew() || session.getAttribute("userid") == null){
            res.setStatus(401);
            res.setMessage("Not authorized");
            return new ResponseEntity(res, HttpStatus.UNAUTHORIZED);
        }
        try {
            JSONObject jsonData = assetService.downloadAsset(id);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(jsonData.getString("path")));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename='"+jsonData.getString("name")+"'")
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
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
