package com.atlantis.supermarket.core.shared;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.atlantis.supermarket.common.parser.JsonParser;

@MappedSuperclass
public abstract class BaseEntity extends AbstractAggregateRoot<BaseEntity> implements Serializable {

    @Id
    private UUID id;

    private static final long serialVersionUID = 1L;

    public UUID getId() {
	return id;
    }

    public void setId(UUID id) {
	this.id = id;
    }

    // standard getters and setters
    @Override
    public String toString() {
	return JsonParser.writeValue(this);
    }

    public BaseEntity() {
	this.id = UUID.randomUUID();
    }

    @Override
    public int hashCode() {
	return id.hashCode();
    }
}
