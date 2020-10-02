package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.converters.BigInteger2StringConverter;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.converters.String2BigIntegerConverter;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.converters.String2TimestampConverter;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.converters.Timestamp2StringConverter;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.ClientHostStatistic;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * This class represents input statistic information from client hosts agents.
 *
 * @see			ClientHostStatistic
 * @see			ClientHostStatisticService
 * @see			ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.converters.ClientHostStatisticFromDTOConverter
 * */
@NoArgsConstructor
@Data
public class ClientHostStatisticDTO {

	private static final Logger LOGGER = Logger.getLogger(ClientHostStatisticDTO.class);

	@JsonProperty
	private String host;

	@JsonProperty("at")
	@JsonSerialize(converter = Timestamp2StringConverter.class)
	@JsonDeserialize(converter = String2TimestampConverter.class)
	private Timestamp clientLocalTime;

	@JsonProperty("boot_time")
	@JsonSerialize(converter = Timestamp2StringConverter.class)
	@JsonDeserialize(converter = String2TimestampConverter.class)
	private Timestamp bootTime;

	@JsonProperty("public_key")
	private String publicKey;

	@JsonProperty("agent_version")
	private String agentVersion;

	@JsonProperty("cpu")
	private List<CPUInfoDTO> CPUInfoList;

	@JsonProperty("disks")
	private List<DiskInfoDTO> disksInfo;

	@JsonProperty("load")
	private List<Double> loadAverage;

	@JsonProperty("memory")
	private MemoryInfoDTO memoryInfo;

	@JsonProperty("network")
	private List<NetworkInfoDTO> networksInfo;

	@NoArgsConstructor
	@Data
	public static class CPUInfoDTO {

		private double num;

		private double user;

		private double system;

		private double idle;
	}

	@NoArgsConstructor
	@Data
	public static class DiskInfoDTO {

		private String origin;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger free;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger total;
	}

	@NoArgsConstructor
	@Data
	public static class MemoryInfoDTO {

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger wired;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger free;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger active;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger inactive;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger total;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class NetworkInfoDTO {

		private String name;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger in;

		@JsonSerialize(converter = BigInteger2StringConverter.class)
		@JsonDeserialize(converter = String2BigIntegerConverter.class)
		private BigInteger out;
	}
}