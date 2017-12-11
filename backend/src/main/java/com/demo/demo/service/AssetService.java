package com.demo.demo.service;

import com.demo.demo.dto.AssetDto;
import com.demo.demo.entity.Asset;
import com.demo.demo.repository.AssetRepository;
import com.demo.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveAsset(Long userId, MultipartFile file) throws Exception {
        if(file == null){
            throw new Exception("File is required");
        }
        Asset asset = new Asset();
        asset.setFileName(file.getOriginalFilename());
        //TODO: save file to file system
        asset.setFilePath(null);
        asset.setOwner(userRepository.findOne(userId));
        assetRepository.save(asset);
    }

    public List<AssetDto> getAssetsByOwner(Long userId) throws Exception {
        return assetRepository.findByOwner(userRepository.findOne(userId));
    }
}
