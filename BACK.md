# Backend 기술명세서 - Coin Sensor

## 🎯 백엔드 구현 요구사항

### 1. 실시간 데이터 처리

#### 1.1 거래소 API 연동

- **바이낸스 선물 거래소 API**
    - WebSocket 연결로 실시간 가격/거래량 데이터 수신
    - REST API로 히스토리컬 데이터 조회
    - Rate Limit 관리 및 재연결 로직

- **추후 확장 거래소**
    - 업비트 API 연동
    - 빗썸 API 연동
    - 통합 데이터 정규화

#### 1.2 이상 패턴 감지 엔진

- **거래량 급증 감지**
    - 이동평균 대비 거래량 증가율 계산
    - 임계값 초과 시 알림 트리거
    - 봉 마감 시점 기준 분석

- **가격 변동률 감지**
    - 단기/장기 변동률 비교
    - 변동성 지표 계산
    - TOP20 고변동성 코인 선별

#### 1.3 실시간 알림 시스템

- **WebSocket 서버**
    - Spring WebSocket 구현
    - 클라이언트별 구독 관리
    - 메시지 브로드캐스팅

- **알림 개인화**
    - 사용자별 알림 설정 저장
    - 코인별 임계값 설정
    - 알림 히스토리 관리

### 2. 데이터 저장 및 관리

#### 2.1 데이터베이스 설계

- **MySQL 8.0 스키마**
    - 코인 정보 테이블
    - 가격/거래량 히스토리 테이블
    - 사용자 설정 테이블
    - 알림 로그 테이블

- **Redis 캐싱**
    - 실시간 데이터 캐싱
    - 세션 관리
    - 임시 데이터 저장

#### 2.2 데이터 수집 스케줄러

- **Spring Scheduler**
    - 정기적 데이터 수집 작업
    - 배치 처리 작업
    - 데이터 정리 작업

### 3. AI 기반 시장 분석

#### 3.1 데이터 분석 엔진

- **OHLCV 데이터 분석**
    - 1개월~1년 비트코인 데이터 처리
    - 통계적 분석 알고리즘
    - 패턴 인식 로직

#### 3.2 AI 리포트 생성

- **일일 시장 분석**
    - 외부 AI API 연동 (OpenAI, Claude)
    - 분석 결과 텍스트 생성
    - 리포트 템플릿 관리

### 4. 시장 지표 서비스

#### 4.1 롱숏 비율 API

- **바이낸스 롱숏 데이터**
    - 실시간 포지션 비율 수집
    - 히스토리컬 데이터 저장
    - 비율 변화 추이 분석

#### 4.2 공포탐욕지수 API

- **외부 API 연동**
    - Fear & Greed Index 데이터 수집
    - 지수 계산 로직 구현
    - 단계별 분류 (극단적 공포~희망)

#### 4.3 김치프리미엄 계산

- **국내외 거래소 가격 비교**
    - 바이낸스 vs 업비트 가격 차이
    - 프리미엄 비율 계산
    - 일봉 차트 데이터 생성

### 5. 커뮤니티 기능

#### 5.1 실시간 채팅 시스템

- **WebSocket 채팅 서버**
    - 메인 채팅룸 관리
    - 코인별 채팅룸 분리
    - 익명 사용자 세션 관리

#### 5.2 키워드 분석

- **채팅 메시지 분석**
    - 실시간 키워드 추출
    - 빈도수 계산 및 순위
    - 불용어 필터링

#### 5.3 투자 도구 API

- **돌림판 결과 API**
    - 랜덤 결과 생성
    - 결과 통계 수집
    - 사용자별 히스토리

### 6. 외부 데이터 연동

#### 6.1 뉴스 API 연동

- **뉴스 소스 통합**
    - CoinDesk, CoinTelegraph API
    - RSS 피드 파싱
    - 뉴스 분류 및 필터링

#### 6.2 경제 캘린더 API

- **경제 이벤트 데이터**
    - Yahoo Finance, Alpha Vantage
    - 중요도별 분류
    - 일정 알림 기능

### 7. 보안 및 인증

#### 7.1 API 보안

- **Rate Limiting**
    - IP별 요청 제한
    - API 키 기반 인증
    - DDoS 방어

#### 7.2 데이터 보안

- **암호화**
    - 민감 데이터 암호화
    - HTTPS 통신
    - API 키 보안 관리

#### 8 사용자 기반 데이터 제공

- 거래소 코인, AI 분석, 뉴스 기사에 대한 좋아요/싫어요
- 사용자 관심 코인
- 사용자 클릭 코인
- 사용자에게 인기 있는 탐지된 코인
- 사람들이 관심을 가지고 있는 코인
- 인기 채팅 주제

## 암호화폐 뉴스 API 제한 정보

### CoinGecko API 제한

#### 무료 플랜

- 분당 10-50 요청
- 월 10,000 요청
- 뉴스 API는 제한적 제공

