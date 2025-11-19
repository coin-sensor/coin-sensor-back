package com.coinsensor.detection.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionChartResponse {
	private List<String> labels;
	private List<Dataset> datasets;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Dataset {
		private String label;
		private List<Integer> data;
	}
}