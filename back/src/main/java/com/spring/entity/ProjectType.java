package com.spring.entity;

import javax.persistence.*;

@Table(name = "project_type")
@Entity


@NamedNativeQuery(
        name    =   "DeleteType",
        query   =   " UPDATE project SET type_id =null WHERE type_id =?; " +
                "DELETE FROM project_type\n" +
                "WHERE id = ?;"
)
@NamedQueries(
        {
                @NamedQuery(name = "ProjectTypeType_notBlocked", query = "select P from ProjectType as P where P.blocked = false "),

                @NamedQuery(name = "BlockedType", query = "update ProjectType P set P.blocked = true where P.id = : projectType"),

                @NamedQuery(name = "UnBlockedType", query = "update ProjectType P set P.blocked = false where P.id = : projectType")
        }
)



public class ProjectType {

    @Id
    @SequenceGenerator(name = "projectType_sequence", sequenceName = "projectType_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectType_sequence")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "blocked")
    private Boolean blocked;

    public Boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
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


    public ProjectType() {
    }

    public ProjectType(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
