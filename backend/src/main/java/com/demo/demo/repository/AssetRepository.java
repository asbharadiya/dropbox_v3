package com.demo.demo.repository;

import com.demo.demo.dto.AssetDto;
import com.demo.demo.entity.Asset;
import com.demo.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssetRepository extends CrudRepository<Asset, Long> {

    @Query(value="SELECT new com.demo.demo.dto.AssetDto(a.id, a.fileName, a.filePath) FROM Asset a WHERE a.owner = ?1")
    List<AssetDto> findByOwner(User owner);

}
