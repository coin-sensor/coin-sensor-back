﻿CREATE TABLE `detected_coins` (
`detected_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '탐지된 코인 ID',
`detection_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '탐지 ID',
`exchange_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 코인 ID',
`change_x`    DECIMAL(10,2)    NOT NULL COMMENT '변동률',
`volume_x`    DECIMAL(10,2)    NOT NULL COMMENT '거래량 배율',
`high`    DECIMAL(20,8)    NOT NULL COMMENT '고가',
`low`    DECIMAL(20,8)    NOT NULL COMMENT '저가',
`view_count`    BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',
`detected_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시'
);

CREATE TABLE `user_ignores` (
`user_ignore_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 무시 ID',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`user_target_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '대상 유저 ID',
`created_at`    DATETIME NOT NULL COMMENT '무시 시각'
);

CREATE TABLE `reactions` (
`reaction_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '리액션 ID',
`name`    VARCHAR(255)    NOT NULL COMMENT '리액션 이름'
);

CREATE TABLE `coin_clicks` (
`coin_click_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '코인 클릭 ID',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`detected_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '탐지된 코인 ID',
`count`    BIGINT NOT NULL DEFAULT 0 COMMENT '클릭 횟수',
`clicked_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '클릭 일시'
);

CREATE TABLE `economic_events` (
`event_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '경제 일정 고유 ID',
`event_name`    VARCHAR(255)    NOT NULL COMMENT '이벤트 이름 (예: FOMC 회의, CPI 발표)',
`country`    VARCHAR(255)    NULL DEFAULT NULL COMMENT '국가 (예: US, KR, JP)',
`importance`    ENUM('low', 'medium', 'high')    NULL DEFAULT 'medium' COMMENT '중요도',
`event_time`    DATETIME NOT NULL COMMENT '이벤트 발생 시각',
`description`    TEXT NULL DEFAULT NULL COMMENT '이벤트 상세 설명',
`related_assets`    VARCHAR(255)    NULL DEFAULT NULL COMMENT '관련 자산 (예: USD, BTC, GOLD)',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'DB 등록 시각'
);

CREATE TABLE `vote_topics` (
`vote_topic_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '투표 주제 ID',
`title`    DATETIME NOT NULL COMMENT '주제',
`start_time`    DATETIME NOT NULL COMMENT '투표 시작 시간',
`end_time`    DATETIME NOT NULL COMMENT '투표 종료 시간',
`is_active`    TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '활성화 여부',
`created_at`    DATETIME NOT NULL COMMENT '생성시각'
);

CREATE TABLE `tables` (
`table_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '테이블 ID',
`name`    VARCHAR(255)    NOT NULL COMMENT '테이름 이름'
);

CREATE TABLE `analyses` (
`ai_analysis_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '분석 ID',
`report_date`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '리포트 날짜',
`summary_text`    TEXT NULL DEFAULT NULL COMMENT 'AI 분석 요약',
`trend_prediction`    ENUM('bullish', 'bearish', 'neutral')    NULL DEFAULT 'neutral' COMMENT '시장 예측 방향',
`is_volatility_alert`    TINYINT(1)    NULL DEFAULT 0 COMMENT '변동성 경고 (0: 정상, 1: 주의)',
`recommendation`    TEXT NULL DEFAULT NULL COMMENT '투자 추천/조언',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '리포트 생성 시각',
`like_count`    VARCHAR(255)    NULL COMMENT '좋아요 수',
`dislike_count`    VARCHAR(255)    NULL COMMENT '싫어요 수'
);

