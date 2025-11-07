package com.coinsensor.channel.dto.response;

import com.coinsensor.channel.entity.Channel;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ChannelResponse {
    private Long channelId;
    private String name;
    private LocalDateTime createdAt;
    
    public static ChannelResponse from(Channel channel) {
        return ChannelResponse.builder()
                .channelId(channel.getChannelId())
                .name(channel.getName())
                .createdAt(channel.getCreatedAt())
                .build();
    }
}