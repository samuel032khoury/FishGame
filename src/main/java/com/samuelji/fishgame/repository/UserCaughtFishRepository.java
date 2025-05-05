package com.samuelji.fishgame.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelji.fishgame.model.UserCaughtFish;

@Repository
public interface UserCaughtFishRepository extends JpaRepository<UserCaughtFish, Long> {
    List<UserCaughtFish> findByUser_UserId(String userId);

    List<UserCaughtFish> findByUser_UserIdAndType(String userId, String fishType, Pageable pageable);
}
