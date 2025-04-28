package com.samuelji.fishgame.service;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samuelji.fishgame.dto.CaughtFishDTO;
import com.samuelji.fishgame.model.Fish;
import com.samuelji.fishgame.model.FishSpecies;
import com.samuelji.fishgame.model.User;
import com.samuelji.fishgame.repository.FishImagesRepository;
import com.samuelji.fishgame.repository.FishRepository;
import com.samuelji.fishgame.repository.FishSpeciesRepository;
import com.samuelji.fishgame.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishService {
    private final UserRepository userRepository;
    private final FishRepository fishRepository;
    private final FishSpeciesRepository fishSpeciesRepository;
    private final FishImagesRepository fishImagesRepository;

    public CaughtFishDTO.Response catchFish(String userId) throws BadRequestException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        List<FishSpecies> fishList = fishSpeciesRepository.findByStatusTrue();
        FishSpecies fishChosen = selectRandomFishByProbability(fishList);

        double weight = generateRandomWeight(fishChosen);
        String rank = determineQualityRank(weight, fishChosen);
        String imageUrl = selectFishImageBasedOnWeight(weight, fishChosen);

        Fish fish = new Fish();
        fish.setUserId(userId);
        fish.setType(fishChosen.getType());
        fish.setPrice(fishChosen.getPrice());
        fish.setWeight(weight);
        fish.setUrl(imageUrl);
        fish.setDescription(fishChosen.getDescription());
        fishRepository.save(fish);
        user.getFishInventory().put(fish.getId(), fish);
        userRepository.save(user);
        CaughtFishDTO.Response response = new CaughtFishDTO.Response();
        response.setFish(fishChosen.getType());
        response.setWeight(weight);
        response.setDescription(fishChosen.getDescription());
        response.setImageUrl(imageUrl);
        response.setRank(rank);
        response.setStatus("success");
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

    private String selectFishImageBasedOnWeight(double weight, FishSpecies fishChosen) {
        String defaultImgUrl = "https://s3.us-west-1.amazonaws.com/fishing.web.images/Fishing+Game+Images/Other/pearl.png";

        return fishImagesRepository.findByType(fishChosen.getType())
                .map(fishImages -> {
                    String qualityRank = determineQualityRank(weight, fishChosen);
                    return Optional.ofNullable(fishImages.getImages().get(qualityRank))
                            .orElse(defaultImgUrl);
                })
                .orElse(defaultImgUrl);
    }

    private String determineQualityRank(double weight, FishSpecies fishChosen) {
        if (weight > fishChosen.getSWeight())
            return "SS";
        if (weight > fishChosen.getAWeight())
            return "S";
        if (weight > fishChosen.getBWeight())
            return "A";
        if (weight > fishChosen.getCWeight())
            return "B";
        return "C";
    }

    @Transactional
    public int sellAllFish(String userId) throws BadRequestException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        List<Fish> fishInventory = fishRepository.findByUserId(userId);
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
        List<Fish> fishInventoryOfType = fishRepository.findByUserIdAndType(userId, fishType, pageRequest);
        if (fishInventoryOfType.isEmpty()) {
            throw new BadRequestException("Not enough fish of this type to sell");
        }
        if (fishInventoryOfType.size() < amount) {
            throw new BadRequestException("Not enough fish of this type to sell");
        }

        return sellFish(user, fishInventoryOfType);
    }

    private int sellFish(User user, List<Fish> fishInventory) {
        int totalRevenue = (int) (fishInventory.stream()
                .mapToDouble(fish -> fish.getWeight() * fish.getPrice()).sum() * 100);
        user.getFishInventory().keySet().removeAll(fishInventory.stream()
                .map(Fish::getId).toList());
        fishRepository.deleteAll(fishInventory);
        user.setCoins(user.getCoins() + totalRevenue);
        userRepository.save(user);
        return totalRevenue;
    }
}
