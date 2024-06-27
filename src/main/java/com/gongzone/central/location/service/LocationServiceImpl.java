package com.gongzone.central.location.service;

import com.gongzone.central.location.domain.LocationSearch;
import com.gongzone.central.location.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    @Override
    public List<LocationSearch> getLocationSearch(Double latitude, Double longitude) {
        return locationMapper.getLocationSearch(latitude, longitude);
    }
}
