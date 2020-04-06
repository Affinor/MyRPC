package com.jin.myconfig.center.springboot.myconfigcenterspringboottest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
}