CREATE TABLE `ban_types` (
`ban_type_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '금지 유형 ID',
`reason`    VARCHAR(255)    NOT NULL COMMENT '차단 사유',
`period`    BIGINT NOT NULL COMMENT '차단 기간'
);

CREATE TABLE `conditions` (
`condition_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '조건 ID',
`timeframe_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '시간 프레임 ID',
`change_x`    DECIMAL(10,2)    NOT NULL COMMENT '변동률',
`volume_x`    DECIMAL(10,2)    NOT NULL COMMENT '거래량 배율'
);

CREATE TABLE `messages` (
`message_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '메시지 ID',
`message_parent_id`    BIGINT NULL DEFAULT auto_increment COMMENT '부모 메시지 ID',
`channnel_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '채널 ID',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`nickname`    VARCHAR(255)    NOT NULL COMMENT '작성자 닉네임',
`content`    TEXT NOT NULL COMMENT '메시지 내용',
`is_deleted`    TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '삭제 여부 (0:정상, 1:삭제됨)',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성 일시',
`updated_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정 일시'
);

CREATE TABLE `timeframes` (
`timeframe_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '시간 프레임 ID',
`name`    VARCHAR(255)    NOT NULL COMMENT '예: 1m, 5m, 1h, 4h'
);

CREATE TABLE `channnels` (
`channnel_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '채널 ID',
`name`    VARCHAR(255)    NOT NULL COMMENT '채널 이름',
`is_deleted`    TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '삭제 여부 (0:정상, 1:삭제됨)',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시'
);

CREATE TABLE `users` (
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`uuid`    VARCHAR(255)    NOT NULL COMMENT 'UUID',
`ip_address`    VARCHAR(255)    NOT NULL COMMENT '접속 IP 주소',
`is_banned`    TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '차단 여부 (0:정상, 1:차단)',
`last_active`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '마지막 활동 시각',
`nickname`    VARCHAR(255)    NOT NULL COMMENT '닉네임',
`role`    VARCHAR(255)    NOT NULL DEFAULT member COMMENT '권한',
`notification`    TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '알림 여부'
);

CREATE TABLE `ohlcvs` (
`coin_ohlcv_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT 'ohlcv ID',
`exchange_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 코인 ID',
`timeframe_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '시간 프레임 ID',
`open`    DECIMAL(20,8)    NOT NULL COMMENT '시가',
`high`    DECIMAL(20,8)    NOT NULL COMMENT '고가',
`low`    DECIMAL(20,8)    NOT NULL COMMENT '저가',
`close`    DECIMAL(20,8)    NOT NULL COMMENT '종가',
`volume`    BIGINT NOT NULL COMMENT '거래량',
`quote_volume`    DECIMAL(20,8)    NOT NULL COMMENT '거래금액',
`trades_count`    BIGINT NOT NULL COMMENT '거래횟수',
`created_at`    DATETIME NOT NULL COMMENT '생성시간'
);

CREATE TABLE `favorite_coins` (
`favorite_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '관심 코인 ID',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`exchange_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 코인 ID',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시'
);

CREATE TABLE `vote_options` (
`vote_option_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '투표 옵션 ID',
`vote_topic_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '투표 주제 ID',
`name`    VARCHAR(255)    NOT NULL COMMENT '투표 유형(롱, 숏, 중립)'
);

CREATE TABLE `user_bans` (
`user_ban_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 금지 ID',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`ban_type_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '금지 유형 ID',
`start_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '금지 시작 시간',
`end_time`    DATETIME NOT NULL COMMENT '금지 종료 시간'
);

CREATE TABLE `exchanges` (
`exchange_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 ID',
`name`    VARCHAR(255)    NOT NULL COMMENT '거래소 이름',
`type`    ENUM('spot', 'future')    NOT NULL COMMENT '거래소 타입'
);

CREATE TABLE `fear_greed_indexes` (
`fear_greed_index_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '공포 탐욕 지수 ID',
`fear_greed_value`    BIGINT NOT NULL COMMENT '공포 탐욕 지수(0~100 사이)',
`volatility_score`    DOUBLE NOT NULL COMMENT '변동성 점수',
`dominance_score`    DOUBLE NOT NULL COMMENT '도미넌스 점수',
`sentiment_score`    DOUBLE NOT NULL COMMENT '감정 점수',
`news_score`    DOUBLE NOT NULL COMMENT '뉴스 점수',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시'
);

CREATE TABLE `user_votes` (
`vote_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '투표 고유 ID',
`vote_topic_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '투표 주제 ID',
`vote_option_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '투표 옵션 ID',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`created_at`    DATETIME NOT NULL COMMENT '투표 시각'
);

