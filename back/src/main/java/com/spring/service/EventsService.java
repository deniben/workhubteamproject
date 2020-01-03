package com.spring.service;

import java.util.List;
import java.util.Optional;

import com.spring.entity.Event;

public interface EventsService {

	void createEvent(Event event);

	void deleteEvent(String eventId);

	List<Event> getAll(Long projectId, Optional<Long> month);
}
