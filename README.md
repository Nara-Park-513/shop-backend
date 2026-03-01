# DAON – Integrated Commerce & ERP Backend

Spring Boot 기반 통합 상거래 백엔드 시스템입니다.  
쇼핑몰 주문 처리, 카카오페이 가결제, 향후 ERP/MES 확장을 고려한 구조로 설계되었습니다.

단순 CRUD 프로젝트가 아닌,  
외부 결제 API 연동과 결제 상태 관리까지 포함한 실전형 백엔드 구조를 목표로 개발했습니다.

---

## 🚀 Tech Stack

- **Spring Boot**
- Spring Data JPA
- Spring Security
- WebClient (외부 API 연동)
- MariaDB
- Gradle
- 카카오페이 Open API

---

## 🏗 시스템 구성

### 📦 주요 도메인

- **Payment**
- (확장 예정) Order
- (확장 예정) Inventory / ERP
- (확장 예정) MES 출고 처리

---

## 💳 카카오페이 가결제 구현

### 1️⃣ Ready (결제 요청 생성)

- 클라이언트로부터 결제 금액 수신
- 카카오페이 ready API 호출
- `tid` 발급
- Payment 상태 `READY`
- redirectUrl 반환

---

### 2️⃣ Approve (결제 승인)

- success 페이지에서 `pg_token` 수신
- 카카오페이 approve API 호출
- Payment 상태 `APPROVED` 업데이트
- (확장 가능) 주문 상태 `PAID` 처리

---

## 🔄 결제 흐름 요약

```text
Frontend
  ↓
POST /api/payments/kakaopay/ready
  ↓
KakaoPay 결제창 이동
  ↓
Success URL → pg_token 전달
  ↓
POST /api/payments/kakaopay/approve
  ↓
Payment 상태 APPROVED
🗄 Payment Entity 구조
필드	설명
id	PK
orderId	내부 주문 ID
tid	카카오 결제 고유 ID
amount	결제 금액
status	READY / APPROVED
createdAt	생성 시간
PaymentStatus Enum
public enum PaymentStatus {
    READY,
    APPROVED
}
📡 API 명세
✅ 결제 준비
POST /api/payments/kakaopay/ready

Request:

{
  "amount": 10000
}

Response:

{
  "orderId": "uuid",
  "redirectUrl": "https://..."
}
✅ 결제 승인
POST /api/payments/kakaopay/approve

Request:

{
  "orderId": "uuid",
  "pg_token": "..."
}
⚙️ 실행 방법
./gradlew bootRun

기본 실행 주소:

http://localhost:9999
🛠 환경 설정

application.properties

kakaopay.base-url=https://open-api.kakaopay.com
kakaopay.secret-key=${KAKAOPAY_SECRET_KEY}
kakaopay.cid=TC0ONETIME
kakaopay.partner-user-id=demo_user

kakaopay.redirect.success=http://localhost:3000/payment/kakao/success
kakaopay.redirect.cancel=http://localhost:3000/payment/kakao/cancel
kakaopay.redirect.fail=http://localhost:3000/payment/kakao/fail

환경 변수 설정 필요:

KAKAOPAY_SECRET_KEY=발급받은_시크릿키
🧠 개발 중 해결한 문제

KAKAOPAY_SECRET_KEY Placeholder 오류 해결

카카오페이 400 BAD_REQUEST (도메인 미등록 문제 해결)

WebClient 의존성 및 실행 오류 해결

module not specified 실행 문제 해결

amount 누락으로 인한 예외 처리 개선

📈 확장 계획

Order 도메인과 Payment 연동

ERP 재고 차감 자동화

MES 출고 처리 API 구현

결제 취소/환불 API 추가

결제 로그 및 리포트 기능

🎯 프로젝트 목표

외부 결제 API 연동 경험 확보

상태 기반 결제 처리 설계

ERP/MES 확장을 고려한 구조 설계

실무에 가까운 결제 흐름 구현