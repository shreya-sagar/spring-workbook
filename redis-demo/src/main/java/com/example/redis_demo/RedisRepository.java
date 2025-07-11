package com.example.redis_demo;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisObject, String> {
}
