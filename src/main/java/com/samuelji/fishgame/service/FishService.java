package com.samuelji.fishgame.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.samuelji.fishgame.model.Fish;
import com.samuelji.fishgame.model.FishImages;
import com.samuelji.fishgame.model.Inventory;
import com.samuelji.fishgame.repository.FishImagesRepository;
import com.samuelji.fishgame.repository.FishRepository;
import com.samuelji.fishgame.repository.InventoryRepository;
import com.samuelji.fishgame.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishService {
    private final UserRepository userRepository;
    private final FishRepository fishRepository;
    private final FishImagesRepository fishImagesRepository;
    private final InventoryRepository inventoryRepository;

    public Map<String, Object> catchFish(String userId) {
        List<Fish> fishList = fishRepository.findByStatusTrue();
        Fish fishChosen = probabilityHelper(fishList);

        double weight = weightGenerator(fishChosen);
        String imageUrl = imageHelper(weight, fishChosen);

        Inventory inventory = new Inventory();
        inventory.setUserId(userId);
        inventory.setType(fishChosen.getType());
        inventory.setPrice(1.0);
        inventory.setWeight(weight);
        inventory.setUrl(imageUrl);
        inventory.setDescription(fishChosen.getDescription());
        inventoryRepository.save(inventory);

        return Map.of(
                "fish", fishChosen.getType(),
                "weight", weight,
                "description", fishChosen.getDescription(),
                "imageUrl", imageUrl,
                "status", "Successfully caught a fish!");

    }

    private Fish probabilityHelper(List<Fish> fishList) {
        double totalProbability = fishList.stream().mapToDouble(Fish::getProbability).sum();
        double randomProbability = Math.random() * totalProbability;
        double currentProb = 0;
        for (Fish fish : fishList) {
            currentProb += fish.getProbability();
            if (currentProb > randomProbability) {
                return fish;
            }
        }
        return fishList.get(fishList.size() - 1);
    }

    private double weightGenerator(Fish fish) {
        double mean = fish.getMean();
        double stdDev = fish.getStandardDeviation();
        double u1 = Math.random();
        double u2 = Math.random();
        double randStdNormal = Math.sqrt(-2.0 * Math.log(u1)) * Math.sin(2.0 * Math.PI * u2);
        return mean + stdDev * randStdNormal;
    }

    private String imageHelper(double weight, Fish fishChosen) {
        String defaultImgUrl = "https://s3.us-west-1.amazonaws.com/fishing.web.images/Fishing+Game+Images/Other/pearl.png";

        Optional<FishImages> fishImgOpt = fishImagesRepository.findByType(fishChosen.getType());
        if (fishImgOpt.isEmpty())
            return defaultImgUrl;

        FishImages fishImg = fishImgOpt.get();
        String score = evaluate(weight, fishChosen);

        if (fishImg.getImages() != null && fishImg.getImages().get(score) != null) {
            return fishImg.getImages().get(score);
        }
        return defaultImgUrl;
    }

    private String evaluate(double weight, Fish fishChosen) {
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
    public Map<String, Object> sellFish(String userId) {
        List<Inventory> inventories = inventoryRepository.findByUserId(userId);
        if (inventories.isEmpty()) {
            return Map.of("revenue", 0);
        }
        double totalRevenue = inventories.stream()
                .mapToDouble(inventory -> inventory.getWeight() * inventory.getPrice()).sum();
        inventoryRepository.deleteAll(inventories);
        userRepository.findByUserId(userId).ifPresent(user -> {
            user.setCoins(user.getCoins() + (int) totalRevenue);
            userRepository.save(user);
        });

        return Map.of("revenue", totalRevenue);
    }
}