CREATE TABLE `detections` (
`detection_id`    BIGINT NOT NULL COMMENT 'auto_increment',
`exchange_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 ID',
`condition_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '조건 ID',
`summary`    TEXT NOT NULL COMMENT '요약',
`detection_count`    BIGINT NOT NULL COMMENT '탐지된 코인 수',
`change_x_avg`    DECIMAL(10,2)    NOT NULL COMMENT '변동률 평균',
`volume_x_avg`    DECIMAL(10,2)    NOT NULL COMMENT '거래량 배율 평균',
`detected_at`    DATETIME NOT NULL COMMENT '탐지 시간'
);

CREATE TABLE `exchange_coins` (
`exchange_coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 코인 ID',
`exchange_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '거래소 ID',
`coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '코인 ID',
`is_active`    TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '상장 여부'
);

CREATE TABLE `news_articles` (
`news_article_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '뉴스 고유 ID',
`title`    VARCHAR(255)    NOT NULL COMMENT '뉴스 제목',
`summary`    TEXT NULL DEFAULT NULL COMMENT '요약 내용',
`content`    LONGTEXT NULL DEFAULT NULL COMMENT '전체 기사 내용',
`source_name`    VARCHAR(100)    NULL DEFAULT NULL COMMENT '뉴스 제공처 이름 (예: CoinDesk, Binance)',
`source_url`    VARCHAR(255)    NULL DEFAULT NULL COMMENT '원본 기사 URL',
`published_at`    DATETIME NOT NULL COMMENT '기사 게시 시각',
`related_ticker`    VARCHAR(255)    NULL DEFAULT NULL COMMENT '관련 코인 (예: BTC, ETH)',
`sentiment`    ENUM('positive', 'neutral', 'negative')    NULL DEFAULT 'neutral' COMMENT '기사 감정 분석 결과',
`created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'DB 저장 시각'
);

CREATE TABLE `coins` (
`coin_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '코인 ID',
`coin_ticker`    VARCHAR(255)    NOT NULL COMMENT '예: BTCUSDT, KRW-BTC',
`base_asset`    VARCHAR(255)    NOT NULL COMMENT '예: 비트코인'
);

