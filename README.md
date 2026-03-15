# DAON – Integrated Commerce Platform (Backend)

Spring Boot 기반으로 개발한 **통합 쇼핑몰 백엔드 프로젝트**입니다.  
Next.js 프론트엔드와 연동하여 상품 조회, 주문 처리, 카카오페이 결제 요청 및 승인 흐름을 처리합니다.

단순 API 서버 구현을 넘어,  
**ERP 및 MES 확장을 고려한 통합 상거래 플랫폼**을 목표로 개발했습니다.

---

## 📌 Project Overview

DAON은 사용자 관점의 쇼핑몰 기능과  
관리/운영 관점의 ERP·MES 확장 가능성을 함께 고려한 프로젝트입니다.

백엔드는 Spring Boot 기반으로 구현되었으며,  
프론트엔드와 REST API 방식으로 통신하며 주문 및 결제 프로세스를 처리합니다.

### 핵심 목표
- 쇼핑몰 주문 및 결제 API 구현
- 카카오페이 결제 연동 처리
- 프론트엔드와의 안정적인 API 통신 구조 설계
- ERP / MES와 연결 가능한 통합 백엔드 구조 마련

---

## 🚀 Tech Stack

### Backend
- **Spring Boot**
- **Java**
- Spring Web
- Spring Data JPA
- Lombok

### Database
- MySQL / MariaDB

### Features / APIs
- REST API 기반 주문 처리
- 카카오페이 결제 연동
- JPA 기반 데이터 처리

### Dev Tools
- Git
- GitHub
- IntelliJ IDEA / VS Code

---

## ✨ Main Features

### 🛍 상품 및 주문
- 상품 조회 API 제공
- 주문 정보 저장 및 처리
- 프론트엔드 주문 페이지와 연동
- 주문 요청 데이터 검증

### 💳 카카오페이 가결제 연동
- Ready → Redirect → Approve 흐름 처리
- 결제 요청 정보 구성
- 결제 승인 완료 후 결과 반환
- 결제 실패 / 취소 상황 대응 구조 설계

### 🔗 프론트엔드 연동
- `/api/payments/kakaopay/ready` 요청 처리
- 카카오 결제창 redirect URL 반환
- success 요청에서 `pg_token` 전달받아 승인 처리
- 프론트엔드 요청 흐름에 맞춘 응답 구조 구성

---

## 🔄 Payment Flow

1. 사용자가 프론트엔드에서 주문하기 버튼을 클릭
2. 프론트엔드가 백엔드 결제 준비 API를 호출
3. 백엔드가 카카오페이 Ready API를 호출
4. 카카오 결제창 URL을 프론트엔드에 반환
5. 사용자가 카카오 결제창에서 결제를 진행
6. 결제 완료 후 success 페이지로 이동
7. `pg_token`을 백엔드 Approve API로 전달
8. 최종 결제 승인 및 완료 처리

---

## 🛠 Getting Started

### 1. Clone repository
```bash
git clone https://github.com/Nara-Park-513/mes-backend.git
cd mes-backend
```

### 2. Configure environment
`application.yml` 또는 `application.properties`에 데이터베이스 및 환경 설정을 추가합니다.

예시:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mesdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

카카오페이 연동을 위한 API 키 및 관련 설정도 함께 추가해야 합니다.

### 3. Build project
```bash
./gradlew clean build
```

### 4. Run server
```bash
./gradlew bootRun
```

### 5. Open in browser
```bash
http://localhost:8080
```

> 프론트엔드 서버(`http://localhost:3000`)와 함께 실행해야 주문 및 결제 기능이 정상적으로 동작합니다.

---

## ⚠️ Troubleshooting

개발 과정에서 아래와 같은 이슈를 해결했습니다.

- 프론트엔드 ↔ 백엔드 API 연동 문제 해결
- 카카오페이 결제 요청 데이터 누락 문제 점검
- 환경변수 및 설정 파일 구성 오류 해결
- CORS 및 요청/응답 구조 문제 정리
- 데이터베이스 연결 설정 문제 해결

---

## 🎯 Project Purpose

- 쇼핑몰 + ERP / MES 확장이 가능한 통합 구조 설계
- 외부 결제 API 연동 경험 축적
- 실제 상용 서비스 흐름과 유사한 결제 프로세스 구현
- 프론트엔드와 백엔드 간 API 협업 구조 이해

---

## 🔮 Future Improvements

- 주문 내역 조회 API 추가
- 결제 상태 및 결제 이력 관리 기능 확장
- 사용자 인증 및 권한 관리 기능 추가
- 출고 / 재고 / 품질 관리 기능 연동
- ERP / MES 도메인 확장

---

## 👩‍💻 Author

DAON Integrated Commerce Platform Backend Project
