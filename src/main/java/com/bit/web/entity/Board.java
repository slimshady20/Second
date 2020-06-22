package com.bit.web.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Board {
    private int boardNo;
    private String title;
    private String content;
    private String writer;
    private Date regDate;

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }
}
