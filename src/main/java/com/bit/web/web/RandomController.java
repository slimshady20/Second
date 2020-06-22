package com.bit.web.web;


import com.bit.web.service.RandNumService;
import com.bit.web.entity.RandNumMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RandomController {
    static Random rand = new Random();
    static Logger logger = LoggerFactory.getLogger(RandomController.class);

    // 서비스
    @Autowired
    private RandNumService randNumService;

    @GetMapping("/random")
    @ResponseBody
    // 엔티티, 엔티티를 리턴할 때는 무조건 고정시켜야함 -> <RandNumMessage>

    public ResponseEntity<RandNumMessage> getRandom() {
        logger.info("getRandom()");
        RandNumMessage random = randNumService.getRandom();
        return ResponseEntity.ok(random);
    }
}