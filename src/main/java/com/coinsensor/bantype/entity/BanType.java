package com.coinsensor.bantype.entity;

import com.coinsensor.bantype.dto.request.BanTypeRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ban_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BanType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ban_type_id")
	private Long banTypeId;

	@Column(nullable = false)
	private String reason;

	@Column(nullable = false)
	private Long period;

	public static BanType to(BanTypeRequest request) {
		return BanType.builder()
			.reason(request.getReason())
			.period(request.getPeriod())
			.build();
	}
}