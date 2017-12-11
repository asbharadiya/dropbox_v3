package com.app.dropbox.repository;

import com.app.dropbox.dto.GroupDto;
import com.app.dropbox.entity.Group;
import com.app.dropbox.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {

    @Query(value="SELECT new com.app.dropbox.dto.GroupDto(g.id, g.name) from Group g WHERE g.owner = ?1")
    List<GroupDto> getGroupsByOwner(User user);

}
