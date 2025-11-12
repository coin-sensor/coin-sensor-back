package com.coinsensor.channel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.channel.dto.request.ChannelRequest;
import com.coinsensor.channel.dto.response.ChannelResponse;
import com.coinsensor.channel.entity.Channel;
import com.coinsensor.channel.repository.ChannelRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelServiceImpl implements ChannelService {

	private final ChannelRepository channelRepository;

	@Override
	@Transactional
	public ChannelResponse createChannel(ChannelRequest request) {
		Channel savedChannel = channelRepository.save(Channel.to(request));
		return ChannelResponse.from(savedChannel);
	}

	@Override
	public List<ChannelResponse> getAllChannels() {
		return channelRepository.findAll().stream()
			.map(ChannelResponse::from)
			.toList();
	}

	@Override
	public ChannelResponse getChannelById(Long channelId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
		return ChannelResponse.from(channel);
	}

	@Override
	@Transactional
	public ChannelResponse updateChannel(Long channelId, ChannelRequest request) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

		channel.updateName(request.getName());
		return ChannelResponse.from(channel);
	}

	@Override
	@Transactional
	public void deleteChannel(Long channelId) {
		if (!channelRepository.existsById(channelId)) {
			throw new CustomException(ErrorCode.ROOM_NOT_FOUND);
		}
		channelRepository.deleteById(channelId);
	}
}