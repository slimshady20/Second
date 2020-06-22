package com.bit.web.repository;

import com.bit.web.entity.RandNumMessage;
import org.springframework.stereotype.Repository;

@Repository
public class RandNumRepository {

    public RandNumMessage getRandom() {
        return new RandNumMessage();
    }
}