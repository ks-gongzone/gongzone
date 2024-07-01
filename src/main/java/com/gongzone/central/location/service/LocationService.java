package com.gongzone.central.location.service;

import com.gongzone.central.location.domain.LocationSearch;

import java.util.List;

public interface LocationService {
    List<LocationSearch> getLocationSearch(Double latitude, Double longitude);
}