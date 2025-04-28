package com.samuelji.fishgame.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.dto.FishSpeciesDTO;
import com.samuelji.fishgame.model.Fish;
import com.samuelji.fishgame.repository.FishRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/fish")
@AllArgsConstructor
public class FishManagementController {

    private FishRepository fishRepository;

    private FishSpeciesDTO.Response mapToResponse(Fish fish) {
        FishSpeciesDTO.Response response = new FishSpeciesDTO.Response();
        response.setId(fish.getId());
        response.setType(fish.getType());
        response.setDescription(fish.getDescription());
        response.setProbability(fish.getProbability());
        return response;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FishSpeciesDTO.Response>> getAllFishTypes() {
        List<Fish> fishList = fishRepository.findAll();
        if (fishList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<FishSpeciesDTO.Response> responseList = fishList.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/create")
    public ResponseEntity<FishSpeciesDTO.Response> createFish(@RequestBody FishSpeciesDTO.Request request) {
        Fish fish = new Fish();
        fish.setType(request.getType());
        fish.setDescription(request.getDescription());
        fish.setProbability(request.getProbability());
        fish.setSWeight(request.getSWeight());
        fish.setAWeight(request.getAWeight());
        fish.setBWeight(request.getBWeight());
        fish.setCWeight(request.getCWeight());
        fish.setMean(request.getMean());
        fish.setStandardDeviation(request.getStandardDeviation());
        fish.setStatus(request.getStatus());
        Fish savedFish = fishRepository.save(fish);
        return ResponseEntity.ok(mapToResponse(savedFish));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFish(@RequestParam Long fid) {
        if (fishRepository.findById(fid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        fishRepository.deleteById(fid);
        return ResponseEntity.ok().build();
    }
}
