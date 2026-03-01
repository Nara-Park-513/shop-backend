package com.lgc.dto;

import com.lgc.entity.MainBanner;

public class MainBannerMapper {
    public static MainBannerResponse toResponse(MainBanner b) {
        if (b == null) return null;
        return MainBannerResponse.builder()
                .id(b.getId())
                .imageUrl(b.getImageUrl())
                .linkUrl(b.getLinkUrl())
                .build();
    }
}