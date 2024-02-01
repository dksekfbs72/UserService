package com.userservice.user.repository;

import com.userservice.user.domain.entity.Follow;
import com.userservice.user.domain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserAndFollowId(User user, User followUser);
    @Query(value = "select f.follow_user_id from follow f where f.user_id = :user", nativeQuery = true)
    Optional<List<Long>> findUsersByUserId(@Param(value = "user") Long user);

    @Query(value = "select f.user_id from follow f where f.follow_user_id = :user", nativeQuery = true)
    Optional<List<Long>> findUserByFollowId(@Param(value = "user") Long user);
}


