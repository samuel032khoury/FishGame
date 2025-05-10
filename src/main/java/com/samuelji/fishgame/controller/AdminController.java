package com.samuelji.fishgame.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuelji.fishgame.dto.FishSpeciesDTO;
import com.samuelji.fishgame.dto.ShopItemDTO;
import com.samuelji.fishgame.model.FishSpecies;
import com.samuelji.fishgame.model.ShopItem;
import com.samuelji.fishgame.repository.FishSpeciesRepository;
import com.samuelji.fishgame.repository.ShopItemRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private FishSpeciesRepository fishSpeciesRepository;
    private ShopItemRepository shopItemRepository;

    @GetMapping
    public String adminDashboard() {
        return "Admin Dashboard";
    }

    private FishSpeciesDTO.Response mapToFishSpeciesDTOResponse(FishSpecies fish) {
        FishSpeciesDTO.Response response = new FishSpeciesDTO.Response();
        response.setId(fish.getId());
        response.setType(fish.getType());
        response.setDescription(fish.getDescription());
        response.setProbability(fish.getProbability());
        response.setPrice(fish.getPrice());
        return response;
    }

    private ShopItemDTO mapToShopItem(ShopItem shopItem) {
        ShopItemDTO response = new ShopItemDTO();
        response.setId(shopItem.getId());
        response.setName(shopItem.getName());
        response.setDescription(shopItem.getDescription());
        response.setCategory(shopItem.getCategory());
        response.setPrice(shopItem.getPrice());
        response.setLimited(shopItem.getLimited());
        response.setStock(shopItem.getStock());
        return response;
    }

    @GetMapping("/fish-species")
    public ResponseEntity<List<FishSpeciesDTO.Response>> getAllFishSpecies() {
        List<FishSpecies> fishSpeciesList = fishSpeciesRepository.findAll();
        if (fishSpeciesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<FishSpeciesDTO.Response> responseList = fishSpeciesList.stream()
                .map(this::mapToFishSpeciesDTOResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/fish-species/create")
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
        return ResponseEntity.ok(mapToFishSpeciesDTOResponse(savedFishSpecies));
    }

    @DeleteMapping("/fish-species/delete")
    public ResponseEntity<Void> deleteFishSpecies(@RequestParam Long fid) {
        if (fishSpeciesRepository.findById(fid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        fishSpeciesRepository.deleteById(fid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shop-item")
    public ResponseEntity<List<ShopItemDTO>> getAllShopItem() {
        List<ShopItem> shopItemList = shopItemRepository.findAll();
        if (shopItemList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ShopItemDTO> responseList = shopItemList.stream()
                .map(this::mapToShopItem)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/shop-item/create")
    public ResponseEntity<ShopItemDTO> createShopItem(@RequestBody ShopItemDTO request) {
        ShopItem shopItem = new ShopItem();
        shopItem.setName(request.getName());
        shopItem.setDescription(request.getDescription());
        shopItem.setCategory(request.getCategory());
        shopItem.setPrice(request.getPrice());
        shopItem.setLimited(request.getLimited());
        if (request.getLimited() != null && request.getLimited()) {
            shopItem.setStock(request.getStock());
        } else {
            shopItem.setStock(null);
        }
        ShopItem savedShopItem = shopItemRepository.save(shopItem);
        return ResponseEntity.ok(mapToShopItem(savedShopItem));
    }

    @DeleteMapping("/shop-item/delete")
    public ResponseEntity<Void> deleteShopItem(@RequestParam Long pid) {
        if (shopItemRepository.findById(pid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        shopItemRepository.deleteById(pid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shop-item/update/{pid}")
    public ResponseEntity<ShopItemDTO> updateShopItem(@PathVariable Long pid, @RequestBody ShopItemDTO request) {
        if (shopItemRepository.findById(pid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ShopItem shopItem = new ShopItem();
        shopItem.setId(pid);
        shopItem.setName(request.getName());
        shopItem.setDescription(request.getDescription());
        shopItem.setCategory(request.getCategory());
        shopItem.setPrice(request.getPrice());
        shopItem.setLimited(request.getLimited());
        if (request.getLimited() != null && request.getLimited()) {
            shopItem.setStock(request.getStock());
        } else {
            shopItem.setStock(null);
        }
        ShopItem savedShopItem = shopItemRepository.save(shopItem);
        return ResponseEntity.ok(mapToShopItem(savedShopItem));
    }
}