package com.spring.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")


@NamedNativeQuery(
        name = "DeleteSkill",
        query = "DELETE FROM required_skills\n" +
                "WHERE skill_id = ?;\n" +
                "DELETE FROM proposed_skills\n" +
                "WHERE skill_id = ?;\n" +
                "DELETE FROM skills\n" +
                "WHERE id= ?;"
)


public class Skill {

    @Id
    @SequenceGenerator(name = "skills_sequence", sequenceName = "skills_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skills_sequence")
    private Long id;

    @Column(unique = true)
    private String name;


    public Skill() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Skill) {
            Skill skill = (Skill) obj;
            return skill.getId().equals(id);
        }

        return super.equals(obj);
    }

}