CREATE TABLE `user_reactions` (
`user_reaction_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '사용자 리액선',
`user_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '유저 ID',
`reaction_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '리액션 ID',
`table_id`    BIGINT NOT NULL DEFAULT auto_increment COMMENT '테이블 ID',
`target_id`    BIGINT NOT NULL COMMENT '타켓 키 값'
);

ALTER TABLE `detected_coins` ADD CONSTRAINT `PK_DETECTED_COINS` PRIMARY KEY (
`detected_coin_id`
);

ALTER TABLE `user_ignores` ADD CONSTRAINT `PK_USER_IGNORES` PRIMARY KEY (
`user_ignore_id`
);

ALTER TABLE `reactions` ADD CONSTRAINT `PK_REACTIONS` PRIMARY KEY (
`reaction_id`
);

ALTER TABLE `coin_clicks` ADD CONSTRAINT `PK_COIN_CLICKS` PRIMARY KEY (
`coin_click_id`
);

ALTER TABLE `economic_events` ADD CONSTRAINT `PK_ECONOMIC_EVENTS` PRIMARY KEY (
`event_id`
);

ALTER TABLE `vote_topics` ADD CONSTRAINT `PK_VOTE_TOPICS` PRIMARY KEY (
`vote_topic_id`
);

ALTER TABLE `tables` ADD CONSTRAINT `PK_TABLES` PRIMARY KEY (
`table_id`
);

ALTER TABLE `analyses` ADD CONSTRAINT `PK_ANALYSES` PRIMARY KEY (
`ai_analysis_id`
);

ALTER TABLE `ban_types` ADD CONSTRAINT `PK_BAN_TYPES` PRIMARY KEY (
`ban_type_id`
);

ALTER TABLE `conditions` ADD CONSTRAINT `PK_CONDITIONS` PRIMARY KEY (
`condition_id`
);

ALTER TABLE `messages` ADD CONSTRAINT `PK_MESSAGES` PRIMARY KEY (
`message_id`
);

ALTER TABLE `timeframes` ADD CONSTRAINT `PK_TIMEFRAMES` PRIMARY KEY (
`timeframe_id`
);

ALTER TABLE `channnels` ADD CONSTRAINT `PK_CHANNNELS` PRIMARY KEY (
`channnel_id`
);

ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
`user_id`
);

ALTER TABLE `ohlcvs` ADD CONSTRAINT `PK_OHLCVS` PRIMARY KEY (
`coin_ohlcv_id`
);

ALTER TABLE `favorite_coins` ADD CONSTRAINT `PK_FAVORITE_COINS` PRIMARY KEY (
`favorite_coin_id`
);

ALTER TABLE `vote_options` ADD CONSTRAINT `PK_VOTE_OPTIONS` PRIMARY KEY (
`vote_option_id`
);

ALTER TABLE `user_bans` ADD CONSTRAINT `PK_USER_BANS` PRIMARY KEY (
`user_ban_id`
);

ALTER TABLE `exchanges` ADD CONSTRAINT `PK_EXCHANGES` PRIMARY KEY (
`exchange_id`
);

ALTER TABLE `fear_greed_indexes` ADD CONSTRAINT `PK_FEAR_GREED_INDEXES` PRIMARY KEY (
`fear_greed_index_id`
);

ALTER TABLE `user_votes` ADD CONSTRAINT `PK_USER_VOTES` PRIMARY KEY (
`vote_id`
);

ALTER TABLE `detections` ADD CONSTRAINT `PK_DETECTIONS` PRIMARY KEY (
`detection_id`
);

ALTER TABLE `exchange_coins` ADD CONSTRAINT `PK_EXCHANGE_COINS` PRIMARY KEY (
`exchange_coin_id`
);

ALTER TABLE `news_articles` ADD CONSTRAINT `PK_NEWS_ARTICLES` PRIMARY KEY (
`news_article_id`
);

ALTER TABLE `coins` ADD CONSTRAINT `PK_COINS` PRIMARY KEY (
`coin_id`
);

ALTER TABLE `user_reactions` ADD CONSTRAINT `PK_USER_REACTIONS` PRIMARY KEY (
`user_reaction_id`
);

ALTER TABLE `detected_coins` ADD CONSTRAINT `FK_detections_TO_detected_coins_1` FOREIGN KEY (
`detection_id`
)
REFERENCES `detections` (
`detection_id`
);

ALTER TABLE `detected_coins` ADD CONSTRAINT `FK_exchange_coins_TO_detected_coins_1` FOREIGN KEY (
`exchange_coin_id`
)
REFERENCES `exchange_coins` (
`exchange_coin_id`
);

ALTER TABLE `user_ignores` ADD CONSTRAINT `FK_users_TO_user_ignores_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `user_ignores` ADD CONSTRAINT `FK_users_TO_user_ignores_2` FOREIGN KEY (
`user_target_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `coin_clicks` ADD CONSTRAINT `FK_users_TO_coin_clicks_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `coin_clicks` ADD CONSTRAINT `FK_detected_coins_TO_coin_clicks_1` FOREIGN KEY (
`detected_coin_id`
)
REFERENCES `detected_coins` (
`detected_coin_id`
);

ALTER TABLE `conditions` ADD CONSTRAINT `FK_timeframes_TO_conditions_1` FOREIGN KEY (
`timeframe_id`
)
REFERENCES `timeframes` (
`timeframe_id`
);

ALTER TABLE `messages` ADD CONSTRAINT `FK_messages_TO_messages_1` FOREIGN KEY (
`message_parent_id`
)
REFERENCES `messages` (
`message_id`
);

ALTER TABLE `messages` ADD CONSTRAINT `FK_channnels_TO_messages_1` FOREIGN KEY (
`channnel_id`
)
REFERENCES `channnels` (
`channnel_id`
);

ALTER TABLE `messages` ADD CONSTRAINT `FK_users_TO_messages_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `ohlcvs` ADD CONSTRAINT `FK_exchange_coins_TO_ohlcvs_1` FOREIGN KEY (
`exchange_coin_id`
)
REFERENCES `exchange_coins` (
`exchange_coin_id`
);

ALTER TABLE `ohlcvs` ADD CONSTRAINT `FK_timeframes_TO_ohlcvs_1` FOREIGN KEY (
`timeframe_id`
)
REFERENCES `timeframes` (
`timeframe_id`
);

ALTER TABLE `favorite_coins` ADD CONSTRAINT `FK_users_TO_favorite_coins_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `favorite_coins` ADD CONSTRAINT `FK_exchange_coins_TO_favorite_coins_1` FOREIGN KEY (
`exchange_coin_id`
)
REFERENCES `exchange_coins` (
`exchange_coin_id`
);

ALTER TABLE `vote_options` ADD CONSTRAINT `FK_vote_topics_TO_vote_options_1` FOREIGN KEY (
`vote_topic_id`
)
REFERENCES `vote_topics` (
`vote_topic_id`
);

ALTER TABLE `user_bans` ADD CONSTRAINT `FK_users_TO_user_bans_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `user_bans` ADD CONSTRAINT `FK_ban_types_TO_user_bans_1` FOREIGN KEY (
`ban_type_id`
)
REFERENCES `ban_types` (
`ban_type_id`
);

ALTER TABLE `user_votes` ADD CONSTRAINT `FK_vote_topics_TO_user_votes_1` FOREIGN KEY (
`vote_topic_id`
)
REFERENCES `vote_topics` (
`vote_topic_id`
);

ALTER TABLE `user_votes` ADD CONSTRAINT `FK_vote_options_TO_user_votes_1` FOREIGN KEY (
`vote_option_id`
)
REFERENCES `vote_options` (
`vote_option_id`
);

ALTER TABLE `user_votes` ADD CONSTRAINT `FK_users_TO_user_votes_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `detections` ADD CONSTRAINT `FK_exchanges_TO_detections_1` FOREIGN KEY (
`exchange_id`
)
REFERENCES `exchanges` (
`exchange_id`
);

ALTER TABLE `detections` ADD CONSTRAINT `FK_conditions_TO_detections_1` FOREIGN KEY (
`condition_id`
)
REFERENCES `conditions` (
`condition_id`
);

ALTER TABLE `exchange_coins` ADD CONSTRAINT `FK_exchanges_TO_exchange_coins_1` FOREIGN KEY (
`exchange_id`
)
REFERENCES `exchanges` (
`exchange_id`
);

ALTER TABLE `exchange_coins` ADD CONSTRAINT `FK_coins_TO_exchange_coins_1` FOREIGN KEY (
`coin_id`
)
REFERENCES `coins` (
`coin_id`
);

ALTER TABLE `user_reactions` ADD CONSTRAINT `FK_users_TO_user_reactions_1` FOREIGN KEY (
`user_id`
)
REFERENCES `users` (
`user_id`
);

ALTER TABLE `user_reactions` ADD CONSTRAINT `FK_reactions_TO_user_reactions_1` FOREIGN KEY (
`reaction_id`
)
REFERENCES `reactions` (
`reaction_id`
);

ALTER TABLE `user_reactions` ADD CONSTRAINT `FK_tables_TO_user_reactions_1` FOREIGN KEY (
`table_id`
)
REFERENCES `tables` (
`table_id`
);