### CryptoCompare API 제한

#### 무료 플랜

- 시간당 100,000 요청
- 초당 50 요청
- 뉴스 API: 일 2,000 요청

## 코인클릭,

## 권장사항

초기 개발에는 **CryptoCompare** 무료 플랜 추천 (더 관대한 제한)

## 🛠 기술 스택

### Core Framework

- **Java 21+**
- **Spring Boot 3.5.6**
- **Spring Security**
- **Spring WebSocket**

### 데이터베이스

- **MySQL 8.0**
- **Redis 7.0**
- **Spring Data JPA**
- **HikariCP (Connection Pool)**

### 빌드 및 배포

- **Maven**
- **Docker & Docker Compose**
- **Nginx (Reverse Proxy)**

### 외부 연동

- **WebClient (HTTP Client)**
- **Jackson (JSON Processing)**
- **WebSocket Client**

### 모니터링

- **Spring Actuator**
- **Micrometer**
- **Logback**

## 📡 API 설계

### 1. 실시간 데이터 API

#### WebSocket Endpoints

```
/ws/coins - 실시간 코인 데이터
/ws/alerts - 이상 패턴 알림
/ws/chat - 실시간 채팅
```

#### REST Endpoints

```
GET /api/coins/abnormal - 이상 코인 리스트
GET /api/coins/volatile - 고변동성 코인 TOP20
GET /api/coins/{symbol}/history - 코인 히스토리 데이터
```

### 2. 시장 지표 API

```
GET /api/market/fear-greed - 공포탐욕지수
GET /api/market/long-short - 롱숏 비율
GET /api/market/kimchi-premium - 김치프리미엄
GET /api/market/overview - 시장 개요
```

### 3. 분석 API

```
GET /api/analysis/daily-report - AI 일일 리포트
GET /api/analysis/technical/{symbol} - 기술적 분석
GET /api/analysis/patterns - 패턴 분석 결과
```

### 4. 커뮤니티 API

```
GET /api/chat/channels - 채팅룸 리스트
GET /api/chat/keywords - 인기 키워드
POST /api/tools/roulette - 투자 돌림판
```

### 5. 정보 서비스 API

```
GET /api/news - 뉴스 피드
GET /api/calendar - 경제 캘린더
GET /api/calendar/events - 경제 이벤트
```

## 🔄 데이터 플로우

### 1. 실시간 데이터 처리

```
거래소 API → WebSocket Client → 데이터 처리 → Redis Cache → WebSocket Server → Frontend
```

### 2. 이상 패턴 감지

```
실시간 데이터 → 분석 엔진 → 임계값 비교 → 알림 트리거 → WebSocket 전송
```

### 3. 배치 데이터 처리

```
Scheduler → 데이터 수집 → 분석 처리 → DB 저장 → 캐시 업데이트
```

## 🏗 아키텍처 설계

### 1. 레이어 구조

```
Controller Layer (REST API, WebSocket)
↓
Service Layer (비즈니스 로직)
↓
Repository Layer (데이터 액세스)
↓
Database Layer (MySQL, Redis)
```

### 2. 모듈 구조

```
coin-sensor-back/
├── src/main/java/com/coinsensor/
│   ├── controller/     # REST API 컨트롤러
│   ├── websocket/      # WebSocket 핸들러
│   ├── service/        # 비즈니스 로직
│   ├── repository/     # 데이터 액세스
│   ├── entity/         # JPA 엔티티
│   ├── dto/           # 데이터 전송 객체
│   ├── config/        # 설정 클래스
│   ├── scheduler/     # 스케줄러
│   └── util/          # 유틸리티
```

### 3. 외부 연동 모듈

```
external/
├── binance/           # 바이낸스 API 클라이언트
├── upbit/            # 업비트 API 클라이언트
├── news/             # 뉴스 API 클라이언트
└── ai/               # AI API 클라이언트
```

## 📊 성능 최적화

### 1. 캐싱 전략

- **Redis 캐싱**
    - 실시간 데이터 캐싱 (TTL: 1분)
    - API 응답 캐싱 (TTL: 5분)
    - 세션 데이터 캐싱

### 2. 데이터베이스 최적화

- **인덱스 최적화**
- **쿼리 최적화**
- **커넥션 풀 튜닝**

### 3. 비동기 처리

- **@Async 어노테이션**
- **CompletableFuture**
- **메시지 큐 활용**

## 🔧 배포 및 운영

### 1. Docker 컨테이너화

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
  mysql:
    image: mysql:8.0
  redis:
    image: redis:7.0
  nginx:
    image: nginx:alpine
```

### 2. 모니터링

- **Spring Actuator** 헬스체크
- **로그 모니터링**
- **메트릭 수집**

### 3. 백업 및 복구

- **데이터베이스 백업**
- **설정 파일 백업**
- **장애 복구 절차**