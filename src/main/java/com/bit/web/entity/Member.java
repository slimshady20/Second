package com.bit.web.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data

public class Member {
    private String userName= "slimshady20";
            private String password= "1234";

            //Data타입의 들어오는 정보가 아래 annotation에 pattern 걸린 형태대로 들어와야 한다.
    // @DateTimeFormat(pattern = "yyyyMMdd")



}
