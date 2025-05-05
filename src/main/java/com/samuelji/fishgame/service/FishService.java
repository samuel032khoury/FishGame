package com.samuelji.fishgame.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samuelji.fishgame.dto.UserCaughtFishDTO;
import com.samuelji.fishgame.model.FishSpecies;
import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.model.UserCaughtFish;
import com.samuelji.fishgame.repository.FishSpeciesRepository;
import com.samuelji.fishgame.repository.UserCaughtFishRepository;
import com.samuelji.fishgame.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishService {
    private final UserRepository userRepository;
    private final UserCaughtFishRepository userCaughtFishRepository;
    private final FishSpeciesRepository fishSpeciesRepository;

    public UserCaughtFishDTO.Response catchFish(String userId) throws BadRequestException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        List<FishSpecies> fishList = fishSpeciesRepository.findByStatusTrue();
        FishSpecies fishChosen = selectRandomFishByProbability(fishList);

        double weight = generateRandomWeight(fishChosen);

        UserCaughtFish fish = new UserCaughtFish();
        fish.setUser(userRepository.getReferenceById(user.getId()));
        fish.setFishSpecies(fishSpeciesRepository.getReferenceById(fishChosen.getId()));
        fish.setWeight(weight);
        userCaughtFishRepository.save(fish);
        userRepository.save(user);
        UserCaughtFishDTO.Response response = new UserCaughtFishDTO.Response();
        response.setType(fishChosen.getType());
        response.setWeight(weight);
        response.setDescription(fishChosen.getDescription());
        response.setImageUrl(fish.getImageUrl());
        response.setRank(fish.getRank());
        return response;
    }

    private FishSpecies selectRandomFishByProbability(List<FishSpecies> fishList) {
        double totalProbability = fishList.stream().mapToDouble(FishSpecies::getProbability).sum();
        double randomProbability = Math.random() * totalProbability;
        double currentProb = 0;
        for (FishSpecies fish : fishList) {
            currentProb += fish.getProbability();
            if (currentProb > randomProbability) {
                return fish;
            }
        }
        return fishList.get(fishList.size() - 1);
    }

    private double generateRandomWeight(FishSpecies fish) {
        double meanWeight = fish.getMean();
        double weightVariation = fish.getStandardDeviation();

        double randomValue1 = Math.random();
        double randomValue2 = Math.random();

        double normalDistributedRandom = Math.sqrt(-2.0 * Math.log(randomValue1))
                * Math.sin(2.0 * Math.PI * randomValue2);

        return Math.max(fish.getMinWeight(), meanWeight + weightVariation * normalDistributedRandom);
    }

    @Transactional
    public int sellAllFish(String userId) throws BadRequestException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        List<UserCaughtFish> fishInventory = userCaughtFishRepository.findByUser_UserId(userId);
        if (fishInventory.isEmpty()) {
            return 0;
        }

        return sellFish(user, fishInventory);
    }

    @Transactional
    public int sellFishByType(String userId, String fishType, int amount) throws BadRequestException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (amount <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }
        Pageable pageRequest = PageRequest.of(0, amount);
        List<UserCaughtFish> fishInventoryOfType = userCaughtFishRepository.findByUser_UserIdAndFishSpecies_Type(userId,
                fishType, pageRequest);
        if (fishInventoryOfType.isEmpty()) {
            throw new BadRequestException("Not enough fish of this type to sell");
        }
        if (fishInventoryOfType.size() < amount) {
            throw new BadRequestException("Not enough fish of this type to sell");
        }

        return sellFish(user, fishInventoryOfType);
    }

    private int sellFish(User user, List<UserCaughtFish> fishInventory) {
        int totalRevenue = (int) (fishInventory.stream()
                .mapToDouble(fish -> fish.getWeight() * fish.getFishSpecies().getPrice()).sum() * 100);
        userCaughtFishRepository.deleteAll(fishInventory);
        user.setCoins(user.getCoins() + totalRevenue);
        userRepository.save(user);
        return totalRevenue;
    }
}
