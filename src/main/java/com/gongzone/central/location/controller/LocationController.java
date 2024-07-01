package com.gongzone.central.location.controller;

import com.gongzone.central.location.domain.LocationSearch;
import com.gongzone.central.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {


    private final LocationService locationService;

    @GetMapping("/location")
    public List<LocationSearch> getLocation(@RequestParam Double latitude, @RequestParam Double longitude) {
        return locationService.getLocationSearch(latitude, longitude);
    }
}
