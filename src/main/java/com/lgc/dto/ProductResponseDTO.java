package com.lgc.dto;

import com.lgc.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String title;
    private String desc;
    private Integer price;

    private String imageUrl;
    private String slug;

    private Long categoryId;
    private String categoryName;

    private List<ProductSizeDTO> sizes;
    private List<ProductSpecDTO> specs;

    // 🔥 반드시 있어야 함
    public static ProductResponseDTO from(Product e) {
        return ProductResponseDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .desc(e.getDesc())
                .price(e.getPrice())
                .imageUrl(e.getImageUrl())
                .slug(e.getSlug())
                .categoryId(e.getCategory() != null ? e.getCategory().getId() : null)
                .categoryName(e.getCategory() != null ? e.getCategory().getName() : null)

                // ✅ 사이즈 매핑
                .sizes(
                        e.getSizes().stream()
                                .map(s -> new ProductSizeDTO(
                                        s.getSize(),
                                        s.getStock()
                                ))
                                .toList()
                )

                // ✅ 상품정보고시 매핑
                .specs(
                        e.getSpecs().stream()
                                .map(s -> new ProductSpecDTO(
                                        s.getLabel(),
                                        s.getValue()
                                ))
                                .toList()
                )
                .build();
    }
}