package com.coinsensor.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	FORBIDDEN("접근 권한이 없습니다."),
	INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),

	NOT_FOUND("찾을 수 없습니다."),
	TIMEFRAME_NOT_FOUND("타임프레임을 찾을 수 없습니다."),
	COIN_NOT_FOUND("코인을 찾을 수 없습니다."),
	USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
	USER_BANNED("차단된 사용자입니다."),
	CHAT_ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다."),
	MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
	VOTE_TOPIC_NOT_FOUND("투표 주제를 찾을 수 없습니다."),
	NEWS_NOT_FOUND("뉴스를 찾을 수 없습니다."),
	REPORT_NOT_FOUND("리포트를 찾을 수 없습니다."),
	ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다."),
	DETECTED_COIN_NOT_FOUND("감지된 코인을 찾을 수 없습니다."),
	EXCHANGE_COIN_NOT_FOUND("거래소 코인을 찾을 수 없습니다."),
	CLICK_COIN_NOT_FOUND("클릭 코인을 찾을 수 없습니다."),
	REACTION_NOT_FOUND("리액션을 찾을 수 없습니다."),
	TABLE_NOT_FOUND("테이블을 찾을 수 없습니다."),
	BAN_TYPE_NOT_FOUND("금지 유형을 찾을 수 없습니다."),
	CONDITION_NOT_FOUND("조건을 찾을 수 없습니다."),

	ALREADY_EXISTS("이미 존재합니다."),
	FAVORITE_COIN_ALREADY_EXISTS("이미 존재하는 즐겨찾기 코인 입니다."),
	REACTION_ALREADY_EXISTS("이미 존재하는 리액션입니다."),
	TARGET_USER_NOT_FOUND("대상 사용자를 찾을 수 없습니다.");

	private final String message;
}
