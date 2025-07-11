package com.example.redis_demo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Data
public class RedisObject {

    @Id
    String id;
    String name;
    String description;
}
