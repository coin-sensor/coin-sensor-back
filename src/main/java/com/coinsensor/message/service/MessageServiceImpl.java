package com.coinsensor.message.service;

import static com.coinsensor.common.exception.ErrorCode.*;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.channel.entity.Channel;
import com.coinsensor.channel.repository.ChannelRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import com.coinsensor.message.entity.Message;
import com.coinsensor.message.repository.MessageRepository;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

	private final MessageRepository chatMessageRepository;
	private final ChannelRepository channelRepository;
	private final UserService userService;

	@Override
	@Transactional
	public MessageResponse saveMessage(MessageRequest request) {
		Channel channel = channelRepository.findById(request.getChannelId())
			.orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));

		User user = userService.getUserByUuid(request.getUuid());
		return MessageResponse.from(chatMessageRepository.save(Message.to(channel, user, request)));
	}

	@Override
	public List<MessageResponse> getRecentMessages(Long channelId, int limit) {
		List<Message> messages = chatMessageRepository.findRecentMessagesByChannelId(channelId, limit);
		return messages.stream()
			.map(MessageResponse::from)
			.sorted(Comparator.comparing(MessageResponse::getCreatedAt))
			.toList();
	}

	@Override
	public List<MessageResponse> getMessagesByChannelId(Long channelId) {
		List<Message> messages = chatMessageRepository.findRecentMessagesByChannelId(channelId, 50);
		return messages.stream()
			.map(MessageResponse::from)
			.sorted(Comparator.comparing(MessageResponse::getCreatedAt))
			.toList();
	}

	@Override
	public List<MessageResponse> getMessagesBefore(Long channelId, Long lastMessageId, int limit) {
		List<Message> messages = chatMessageRepository.findMessagesBefore(channelId, lastMessageId, limit);
		return messages.stream()
			.map(MessageResponse::from)
			.sorted(Comparator.comparing(MessageResponse::getCreatedAt))
			.toList();
	}

	@Override
	@Transactional
	public void deleteMessage(Long messageId) {
		Message message = chatMessageRepository.findById(messageId)
			.orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

		Message deletedMessage = Message.builder()
			.messageId(message.getMessageId())
			.channel(message.getChannel())
			.user(message.getUser())
			.nickname(message.getNickname())
			.content(message.getContent())
			.isDeleted(true)
			.createdAt(message.getCreatedAt())
			.build();

		chatMessageRepository.save(deletedMessage);
	}
}