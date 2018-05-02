package com.guerreiro.heder.springdatarestticketj.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.guerreiro.heder.springdatarestticketj.entities.User;

@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, String> {

}
