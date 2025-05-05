package com.samuelji.fishgame.service;

import org.springframework.stereotype.Service;

import com.samuelji.fishgame.dto.UserDTO;
import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean userExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public void createUser(UserDTO.Request request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        userRepository.save(user);
    }

    public UserDTO.BasicInfoResponse getUserBasicInfo(String userId) {
        User user = userRepository.findByUserId(userId).get();
        return new UserDTO.BasicInfoResponse(user.getUserName(), user.getRodType());
    }

    public UserDTO.FinanceInfoResponse getUserFinanceInfo(String userId) {
        User user = userRepository.findByUserId(userId).get();
        return new UserDTO.FinanceInfoResponse(user.getCoins(), user.getDiamonds());
    }

    public UserDTO.LevelInfoResponse getUserLevelInfo(String userId) {
        User user = userRepository.findByUserId(userId).get();
        return new UserDTO.LevelInfoResponse(user.getLevel(), user.getCurrentExperience(),
                user.getExperienceForNextLevel());
    }

    public UserDTO.InventoryInfoResponse getUserInventoryInfo(String userId) {
        User user = userRepository.findByUserId(userId).get();
        return new UserDTO.InventoryInfoResponse(user.getFishInventory());
    }
}
