package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.converters;

import java.util.function.Function;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.dto.ClientHostStatisticDTO;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.MemoryInfo;

/**
 * A converter for
 * {@link ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.MemoryInfo}
 * to its representation-level POJO
 * */
public class MemoryInfo2DTOConverter implements
    Function<MemoryInfo, ClientHostStatisticDTO.MemoryInfoDTO> {

    @Override
    public ClientHostStatisticDTO.MemoryInfoDTO apply(MemoryInfo value) {
        ClientHostStatisticDTO.MemoryInfoDTO dto =
            new ClientHostStatisticDTO.MemoryInfoDTO();
        dto.setActive(value.getActive());
        dto.setFree(value.getFree());
        dto.setInactive(value.getInactive());
        dto.setTotal(value.getTotal());
        dto.setWired(value.getWired());
        return dto;
    }
}
