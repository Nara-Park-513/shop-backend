package com.lgc.controller;

import com.lgc.dto.ScrollBannerCreateReq;
import com.lgc.dto.ScrollBannerRes;
import com.lgc.service.ScrollBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor   
@RequestMapping("/api/scroll-banners")
public class ScrollBannerController {

    private final ScrollBannerService scrollBannerService;

    /** ✅ 목록 */
    @GetMapping
    public List<ScrollBannerRes> list() {
        return scrollBannerService.list();
    }

    /** ✅ 생성 (multipart/form-data) */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ScrollBannerRes create(@ModelAttribute ScrollBannerCreateReq req) {
        return scrollBannerService.create(req);
    }

    /** ✅ 삭제 */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        scrollBannerService.delete(id);
    }
}