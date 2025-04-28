package com.samuelji.fishgame.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.samuelji.fishgame.model.Fish;
import com.samuelji.fishgame.repository.FishRepository;

@RestController
@RequestMapping("/admin/fish")
public class FishManageController {

    @Autowired
    private FishRepository fishRepository;

    @GetMapping("/list")
    public List<Fish> getAllFishTypes() {
        return fishRepository.findAll();
    }

    @PostMapping("/create")
    public Fish createFish(@RequestBody Fish fish) {

        return fishRepository.save(fish);
    }

    @DeleteMapping("/delete{fid}")
    public Long deleteFish(@RequestParam Long fid) throws BadRequestException {
        if (fishRepository.findById(fid).isEmpty()) {
            throw new BadRequestException("No fish type has id " + fid + ".");
        }
        fishRepository.deleteById(fid);
        return fid;
    }
}
