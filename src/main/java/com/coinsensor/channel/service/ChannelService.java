package com.coinsensor.channel.service;

import com.coinsensor.channel.dto.request.ChannelRequest;
import com.coinsensor.channel.dto.response.ChannelResponse;
import java.util.List;

public interface ChannelService {
    ChannelResponse createChannel(ChannelRequest request);
    List<ChannelResponse> getAllChannels();
    ChannelResponse getChannelById(Long channelId);
    ChannelResponse updateChannel(Long channelId, ChannelRequest request);
    void deleteChannel(Long channelId);
}