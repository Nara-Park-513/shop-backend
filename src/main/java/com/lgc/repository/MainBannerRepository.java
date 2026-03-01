package com.lgc.repository;

import com.lgc.entity.MainBanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainBannerRepository extends JpaRepository<MainBanner, Long> {
    Optional<MainBanner> findTopByOrderByIdDesc();
}