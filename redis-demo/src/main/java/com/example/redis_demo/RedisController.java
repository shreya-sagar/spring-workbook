package com.example.redis_demo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisTemplate<String, RedisObject> redisTemplate;
    private final RedisRepository redisRepository;

    @GetMapping("/{key}")
    public ResponseEntity<RedisObject> getDataFromRedis(@PathVariable String key) {
        if(redisTemplate.hasKey(key)) {
            RedisObject redisObject = redisTemplate.opsForValue().get(key);
            return ResponseEntity.ok(redisObject);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

    }

    @GetMapping("/repo/{key}")
    public ResponseEntity<RedisObject> getDataFromRedisRepo(@PathVariable String key) {
        if(redisRepository.existsById(key)) {
            return redisRepository.findById(key)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

    }

    @PostMapping
    public ResponseEntity<RedisObject> saveDataInRedis(@RequestBody RedisObject value) {
        redisTemplate.opsForValue().set(value.getId(), value);
        URI location = UriComponentsBuilder
                .fromPath("/{id}")                             // append /{id}
                .buildAndExpand(value.getId())             // fill in the id
                .toUri();
        return ResponseEntity.created(location)
                .body(value);
    }

    @PostMapping("/repo")
    public ResponseEntity<RedisObject> saveDataInRedisRepo(@RequestBody RedisObject value) {
        RedisObject saved = redisRepository.save(value);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()                      // /repo
                .path("/{id}")                             // append /{id}
                .buildAndExpand(saved.getId())             // fill in the id
                .toUri();
        return ResponseEntity.created(location)
                .body(saved);
    }
}
