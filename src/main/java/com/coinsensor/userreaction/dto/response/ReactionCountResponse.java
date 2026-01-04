package com.coinsensor.userreaction.dto.response;

public record ReactionCountResponse(
	String reactionName,
	Long count
) {
}