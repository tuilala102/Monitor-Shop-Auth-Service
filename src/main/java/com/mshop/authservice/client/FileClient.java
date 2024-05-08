package com.mshop.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(value = "FileClient", url = "${file-service.url}/api/file")
public interface FileClient {

    @PostMapping("/string")
    String saveFileString(@RequestBody String base64FileString);

    @GetMapping("/string")
    String getFileString(@RequestParam("key") String key);

    @PostMapping("/strings")
    Map<String, String> getFileStrings(@RequestBody Collection<String> keys);

    @DeleteMapping("/string")
    void deleteFile(@RequestParam("key") String key);

}
