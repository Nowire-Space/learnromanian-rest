package ro.ugal.learnromanian.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="specialization")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="specialization_id")
    protected Integer specializationId;

    @Column(name="specialization_name")
    protected String specializationName;

    public Specialization() {
    }

    public Specialization(String specializationName) {
        this.specializationName = specializationName;
    }

    public Specialization(Integer specializationId, String specializationName) {
        this.specializationId = specializationId;
        this.specializationName = specializationName;
    }

    public Integer getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Integer specializationId) {
        this.specializationId = specializationId;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }
}
