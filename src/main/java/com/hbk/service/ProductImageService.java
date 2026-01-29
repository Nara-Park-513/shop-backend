package com.hbk.service;

import com.hbk.dto.ProductImageDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service //이 클래스는 비즈니스 로직 담당이라고 스프링에게 알려주는 표시
public class ProductImageService {

    //상품 이미지 종류 목록 스태틱 -> 클래스 하나당 한번만 파이널 -> 값 변경 불가
    private static final List<String> IMAGE_KEYS =
            List.of("p1", "p2", "p3", "p4");
    public List<ProductImageDto> getRandomWebImages(){
        List<ProductImageDto> result = new ArrayList<>();

        for (String key: IMAGE_KEYS){

            int seed = ThreadLocalRandom.current().nextInt(1, 10000);

            String url = String.format(
                    "https://picsum.photos/seed/%s-%d/400/300", key, seed
            );
            result.add(new ProductImageDto(key, url));
        }
        return result;
    }
}
