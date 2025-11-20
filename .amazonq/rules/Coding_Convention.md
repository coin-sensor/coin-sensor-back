# Coding Convention

## Database 네이밍 규칙

- 테이블명은 복수형 사용
- name: 식별자, 레이블, 카테고리명 등 텍스트 형태의 명칭
- value: 수치, 측정값, 계산 결과 등 정량적 데이터

## 예외처리

- CustomException.java 를 활용
- 기존에 있는 ErrorCode 사용 or 특정 패키지에 해당하는 ErrorCode 가 없으면 다른 ErrorCode 참고해서 추가하고 사용

## Controller @RequestMapping 명명규칙

- '/api' 로 시작
- 각 엔티티에 맞게 작성하는데 Camel Case 사용, 복수형 사용

## 팩토리 메서드 네이밍 규칙

### `from`

- **용도**: 다른 타입에서 현재 타입으로 변환
- **주요 사용**: DTO ↔ Entity 변환
- **예시**:
-

```java
// Entity → DTO
DetectedCoinResponse.from(detectedCoin)
UserResponse.

from(user)

// DTO → Entity  
User.

from(userDto)
Coin.

from(coinRequest)
```

### `to`

- **용도**: 현재 타입에서 다른 타입으로 변환 또는 여러 파라미터로 객체 생성
- **주요 사용**: 여러 파라미터를 받아 Entity 생성
- **예시**:

```java
// 여러 파라미터 → Entity
DetectedCoin.to(exchangeCoin, volatility, volume, high, low)
Detection.

to(criteria, exchange, summary, count)

// Entity → DTO (인스턴스 메서드)
user.

toDto()
coin.

toResponse()
```

### `of`

- **용도**: 같은 타입 내에서 생성 (팩토리 메서드)
- **주요 사용**: 여러 파라미터로 DTO/Response 객체 생성
- **예시**:

```java
// 여러 파라미터 → DTO/Response
DetectionInfoResponse.of(detection, detectedCoins)
ErrorResponse.

of(errorCode)
PageResponse.

of(content, totalElements, pageNumber)

// Java 표준 라이브러리 스타일
LocalDate.

of(2023,12,25)
Optional.

of(value)
```

## 사용 가이드라인

1. **Entity ↔ DTO 변환**: `from` 사용
2. **여러 파라미터로 Entity 생성**: `to` 사용
3. **여러 파라미터로 DTO/Response 생성**: `of` 사용
4. **일관성 유지**: 프로젝트 전체에서 동일한 규칙 적용

## JPA 사용

- 기본적인 find, delete를 사용하는게 아닌 경우 각 패키지에 CustomRepository를 구현하고 queryDSL 을 통해 구현하여 사용

## DTO

- Post, Delete, Put 은 요청 파라미터가 필요한경우 별도의 DTO 를 구현하여 사용 