package com.lgc.service;

import com.lgc.dto.BannerCreateRequest;
import com.lgc.dto.BannerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {
    List<BannerResponse> list();
    List<BannerResponse> listVisible();
    BannerResponse create(BannerCreateRequest req, MultipartFile image);
}