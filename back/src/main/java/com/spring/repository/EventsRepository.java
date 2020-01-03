package com.spring.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.Event;

@Repository
public interface EventsRepository extends MongoRepository<Event, String> {
	List<Event> findByProjectIdAndStartTimeBetween(Long ownerId, LocalDateTime startTimeLeft, LocalDateTime startTimeRight);
}
