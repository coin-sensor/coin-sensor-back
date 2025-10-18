# 도메인 구조 문서 (엔티티별 독립 패키지)

## 📁 패키지 구조

각 테이블(엔티티)마다 완전히 독립된 패키지로 분리되어 있습니다.
`com.coinsensor` 바로 하위에 각 엔티티 패키지가 위치합니다.

```
com.coinsensor/
├── coin/               # 코인 정보
├── exchange/           # 거래소 정보
├── timeframe/          # 시간 프레임
├── coinohlcv/          # OHLCV 데이터
├── detectedcoin/       # 탐지된 코인
├── detectiongroup/     # 탐지 그룹
├── detectioncriteria/  # 탐지 기준
├── economicevent/      # 경제 일정
├── votetopic/          # 투표 주제
├── voteoption/         # 투표 옵션
├── uservote/           # 사용자 투표
├── btcaireport/        # BTC AI 리포트
├── chatban/            # 채팅 차단
├── chatmessage/        # 채팅 메시지
├── chatroom/           # 채팅방
├── user/               # 사용자
├── kimchipremium/      # 김치 프리미엄
├── feargreedindex/     # 공포 탐욕 지수
├── newsarticle/        # 뉴스 기사
├── common/             # 공통 설정
├── external/           # 외부 API 연동
├── scheduler/          # 스케줄러
├── websocket/          # 웹소켓
└── util/               # 유틸리티
```

## 🏗️ 각 패키지 표준 구조

```
com.coinsensor.{entity_name}/
├── dto/
│   ├── request/     # 요청 DTO
│   └── response/    # 응답 DTO
├── entity/          # JPA 엔티티
├── repository/      # Spring Data JPA Repository
├── service/         # 서비스 인터페이스 및 구현체
└── controller/      # REST API 컨트롤러
```

### 패키지 경로 예시
- Entity: `com.coinsensor.coin.entity.Coin`
- Repository: `com.coinsensor.coin.repository.CoinRepository`
- Service: `com.coinsensor.coin.service.CoinService`
- Controller: `com.coinsensor.coin.controller.CoinController`

---

## 📋 엔티티 목록 (19개)

### 1. coin - 코인 정보
- **Entity**: `Coin.java`
- **Repository**: `CoinRepository.java`
- **Service**: `CoinService.java`, `CoinServiceImpl.java`
- **Controller**: `CoinController.java`
- **DTO**: `CoinResponse.java`

### 2. exchange - 거래소 정보
- **Entity**: `Exchange.java`
- **Repository**: `ExchangeRepository.java`

### 3. timeframe - 시간 프레임
- **Entity**: `Timeframe.java`
- **Repository**: `TimeframeRepository.java`

### 4. coinohlcv - OHLCV 데이터
- **Entity**: `CoinOhlcv.java`
- **Repository**: `CoinOhlcvRepository.java`

### 5. detectedcoin - 탐지된 코인
- **Entity**: `DetectedCoin.java`
- **Repository**: `DetectedCoinRepository.java`

### 6. detectiongroup - 탐지 그룹
- **Entity**: `DetectionGroup.java`
- **Repository**: `DetectionGroupRepository.java`

### 7. detectioncriteria - 탐지 기준
- **Entity**: `DetectionCriteria.java`
- **Repository**: `DetectionCriteriaRepository.java`

### 8. economicevent - 경제 일정
- **Entity**: `EconomicEvent.java`
- **Repository**: `EconomicEventRepository.java`

### 9. votetopic - 투표 주제
- **Entity**: `VoteTopic.java`
- **Repository**: `VoteTopicRepository.java`

### 10. voteoption - 투표 옵션
- **Entity**: `VoteOption.java`
- **Repository**: `VoteOptionRepository.java`

### 11. uservote - 사용자 투표
- **Entity**: `UserVote.java`
- **Repository**: `UserVoteRepository.java`

### 12. btcaireport - BTC AI 리포트
- **Entity**: `BtcAiReport.java`
- **Repository**: `BtcAiReportRepository.java`

### 13. chatban - 채팅 차단
- **Entity**: `ChatBan.java`
- **Repository**: `ChatBanRepository.java`

### 14. chatmessage - 채팅 메시지
- **Entity**: `ChatMessage.java`
- **Repository**: `ChatMessageRepository.java`

### 15. chatroom - 채팅방
- **Entity**: `ChatRoom.java`
- **Repository**: `ChatRoomRepository.java`

### 16. user - 사용자
- **Entity**: `User.java`
- **Repository**: `UserRepository.java`

### 17. kimchipremium - 김치 프리미엄
- **Entity**: `KimchiPremium.java`
- **Repository**: `KimchiPremiumRepository.java`

### 18. feargreedindex - 공포 탐욕 지수
- **Entity**: `FearGreedIndex.java`
- **Repository**: `FearGreedIndexRepository.java`

### 19. newsarticle - 뉴스 기사
- **Entity**: `NewsArticle.java`
- **Repository**: `NewsArticleRepository.java`

---

## 🔧 기술 스택

- **JPA/Hibernate** - ORM
- **Spring Data JPA** - Repository 추상화
- **Lombok** - 보일러플레이트 코드 제거
- **Jakarta Persistence** - JPA 3.0 표준

## 📝 패키지 네이밍 규칙

- 모두 소문자 사용
- 테이블명을 그대로 패키지명으로 사용
- 언더스코어(_) 제거하고 붙여쓰기
  - `detected_coins` → `detectedcoin`
  - `coin_ohlcvs` → `coinohlcv`
  - `fear_greed_indexes` → `feargreedindex`

## 🚀 다음 단계

각 엔티티별로 필요에 따라:
- [ ] Service 인터페이스 및 구현체 추가
- [ ] Controller 엔드포인트 추가
- [ ] Request/Response DTO 추가
- [ ] 비즈니스 로직 구현
- [ ] 예외 처리 추가
- [ ] 테스트 코드 작성
