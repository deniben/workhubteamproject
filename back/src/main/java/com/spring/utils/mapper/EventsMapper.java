package com.spring.utils.mapper;

import com.spring.dto.EventDto;
import com.spring.entity.Event;
import com.spring.service.CompanyService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventsMapper implements DTOMapper<Event, EventDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsMapper.class);
    private ModelMapper modelMapper;
    private final CompanyService companyService;

    @Autowired
    public EventsMapper(ModelMapper modelMapper, CompanyService companyService) {
        this.modelMapper = modelMapper;
        this.companyService = companyService;
    }

    @Override
    public EventDto toDto(Event event) {
        LOGGER.debug("in toDto method with param:{}", event);
        if (event == null) {
            return null;
        }
        TypeMap<Event, EventDto> toDto = modelMapper.getTypeMap(Event.class, EventDto.class);
        if (toDto == null) {
            toDto = modelMapper.createTypeMap(Event.class, EventDto.class);
        }
        Converter<LocalDateTime, String> dateTimeStringConverter = y -> y.getSource().toString();
        toDto.addMappings(x -> x.using(dateTimeStringConverter)
                .map(Event::getStartTime, EventDto::setStartTime))
                .addMappings(x -> x.using(dateTimeStringConverter)
                        .map(Event::getEndTime, EventDto::setEndTime))
                .addMappings(x -> x.using(y -> companyService.findCompanyById((Long) y.getSource()).getName())
                        .map(Event::getOwnerId, EventDto::setOwnerName));
        return toDto.map(event);
    }

    @Override
    public Event toEntity(EventDto eventDto) {
        LOGGER.debug("in toEntity method with param:{}", eventDto);
        if (eventDto == null) {
            return null;
        }
        Event event = modelMapper.map(eventDto, Event.class);
        if (event.getAllDay() == null) {
            event.setAllDay(false);
        }
        event.setStartTime(LocalDateTime.parse(eventDto.getStartTime()));
        event.setEndTime(LocalDateTime.parse(eventDto.getEndTime()));
        return event;
    }

}
