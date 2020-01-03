package com.spring.service.implementations;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.component.UserContext;
import com.spring.dao.ProjectDao;
import com.spring.dao.ProjectTypeDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.ProjectType;
import com.spring.exception.RestException;
import com.spring.service.ProjectTypeService;


@Service
@Transactional
public class ProjectTypeServiceImpl implements ProjectTypeService {


	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTypeServiceImpl.class);
	private final ProjectTypeDao projectTypeDao;
	private final ProjectDao projectDao;
	private final UserContext userContext;
	private ModelMapper modelMapper = new ModelMapper();


	@Autowired
	public ProjectTypeServiceImpl(ProjectTypeDao projectTypeDao, ProjectDao projectDao, UserContext userContext) {

		this.projectTypeDao = projectTypeDao;
		this.projectDao = projectDao;
		this.userContext = userContext;
	}


	@Override
	public List<ProjectType> findAllNotBlocked() {

		LOGGER.trace("in findAll()");
		return projectTypeDao.findAllNotBlocked();

	}

	@Override
	public PageableResponse getAll(Integer page, Optional<String> name) {

		String currentUser = userContext.getCurrentUser().getUsername();
		LOGGER.debug("getAll()");
		return projectTypeDao.findAll(page, name, currentUser);
	}

	@Override
	public List<ProjectType> findAll() {

		LOGGER.trace("in findAll()");
		return projectTypeDao.findAll();

	}

	@Override
	public ProjectType save(ProjectType projectType) {

		LOGGER.debug("in save()");

		if(projectType != null) {
			if(projectTypeDao.findProjectTypeByName(projectType.getName()).isPresent()) {
				throw new RestException("Type with such name is already exist", HttpStatus.BAD_REQUEST.value());
			} else {
				projectType.setBlocked(false);
				projectTypeDao.save(projectType);
			}
		} else {
			throw new RestException("Not found", HttpStatus.BAD_REQUEST.value());
		}
		return projectType;
	}

	@Override
	public ProjectType delete(long id) {

		LOGGER.trace("in delete()");
		Optional<ProjectType> projectType = projectTypeDao.findById(id);
		if(projectType.isPresent()) {
			projectDao.delete(id);
			return projectType.get();
		}
		throw new RestException("No entity with that id", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public ProjectType findById(long id) {

		LOGGER.debug("in findById({})", id);
		Optional<ProjectType> projectType = projectTypeDao.findById(id);
		if (projectType.isPresent()){
			return projectType.get();
		}
		throw new RestException("No entity with that id", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public ProjectType findProjectTypeByName(String name) {

		LOGGER.debug("in findProjectTypeByName({})", name);
		ProjectType projectType;
		projectType = modelMapper.map(projectTypeDao.findProjectTypeByName(name), ProjectType.class);
		return projectType;
	}

	@Override
	public ProjectType updateProjectType(ProjectType projectType) {

		LOGGER.debug("in updateProjectType({})", projectType);
		projectTypeDao.save(projectType);

		return projectType;
	}

	@Override
	public ResponseEntity<Object> deleteType(long id) {

		LOGGER.trace("in deleteType()");
		Optional<ProjectType> projectType = projectTypeDao.findById(id);
		if(projectType.isPresent() && projectDao.findByType(projectType.get()).isEmpty()) {
			projectTypeDao.delete(id);
			return ResponseEntity.ok("Deleted!");
		} else {
			projectTypeDao.blockedType(id);
			return ResponseEntity.ok("Blocked!");

		}
	}

	@Override
	public ResponseEntity<Object> unBlockedType(long id) {

		LOGGER.debug("in UnBlockedType()");

		projectTypeDao.unBlockedType(id);

		return ResponseEntity.ok("Blocked!");

	}


}




