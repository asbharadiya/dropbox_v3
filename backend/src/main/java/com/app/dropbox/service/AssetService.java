package com.app.dropbox.service;

import com.app.dropbox.dto.AssetDto;
import com.app.dropbox.entity.Asset;
import com.app.dropbox.entity.Group;
import com.app.dropbox.entity.User;
import com.app.dropbox.entity.UserActivity;
import com.app.dropbox.repository.AssetRepository;
import com.app.dropbox.repository.GroupRepository;
import com.app.dropbox.repository.UserActivityRepository;
import com.app.dropbox.repository.UserRepository;
import com.app.dropbox.utils.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AssetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    public void addAsset(Long userId, JSONObject jsonData, MultipartFile file) throws Exception {
        User u = userRepository.findOne(userId);
        if(jsonData.getBoolean("isDirectory")){
            if(jsonData.has("superParent") && jsonData.has("parent")){
                Asset parent = assetRepository.findByName(jsonData.getString("parent")).get(0);
                int count = assetRepository.findCountByName(jsonData.getString("name"));
                Asset a = new Asset();
                a.setDeleted(false);
                a.setCreatedDate(new Date());
                a.setDirectory(true);
                if(count > 0) {
                    a.setName(jsonData.getString("name") + "_" + (count));
                    a.setOriginalName(jsonData.getString("name")+ "_" + (count));
                } else {
                    a.setName(jsonData.getString("name"));
                    a.setOriginalName(jsonData.getString("name"));
                }
                a.setOwner(u);
                a.setParent(parent);
                assetRepository.save(a);
            } else {
                int count = assetRepository.findCountByName(jsonData.getString("name"));
                Asset a = new Asset();
                a.setDeleted(false);
                a.setCreatedDate(new Date());
                a.setDirectory(true);
                if(count > 0) {
                    a.setName(jsonData.getString("name") + "_" + (count));
                    a.setOriginalName(jsonData.getString("name")+ "_" + (count));
                } else {
                    a.setName(jsonData.getString("name"));
                    a.setOriginalName(jsonData.getString("name"));
                }
                a.setOwner(u);
                assetRepository.save(a);
            }
            UserActivity act = new UserActivity();
            act.setAction("New folder added");
            act.setCreatedDate(new Date());
            act.setOwner(u);
            userActivityRepository.save(act);
        } else {
            if(jsonData.has("superParent") && jsonData.has("parent")){
                Asset parent = assetRepository.findByName(jsonData.getString("parent")).get(0);
                int count = assetRepository.findCountByName(file.getOriginalFilename());
                String filename;
                if(count > 0) {
                    filename = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."))+"_"+count+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                } else {
                    filename = file.getOriginalFilename();
                }
                String filePath = new File("/uploads").getAbsolutePath()+"/";
                if(! new File(filePath).exists())
                {
                    new File(filePath).mkdir();
                }
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath + file.getOriginalFilename());
                Files.write(path, bytes);
                JSONObject obj = new JSONObject();
                obj.put("size",file.getSize());
                obj.put("mimetype",file.getContentType());
                obj.put("path",filePath + file.getOriginalFilename());
                Asset a = new Asset();
                a.setDeleted(false);
                a.setCreatedDate(new Date());
                a.setDirectory(false);
                a.setName(filename);
                a.setOriginalName(filename);
                a.setOwner(u);
                a.setParent(parent);
                a.setMetadata(obj.toString());
                assetRepository.save(a);
            } else {
                int count = assetRepository.findCountByName(file.getOriginalFilename());
                String filename;
                if(count > 0) {
                    filename = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."))+"_"+count+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                } else {
                    filename = file.getOriginalFilename();
                }
                String filePath = new File("uploads").getAbsolutePath()+"/";
                if(! new File(filePath).exists())
                {
                    new File(filePath).mkdir();
                }
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath + file.getOriginalFilename());
                Files.write(path, bytes);
                JSONObject obj = new JSONObject();
                obj.put("size",file.getSize());
                obj.put("mimetype",file.getContentType());
                obj.put("path",filePath + file.getOriginalFilename());
                Asset a = new Asset();
                a.setDeleted(false);
                a.setCreatedDate(new Date());
                a.setDirectory(false);
                a.setName(filename);
                a.setOriginalName(filename);
                a.setOwner(u);
                a.setMetadata(obj.toString());
                assetRepository.save(a);
            }
            UserActivity act = new UserActivity();
            act.setAction("New file uploaded");
            act.setCreatedDate(new Date());
            act.setOwner(u);
        }
    }

    public Set<AssetDto> getAssets(Long userId, JSONObject jsonData) throws Exception {
        Set<AssetDto> dtos = new HashSet<>();
        User u = userRepository.findOne(userId);
        if(jsonData.has("starred") && jsonData.getBoolean("starred")){
            for(Asset a:u.getStarredAssets()){
                AssetDto dto = new AssetDto();
                dto.setId(a.getId());
                dto.setCreatedDate(a.getCreatedDate());
                dto.setDirectory(a.getDirectory());
                dto.setName(a.getName());
                dto.setStarred(true);
                dto.setOwnerId(a.getOwner().getId());
                dto.setOwner(a.getOwner().getId().equals(u.getId()));
                dtos.add(dto);
            }
        } else if(jsonData.has("recent") && jsonData.getBoolean("recent")){
            Set<Asset> starredAssets = u.getStarredAssets();
            List<Asset> finalAssets = new ArrayList<>();
            finalAssets.addAll(u.getMyAssets());
            finalAssets.addAll(u.getSharedAssets());
            for(Group g:u.getMyGroups()){
                finalAssets.addAll(g.getSharedAssets());
            }
            for(Group g:u.getSharedGroups()){
                finalAssets.addAll(g.getSharedAssets());
            }
            Collections.sort(finalAssets, new Comparator<Asset>(){
                public int compare(Asset o1, Asset o2){
                    return o1.getCreatedDate().compareTo(o2.getCreatedDate());
                }
            });
            for(Asset a:finalAssets){
                AssetDto dto = new AssetDto();
                dto.setId(a.getId());
                dto.setCreatedDate(a.getCreatedDate());
                dto.setDirectory(a.getDirectory());
                dto.setName(a.getName());
                dto.setOwnerId(a.getOwner().getId());
                dto.setOwner(a.getOwner().getId().equals(u.getId()));
                dto.setStarred(starredAssets.contains(a));
                dtos.add(dto);
                if(dtos.size() == 10){
                    break;
                }
            }
        } else if(jsonData.has("parent") && jsonData.get("parent") != null && jsonData.getString("parent").length() > 0
                && jsonData.has("superParent") && jsonData.get("superParent") != null && jsonData.getString("superParent").length() > 0){
            Set<Asset> starredAssets = u.getStarredAssets();
            List<Asset> finalAssets = new ArrayList<>();
            finalAssets.addAll(u.getMyAssets());
            finalAssets.addAll(u.getSharedAssets());
            for(Group g:u.getMyGroups()){
                finalAssets.addAll(g.getSharedAssets());
            }
            for(Group g:u.getSharedGroups()){
                finalAssets.addAll(g.getSharedAssets());
            }
            for(Asset a:finalAssets){
                if(a.getName().equals(jsonData.getString("superParent"))){
                    if(jsonData.getString("superParent").equals(jsonData.getString("parent"))){
                        for(Asset as:assetRepository.findByParent(a)){
                            AssetDto dto = new AssetDto();
                            dto.setId(as.getId());
                            dto.setCreatedDate(as.getCreatedDate());
                            dto.setDirectory(as.getDirectory());
                            dto.setName(as.getName());
                            dto.setOwnerId(as.getOwner().getId());
                            dto.setOwner(as.getOwner().getId().equals(u.getId()));
                            dto.setStarred(starredAssets.contains(as));
                            dtos.add(dto);
                        }
                    } else {
                        List<Asset> assets = assetRepository.findByNameAndOwner(jsonData.getString("parent"),a.getOwner());
                        for(Asset as:assetRepository.findByParent(assets.get(0))){
                            AssetDto dto = new AssetDto();
                            dto.setId(as.getId());
                            dto.setCreatedDate(as.getCreatedDate());
                            dto.setDirectory(as.getDirectory());
                            dto.setName(as.getName());
                            dto.setOwnerId(as.getOwner().getId());
                            dto.setOwner(as.getOwner().getId().equals(u.getId()));
                            dto.setStarred(starredAssets.contains(as));
                            dtos.add(dto);
                        }
                    }
                    break;
                }
            }
        } else {
            Set<Asset> starredAssets = u.getStarredAssets();
            List<Asset> finalAssets = new ArrayList<>();
            finalAssets.addAll(u.getMyAssets());
            finalAssets.addAll(u.getSharedAssets());
            for(Group g:u.getMyGroups()){
                finalAssets.addAll(g.getSharedAssets());
            }
            for(Group g:u.getSharedGroups()){
                finalAssets.addAll(g.getSharedAssets());
            }
            for(Asset a:finalAssets){
                if((a.getOwner().getId().equals(u.getId()) && a.getParent() == null) || !a.getOwner().getId().equals(u.getId())) {
                    AssetDto dto = new AssetDto();
                    dto.setId(a.getId());
                    dto.setCreatedDate(a.getCreatedDate());
                    dto.setDirectory(a.getDirectory());
                    dto.setName(a.getName());
                    dto.setOwnerId(a.getOwner().getId());
                    dto.setOwner(a.getOwner().getId().equals(u.getId()));
                    dto.setStarred(starredAssets.contains(a));
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }

    public void deleteAsset(Long userId, JSONObject jsonData) throws Exception {
        if(jsonData.getLong("assetId") == 0){
            throw new CustomException("Required fields missing",400);
        }
        User u = userRepository.findOne(userId);
        Asset a = assetRepository.findOne(jsonData.getLong("assetId"));
        if(!a.getOwner().getId().equals(u.getId())){
            throw new CustomException("Not authorized",403);
        }
        assetRepository.delete(a);
        UserActivity act = new UserActivity();
        act.setAction("File or folder deleted");
        act.setCreatedDate(new Date());
        act.setOwner(u);
        userActivityRepository.save(act);
    }

    public void starAsset(Long userId, JSONObject jsonData) throws Exception {
        if(jsonData.getLong("assetId") == 0){
            throw new CustomException("Required fields missing",400);
        }
        Asset a = assetRepository.findOne(jsonData.getLong("assetId"));
        User u = userRepository.findOne(userId);
        Set<User> starredUsers = a.getStarredUsers();
        if(jsonData.getBoolean("isStarred")){
            starredUsers.add(u);
        } else {
            starredUsers.remove(u);
        }
        a.setStarredUsers(starredUsers);
        assetRepository.save(a);
        UserActivity act = new UserActivity();
        act.setAction("File or folder starred");
        act.setCreatedDate(new Date());
        act.setOwner(u);
        userActivityRepository.save(act);
    }

    public void shareAsset(Long userId, JSONObject jsonData) throws Exception {
        if(jsonData.getLong("assetId") == 0 || jsonData.getLong("targetId") == 0
                || jsonData.getString("shareWith") == null || jsonData.getString("shareWith").length() == 0){
            throw new CustomException("Required fields missing",400);
        }
        UserActivity act = new UserActivity();
        Asset a = assetRepository.findOne(jsonData.getLong("assetId"));
        User owner = userRepository.findOne(userId);
        if(!a.getOwner().getId().equals(owner.getId())){
            throw new CustomException("Not authorized",403);
        }
        if(jsonData.getString("shareWith").equals("users")){
            User target = userRepository.findOne(jsonData.getLong("targetId"));
            Set<User> sharedUsers = a.getSharedUsers();
            sharedUsers.add(target);
            a.setSharedUsers(sharedUsers);
            assetRepository.save(a);
            act.setAction("File or folder shared with an user");
        } else {
            Group target = groupRepository.findOne(jsonData.getLong("targetId"));
            Set<Group> sharedGroups = a.getSharedGroups();
            sharedGroups.add(target);
            a.setSharedGroups(sharedGroups);
            assetRepository.save(a);
            act.setAction("File or folder shared with a group");
        }
        act.setCreatedDate(new Date());
        act.setOwner(owner);
        userActivityRepository.save(act);
    }

    public JSONObject downloadAsset(Long id) throws Exception {
        Asset a = assetRepository.findOne(id);
        JSONObject jsonObject = new JSONObject(a.getMetadata());
        jsonObject.put("name",a.getName());
        return jsonObject;
    }

}
