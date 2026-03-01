package com.lgc.repository;

import com.lgc.entity.Product;
import com.lgc.entity.NavMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(NavMenu category);

    List<Product> findByCategory_Id(Long categoryId);

    // 🔥 추가 (slug 조회용)
    Optional<Product> findBySlug(String slug);

    //add
    // 🔥 slug 중복 체크용 (서비스에서 slug 생성 시 필요)
    boolean existsBySlug(String slug);

    // 🔥 제목 검색 (관리자 검색용으로 나중에 필요할 확률 높음)
    List<Product> findByTitleContaining(String keyword);

}