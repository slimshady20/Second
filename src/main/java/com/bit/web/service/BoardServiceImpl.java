package com.bit.web.service;

import com.bit.web.entity.Board;
import com.bit.web.repository.BoardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardServiceImpl implements BoardService {
    @Autowired private BoardDAO dao;
    @Override
    public void register(Board board) throws Exception {
        dao.create(board);
    }
    @Override
    public Board read(Integer boardNo) throws Exception {
        return dao.read(boardNo);
    }
    @Override
    public void modify(Board board) throws Exception {
        dao.update(board);
    }
    @Override
    public void remove(Integer boardNo) throws Exception {
        dao.delete(boardNo);
    }
    @Override
    public List<Board> list() throws Exception {
        return dao.list();
    }
}
//public class BoardServiceImpl implements BoardService{
    // Data Access Onject를 통해 Data에 접근하는 통로 역할을 해줌
//    @Autowired private BoardDAO dao;
//
//    @Override
//    public void register(Board board) throws Exception {
//        dao.saveBoard(board);
//    }
//}
