package com.spring.service.implementations;

import com.spring.component.UserContext;
import com.spring.dao.SkillsDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.Project;
import com.spring.entity.Skill;
import com.spring.exception.RestException;
import com.spring.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillServiceImpl implements SkillsService {

    private final UserContext userContext;
    private final SkillsDao skillsDao;

    @Autowired
    public SkillServiceImpl(SkillsDao skillsDao, UserContext userContext) {

        this.skillsDao = skillsDao;
        this.userContext = userContext;
    }

    @Override
    public List<Skill> findAll() {
        return skillsDao.findAll();
    }


    @Override
    public Skill save(Skill skill) {
        if (skill != null) {
            Skill skill1 = skillsDao.findSkillByName(skill.getName());

            if (skill1 != null)
            {
                throw new RestException("Skill with such name is already exist", HttpStatus.BAD_REQUEST.value());
            }
            else { skillsDao.save(skill); }
        }
        else { throw new RestException("Not found", HttpStatus.BAD_REQUEST.value()); }
        return skill;

    }

    @Override
    public ResponseEntity<Object> delete(long id) {
        if (skillsDao.findById(id).isPresent()) {
            skillsDao.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Skill findById(long id) {
        return skillsDao.findById(id).get();
    }

    @Override
    public Integer calculateMatch(Project project, Set<Skill> skills) {

        return calculateMatch(project.getRequiredSkills(), skills);

    }

    private Integer calculateMatch(Set<Skill> requiredSkills, Set<Skill> proposedSkills) {
        Set<Long> proposedSkillsIds = proposedSkills.stream().map(x -> x.getId()).collect(Collectors.toSet());

        return (int) requiredSkills.stream().filter(x -> proposedSkillsIds.contains(x.getId())).count();
    }

    @Override
    public Integer calculateMatchPercentage(Project project, Set<Skill> skills) {

        Set<Skill> requiredSkills = skillsDao.findByProject(project.getId());

        if (requiredSkills.size() == 0) {
            return 100;
        }

        int matches = (int) skills.stream().filter(x -> requiredSkills.contains(x)).count();

        return (int) (((float) matches / (float) requiredSkills.size()) * 100);
    }

    @Override
    public Skill updateSkill(Skill skill) {

        skillsDao.save(skill);

        return skill;
    }

    @Override
    public ResponseEntity<Object> deleteSkill(long id) {
        if (skillsDao.findById(id).isPresent()) {
            skillsDao.deleteSkill(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public PageableResponse getAll(Integer page, Optional<String> name) {

        String currentUser = userContext.getCurrentUser().getUsername();
        return skillsDao.findAll(page, name, currentUser );
    }

}
