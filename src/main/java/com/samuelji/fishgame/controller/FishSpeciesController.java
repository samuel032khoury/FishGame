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
import com.samuelji.fishgame.model.FishSpecies;
import com.samuelji.fishgame.repository.FishSpeciesRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/fish-species")
@AllArgsConstructor
public class FishSpeciesController {

    private FishSpeciesRepository fishSpeciesRepository;

    private FishSpeciesDTO.Response mapToResponse(FishSpecies fish) {
        FishSpeciesDTO.Response response = new FishSpeciesDTO.Response();
        response.setId(fish.getId());
        response.setType(fish.getType());
        response.setDescription(fish.getDescription());
        response.setProbability(fish.getProbability());
        response.setPrice(fish.getPrice());
        return response;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FishSpeciesDTO.Response>> getAllFishSpecies() {
        List<FishSpecies> fishSpeciesList = fishSpeciesRepository.findAll();
        if (fishSpeciesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<FishSpeciesDTO.Response> responseList = fishSpeciesList.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/create")
    public ResponseEntity<FishSpeciesDTO.Response> createFishSpecies(@RequestBody FishSpeciesDTO.Request request) {
        FishSpecies fishSpecies = new FishSpecies();
        fishSpecies.setType(request.getType());
        fishSpecies.setDescription(request.getDescription());
        fishSpecies.setProbability(request.getProbability());
        fishSpecies.setSWeight(request.getSWeight());
        fishSpecies.setAWeight(request.getAWeight());
        fishSpecies.setBWeight(request.getBWeight());
        fishSpecies.setCWeight(request.getCWeight());
        fishSpecies.setMean(request.getMean() != null ? request.getMean() : request.getBWeight());
        fishSpecies.setStandardDeviation(request.getStandardDeviation());
        fishSpecies.setStatus(request.getStatus());
        fishSpecies.setPrice(request.getPrice());
        fishSpecies.setMinWeight(request.getCWeight());
        FishSpecies savedFishSpecies = fishSpeciesRepository.save(fishSpecies);
        return ResponseEntity.ok(mapToResponse(savedFishSpecies));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFishSpecies(@RequestParam Long fid) {
        if (fishSpeciesRepository.findById(fid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        fishSpeciesRepository.deleteById(fid);
        return ResponseEntity.ok().build();
    }
}
