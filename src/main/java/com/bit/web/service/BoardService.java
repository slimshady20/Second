package com.bit.web.service;



import com.bit.web.entity.Board;

import java.util.List;

public interface BoardService {// 인터페이스를 구현할 무언가가필요! Impl
    public void register(Board board) throws Exception;
    public Board read(Integer boardNo) throws Exception;
    public void modify(Board board) throws Exception;
    public void remove(Integer boardNo) throws Exception;
    public List<Board> list() throws Exception;
}
