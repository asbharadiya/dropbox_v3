package com.app.dropbox.repository;

import com.app.dropbox.entity.Asset;
import com.app.dropbox.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AssetRepository extends CrudRepository<Asset, Long> {

    List<Asset> findByNameAndOwner(String name,User user);

    List<Asset> findByParent(Asset a);

    List<Asset> findByName(String name);

    @Query(value="SELECT count(*) from Asset a WHERE a.name = ?1")
    int findCountByName(String name);
}
