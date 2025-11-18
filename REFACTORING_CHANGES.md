# 코인 센서 백엔드 리팩토링 변경사항

## 1. 데이터베이스 스키마 변경

### 테이블명 변경
- `detection_criteria` → `criteria` → `conditions` (최종)
- `coin_ohlcvs` → `ohlcvs` (최종)
- `timeframes` 테이블의 `timeframe_label` → `name`

### 필드명 변경
- `detection_criteria_id` → `criterion_id` → `condition_id` (최종)
- `volume` → `volume_x`
- `volatility` → `change_x` (최종)
- `volatility_avg` → `change_x_avg` (최종)
- `timeframe_label` → `name`
- Detection 테이블의 외래키: `criterion_id` → `condition_id`

### 새로 추가된 필드
- `detections` 테이블:
  - `volatility_avg` DECIMAL(10,2) NOT NULL
  - `volume_x_avg` DECIMAL(10,2) NOT NULL
- `detected_coins` 테이블:
  - `high` DECIMAL(20,8) NOT NULL
  - `low` DECIMAL(20,8) NOT NULL

## 2. 패키지 구조 변경

### 패키지명 변경
```
com.coinsensor.detectioncriteria → com.coinsensor.criteria → com.coinsensor.conditions (최종)
com.coinsensor.coinohlcv → com.coinsensor.ohlcvs (최종)
```

### 엔티티명 변경
```
DetectionCriteria → Criteria → Condition (최종)
CoinOhlcv → Ohlcv (최종)
```

### 리포지토리명 변경
```
DetectionCriteriaRepository → CriteriaRepository → ConditionRepository (최종)
CoinOhlcvRepository → OhlcvRepository (최종)
```

## 3. API 변경사항

### 3.1 Detection API

#### GET /api/detections
**요청 파라미터:**
- `exchange`: String (거래소명, 예: "binance")
- `exchangeType`: String (거래소 타입, 예: "spot", "future")
- `coinCategory`: String (코인 카테고리, 예: "all", "top20", "bottom20")
- `timeframe`: String (시간프레임, 예: "1m", "5m", "15m", "1h", "4h", "1d")

