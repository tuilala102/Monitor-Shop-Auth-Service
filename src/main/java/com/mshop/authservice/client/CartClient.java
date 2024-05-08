package com.mshop.authservice.client;

import com.mshop.authservice.dto.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "CartClient", url = "${cart-service.url}/api/cart")
public interface CartClient {

    @PostMapping("/user/{id}")
    Cart addCartUser(@PathVariable("id") Long id, @RequestBody Cart cart);

}
