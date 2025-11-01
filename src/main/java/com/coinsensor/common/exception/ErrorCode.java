package com.coinsensor.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
    COIN_NOT_FOUND("코인을 찾을 수 없습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    USER_BANNED("차단된 사용자입니다."),
    CHAT_ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    VOTE_TOPIC_NOT_FOUND("투표 주제를 찾을 수 없습니다."),
    ALREADY_VOTED("이미 투표하셨습니다."),
    NEWS_NOT_FOUND("뉴스를 찾을 수 없습니다."),
    REPORT_NOT_FOUND("리포트를 찾을 수 없습니다."),
    ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다.");
    
    private final String message;
}
