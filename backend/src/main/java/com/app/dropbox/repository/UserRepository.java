package com.app.dropbox.repository;


import com.app.dropbox.dto.UserDto;
import com.app.dropbox.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

//    @Query(value="SELECT new com.app.dropbox.dto.UserDto(u.id, concat(u.firstName,' ',u.lastName) as username, u.email) from User u WHERE (u.firstName like '%?2%' or u.lastName like '%?2%' or u.email like '%?2%') and u.id <> ?1")
//    List<UserDto> searchUsers(Long userId, String q);

    @Query(value="SELECT new com.app.dropbox.dto.UserDto(u.id, concat(u.firstName,' ',u.lastName) as username, u.email) from User u WHERE u.id <> ?1")
    List<UserDto> searchUsers(Long userId);

}