# API 엔드포인트 문서

## 📋 목차
1. [코인 관련 API](#1-코인-관련-api)
2. [채팅 API](#2-채팅-api)
3. [시장 지표 API](#3-시장-지표-api)
4. [분석 리포트 API](#4-분석-리포트-api)
5. [뉴스 API](#5-뉴스-api)
6. [투표 API](#6-투표-api)
7. [경제 일정 API](#7-경제-일정-api)
8. [거래소 API](#8-거래소-api)

---

## 1. 코인 관련 API

### 1.1 전체 코인 목록 조회
```
GET /api/coins
```
**Response**: `List<CoinResponse>`

### 1.2 코인 상세 정보
```
GET /api/coins/{coinId}
```
**Response**: `CoinResponse`

### 1.3 이상 코인 조회
```
GET /api/coins/abnormal
```
**Response**: `List<DetectedCoinResponse>`

### 1.4 고변동성 코인 TOP20
```
GET /api/coins/volatile
```
**Response**: `List<DetectedCoinResponse>`

### 1.5 코인 OHLCV 데이터
```
GET /api/coins/{coinId}/ohlcv
```
**Response**: `List<CoinOhlcvResponse>`

---

## 2. 채팅 API

### 2.1 채팅방 메시지 조회
```
GET /api/chat/channels/{channelId}/messages
```
**Response**: `List<ChatMessageResponse>`

### 2.2 메시지 전송
```
POST /api/chat/messages
```
**Request Body**:
```json
{
  "channelId": 1,
  "userId": 1,
  "nickname": "사용자닉네임",
  "message": "메시지 내용"
}
```
**Response**: `ChatMessageResponse`

---

## 3. 시장 지표 API

### 3.1 김치 프리미엄 조회
```
GET /api/market/kimchi-premium
```
**Response**: `KimchiPremiumResponse`
```json
{
  "binanceBtcUsdt": 50000.0,
  "upbitBtcKrw": 65000000.0,
  "kimchiPremium": 2.5,
  "createdAt": "2024-01-01T00:00:00"
}
```

### 3.2 공포 탐욕 지수 조회
```
GET /api/market/fear-greed
```
**Response**: `FearGreedIndexResponse`
```json
{
  "fearGreedValue": 45,
  "volatilityScore": 0.75,
  "dominanceScore": 0.60,
  "sentimentScore": 0.50,
  "newsScore": 0.55,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

## 4. 분석 리포트 API

### 4.1 일일 AI 리포트 조회
```
GET /api/analysis/daily-report
```
**Response**: `BtcAiReportResponse`
```json
{
  "reportId": 1,
  "reportDate": "2024-01-01",
  "summaryText": "오늘의 시장 분석...",
  "trendPrediction": "bullish",
  "isVolatilityAlert": false,
  "recommendation": "투자 추천 내용..."
}
```

---

## 5. 뉴스 API

### 5.1 최신 뉴스 조회
```
GET /api/news
```
**Response**: `List<NewsArticleResponse>`

### 5.2 특정 코인 관련 뉴스
```
GET /api/news/{ticker}
```
**Path Variable**: `ticker` (예: BTC, ETH)
**Response**: `List<NewsArticleResponse>`

---

## 6. 투표 API

### 6.1 활성 투표 주제 조회
```
GET /api/vote/topics
```
**Response**: `List<VoteTopicResponse>`

### 6.2 투표하기
```
POST /api/vote
```
**Request Body**:
```json
{
  "voteTopicId": 1,
  "voteOptionId": 1,
  "userId": 1
}
```
**Response**: `UserVoteResponse`

---

## 7. 경제 일정 API

### 7.1 예정된 경제 일정 조회
```
GET /api/events
```
**Response**: `List<EconomicEventResponse>`
```json
[
  {
    "eventId": 1,
    "eventName": "FOMC 회의",
    "country": "US",
    "importance": "high",
    "eventTime": "2024-01-15T14:00:00",
    "description": "연방공개시장위원회 정례회의",
    "relatedAssets": "USD, BTC, GOLD"
  }
]
```

---

## 8. 거래소 API

### 8.1 전체 거래소 목록
```
GET /api/exchanges
```
**Response**: `List<ExchangeResponse>`

---

## 📝 공통 응답 형식

### 성공 응답
```json
{
  "status": 200,
  "data": { ... }
}
```

### 에러 응답
```json
{
  "status": 400,
  "error": "에러 메시지"
}
```

---

## 🔐 인증

현재 모든 API는 `@CrossOrigin(origins = "*")`로 설정되어 있습니다.
프로덕션 환경에서는 적절한 인증 및 권한 체크가 필요합니다.

---

## 📊 Response DTO 구조

### CoinResponse
```json
{
  "coinId": 1,
  "coinTicker": "BTCUSDT",
  "baseAsset": "BTC",
  "quoteAsset": "USDT",
  "name": "Bitcoin",
  "isActive": true
}
```

### DetectedCoinResponse
```json
{
  "detectedCoinId": 1,
  "coinId": 1,
  "volatility": 5.5,
  "volume": 1000000.0,
  "createdAt": "2024-01-01T00:00:00"
}
```

### ChatMessageResponse
```json
{
  "messageId": 1,
  "channelId": 1,
  "userId": 1,
  "nickname": "사용자",
  "message": "안녕하세요",
  "createdAt": "2024-01-01T00:00:00"
}
```