**응답 DTO 변경사항:**
```json
{
  "exchangeName": "binance",
  "exchangeType": "spot",
  "timeframeName": "1m",           // 변경: timeframeLabel → timeframeName
  "criteriaChangeX": 1.00,         // 변경: criteriaVolatility → criteriaChangeX
  "criteriaVolumeX": 2.00,         // 변경: criteriaVolume → criteriaVolumeX (타입: BigDecimal)
  "detectedAt": "2024-01-01T10:00:00",
  "coins": [
    {
      "detectedCoinId": 1,
      "coinTicker": "BTCUSDT",
      "changeX": 5.25,             // 변경: volatility → changeX
      "volumeX": 3.50,             // 변경: volume → volumeX (타입: BigDecimal)
      "viewCount": 10,
      "detectedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 3.2 OHLCV API

#### GET /api/ohlcvs/exchange-coin/{exchangeCoinId}
**요청 파라미터:**
- `exchangeCoinId`: Long (거래소 코인 ID)

**응답 DTO 변경사항:**
```json
[
  {
    "ohlcvId": 1,                 // 변경: coinOhlcvId → ohlcvId
    "exchangeCoinId": 123,        // 변경: coinId → exchangeCoinId
    "open": "50000.12345678",      // 변경: Double → BigDecimal(20,8)
    "high": "51000.87654321",      // 변경: Double → BigDecimal(20,8)
    "low": "49500.11111111",       // 변경: Double → BigDecimal(20,8)
    "close": "50500.22222222",     // 변경: Double → BigDecimal(20,8)
    "volume": 1000000,             // 변경: Double → Long
    "quoteVolume": "50500000000.12345678",  // 새로 추가: BigDecimal(20,8)
    "tradesCount": 5000,           // 새로 추가: Long
    "createdAt": "2024-01-01T10:00:00"
  }
]
```

### 3.3 WebSocket 메시지 변경사항

#### 토픽 구독 경로 (변경 없음)
```
/topic/detections?exchange={exchange}&exchangeType={exchangeType}&coinCategory={coinCategory}&timeframe={timeframe}
```

#### WebSocket 메시지 응답 구조 변경
```json
{
  "exchangeName": "binance",
  "exchangeType": "spot", 
  "timeframeName": "1m",           // 변경: timeframeLabel → timeframeName
  "criteriaChangeX": 1.00,         // 변경: criteriaVolatility → criteriaChangeX
  "criteriaVolumeX": 2.00,         // 변경: criteriaVolume → criteriaVolumeX
  "detectedAt": "2024-01-01T10:00:00",
  "coins": [
    {
      "detectedCoinId": 1,
      "coinTicker": "BTCUSDT", 
      "changeX": 5.25,             // 변경: volatility → changeX
      "volumeX": 3.50,             // 변경: volume → volumeX
      "viewCount": 0,
      "detectedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

## 4. 엔티티 필드 변경사항

### 4.1 Condition 엔티티 (최종)
```java
// 이전
private Long detectionCriteriaId;
private BigDecimal volatility;
private Double volume;

// 변경 후  
private Long conditionId;
private BigDecimal changeX;      // volatility → changeX
private BigDecimal volumeX;
```

### 4.2 Timeframe 엔티티
```java
// 이전
private String timeframeLabel;

// 변경 후
private String name;
```

### 4.3 Detection 엔티티
```java
// 이전
private DetectionCriteria detectionCriteria;
private BigDecimal volatilityAvg;

// 변경 후
private Condition condition;         // 최종
private BigDecimal changeXAvg;       // volatilityAvg → changeXAvg
private BigDecimal volumeXAvg;       // 새로 추가
```

### 4.4 DetectedCoin 엔티티
```java
// 이전
private BigDecimal volatility;
private Double volume;
private Double high;
private Double low;

// 변경 후
private BigDecimal changeX;     // volatility → changeX
private BigDecimal volumeX;
private BigDecimal high;        // 정밀도 변경: DECIMAL(20,8)
private BigDecimal low;         // 정밀도 변경: DECIMAL(20,8)
```

### 4.5 Ohlcv 엔티티 (최종)
```java
// 이전
private Coin coin;
private Double open;
private Double high;
private Double low;
private Double close;
private Double volume;
private Double quoteVolume;
private Integer tradesCount;

// 변경 후
private ExchangeCoin exchangeCoin;  // Coin → ExchangeCoin
private BigDecimal open;            // Double → BigDecimal(20,8)
private BigDecimal high;            // Double → BigDecimal(20,8)
private BigDecimal low;             // Double → BigDecimal(20,8)
private BigDecimal close;           // Double → BigDecimal(20,8)
private Long volume;                // Double → Long
private BigDecimal quoteVolume;     // Double → BigDecimal(20,8)
private Long tradesCount;           // Integer → Long
```

## 5. 메서드명 변경사항

### 5.1 Repository 메서드
```java
// TimeframeRepository
// 이전: findByTimeframeLabel(String timeframeLabel)
// 변경 후: findByName(String name)
```

### 5.2 Service 메서드
```java
// CoinDetectionService
// 이전: detectByTimeframe(String timeframeLabel)
// 변경 후: detectByTimeframe(String timeframeName)
```

## 6. 프론트엔드 수정 필요사항

### 6.1 API 응답 처리 변경
```javascript
// 이전
response.timeframeLabel
response.criteriaVolatility
response.criteriaVolume
response.coins[0].volatility
response.coins[0].volume

// 변경 후
response.timeframeName
response.criteriaChangeX     // criteriaVolatility → criteriaChangeX
response.criteriaVolumeX     // criteriaVolume → criteriaVolumeX
response.coins[0].changeX    // volatility → changeX
response.coins[0].volumeX    // volume → volumeX
```

### 6.2 WebSocket 메시지 처리 변경
```javascript
// 이전
message.timeframeLabel
message.criteriaVolatility
message.criteriaVolume
message.coins[0].volatility
message.coins[0].volume

// 변경 후  
message.timeframeName
message.criteriaChangeX      // criteriaVolatility → criteriaChangeX
message.criteriaVolumeX      // criteriaVolume → criteriaVolumeX
message.coins[0].changeX     // volatility → changeX
message.coins[0].volumeX     // volume → volumeX
```

### 6.3 데이터 타입 변경 주의사항
- `criteriaChangeX`, `criteriaVolumeX`, `changeX`, `volumeX`는 이제 BigDecimal 타입으로 소수점 2자리까지 정확한 값
- `high`, `low` 필드는 소수점 8자리까지 정확한 값 (암호화폐 가격 정밀도)

## 7. 초기 데이터 변경사항

### 7.1 기본 조건 데이터 (최종)
```java
// 모든 volumeX 값이 BigDecimal로 변경
new Condition(tf1m, BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.0));
new Condition(tf5m, BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.0)); 
new Condition(tf15m, BigDecimal.valueOf(2.00), BigDecimal.valueOf(2.0));
new Condition(tf1h, BigDecimal.valueOf(3.00), BigDecimal.valueOf(2.0));
new Condition(tf4h, BigDecimal.valueOf(5.00), BigDecimal.valueOf(2.0));
```

## 9. 최종 변경사항 요약

### 9.1 핵심 변경사항
1. **패키지명**: `detectioncriteria` → `conditions`
2. **엔티티명**: `DetectionCriteria` → `Condition`
3. **테이블명**: `detection_criteria` → `conditions`
4. **기본키**: `detection_criteria_id` → `condition_id`
5. **외래키**: Detection 테이블의 `criterion_id` → `condition_id`
6. **필드명**: Detection 엔티티의 `criteria` → `condition`
7. **변동률 필드**: `volatility` → `change_x`, `volatilityAvg` → `change_x_avg`

### 9.2 메서드명 변경
```java
// Service 메서드
detectAbnormalCoins(Criteria criteria) → detectAbnormalCoins(Condition condition)
detectBinanceSpotCoins(Criteria criteria) → detectBinanceSpotCoins(Condition condition)
detectSingleCoin(ExchangeCoin exchangeCoin, Criteria criteria, String baseUrl) → detectSingleCoin(ExchangeCoin exchangeCoin, Condition condition, String baseUrl)

// Repository 주입
CriteriaRepository criteriaRepository → ConditionRepository conditionRepository

// Getter 메서드
getVolatility() → getChangeX()
getVolatilityAvg() → getChangeXAvg()
```

### 9.3 JPQL 쿼리 변경
```sql
-- 이전
JOIN FETCH dg.criteria c

-- 변경 후
JOIN FETCH dg.condition c
```

## 8. 주요 변경 파일 목록

### 새로 생성된 파일
- `com.coinsensor.conditions.entity.Condition` (최종)
- `com.coinsensor.conditions.repository.ConditionRepository` (최종)

### 수정된 파일
- `com.coinsensor.timeframe.entity.Timeframe`
- `com.coinsensor.detection.entity.Detection`
- `com.coinsensor.detectedcoin.entity.DetectedCoin`
- `com.coinsensor.detection.dto.response.DetectionInfoResponse`
- `com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse`
- `com.coinsensor.detection.service.CoinDetectionService`
- `com.coinsensor.scheduler.CoinDetectionScheduler`
- `com.coinsensor.common.config.DataInitializer`

### 삭제된 파일
- `com.coinsensor.detectioncriteria` 패키지 전체
- `com.coinsensor.criteria` 패키지 전체 (중간 단계)
- `com.coinsensor.coinohlcv` 패키지 전체

### 최종 생성된 파일
- `com.coinsensor.conditions.entity.Condition`
- `com.coinsensor.conditions.repository.ConditionRepository`
- `com.coinsensor.ohlcvs.entity.Ohlcv`
- `com.coinsensor.ohlcvs.repository.OhlcvRepository`
- `com.coinsensor.ohlcvs.service.OhlcvService`
- `com.coinsensor.ohlcvs.controller.OhlcvController`
- `com.coinsensor.ohlcvs.dto.response.OhlcvResponse`