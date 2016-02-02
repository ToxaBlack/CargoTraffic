package models;

import models.enums.PackingListStatus;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dmitriy on 1.2.16.
 */

@Entity
@Table(name = "packing_list")
public class PackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    public String listNumber;

    @Constraints.Required
    public Date issueDate;

    @ManyToOne
    public User dispatcher;

    @Constraints.Required
    public PackingListStatus status;
}
