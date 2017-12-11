package com.app.dropbox.repository;

import com.app.dropbox.entity.UserActivity;
import org.springframework.data.repository.CrudRepository;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {
}
