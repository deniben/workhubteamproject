package com.spring.service.implementations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.component.UserContext;
import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import com.spring.entity.Event;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.exception.RestException;
import com.spring.repository.EventsRepository;
import com.spring.service.EventsService;

@Service
@Transactional
public class EventsServiceImpl implements EventsService {

	private final EventsRepository eventsRepository;
	private final ValidationService validationService;
	private final UserContext userContext;
	private final ProjectDao projectDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventsServiceImpl.class);


	@Autowired
	public EventsServiceImpl(EventsRepository eventsRepository, ValidationService validationService, UserContext userContext, ProjectDao projectDao) {
		this.eventsRepository = eventsRepository;
		this.validationService = validationService;
		this.userContext = userContext;
		this.projectDao = projectDao;
	}

	@Override
	public void createEvent(Event event) {
		LOGGER.debug("in createEvent(event: {})", event);
		if(event == null) {
			throw new RestException("Please, provide event data to create it");
		}
		validationService.validateEvent(event);
		User user = userContext.getCurrentUser();
		event.setOwnerId(user.getProfile().getCompany().getId());
		eventsRepository.save(event);
	}

	@Override
	public void deleteEvent(String eventId) {
		LOGGER.debug("in deleteEvent(eventId: {})", eventId);

		if(eventId == null) {
			throw new RestException("Event id can not be null");
		}
		Optional<Event> eventOptional = eventsRepository.findById(eventId);
		if(!eventOptional.isPresent()) {
			throw new RestException("Event with such id do not exists");
		}
		Event event = eventOptional.get();
		if(!event.getOwnerId().equals(userContext.getCurrentUser().getProfile().getCompany().getId())) {
			throw new RestException("Invalid permission");
		}
		eventsRepository.delete(event);
	}

	@Override
	public List<Event> getAll(Long projectId, Optional<Long> month) {
		LOGGER.debug("in getAll(projectId: {}, month: {})", projectId, month);

		User user = userContext.getCurrentUser();
		Company currentCompany = user.getProfile().getCompany();
		Project project = projectDao.findById(projectId).get();

		LocalDateTime now = LocalDateTime.now();

		Integer selectedMonth = month.isPresent() ? month.get().intValue() : now.getMonth().getValue();

		if(selectedMonth > 12 || selectedMonth < 1) {
			throw new RestException("Invalid month index");
		}

		if(project == null) {
			throw new RestException("Project with such id do not exists");
		}

		if(!project.getCompanyCreator().getId().equals(currentCompany.getId()) &&
				!project.getCompanyEmployee().getId().equals(currentCompany.getId())) {
			throw new RestException("Invalid permission");
		}

		LocalDateTime selectedDateLeft = now.withMonth(selectedMonth).withDayOfMonth(1);
		LocalDateTime selectedDateRight = now.withMonth(selectedMonth + 1).withDayOfMonth(1);

		return eventsRepository.findByProjectIdAndStartTimeBetween(project.getId(), selectedDateLeft, selectedDateRight);
	}

}
