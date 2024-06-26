package com.gongzone.central.location.mapper;

import com.gongzone.central.location.domain.LocationSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LocationMapper {
    List<LocationSearch> getLocationSearch(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
