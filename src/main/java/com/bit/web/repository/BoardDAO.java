package com.bit.web.repository;
import com.bit.web.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
//package com.bit.web.repository;
//
//import entity.Board;
//import org.slf4j.ILoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//
//import javax.sql.DataSource;
//import javax.xml.crypto.Data;
//import java.sql.*;
//
@Component
public class BoardDAO {

    @Autowired private JdbcTemplate jdbcTemplate;
    public void create(Board board) throws Exception{
        String query = "insert into board (title, content, writer) values (?, ?, ?)";
        jdbcTemplate.update(query, board.getTitle(), board.getContent(), board.getWriter());
    }
    public Board read(Integer boardNo) throws Exception {
        List<Board> results = jdbcTemplate.query(
                "select board_no, title, content, writer, reg_date from board where board_no = ?",
                new RowMapper<Board>() {
                    public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Board board = new Board();
                        board.setBoardNo(rs.getInt("board_no"));
                        board.setTitle(rs.getString("title"));
                        board.setContent(rs.getString("content"));
                        board.setWriter(rs.getString("writer"));
                        board.setRegDate(rs.getDate("reg_date"));
                        return board;
                    }
                },
                boardNo
        );
        return results.isEmpty() ? null : results.get(0);
    }
    public void update(Board board) throws Exception {
        String query = "update board set title = ?, content = ? where board_no = ?";
        jdbcTemplate.update(query, board.getTitle(), board.getContent(), board.getBoardNo());
    }
    public void delete(Integer boardNo) throws Exception {
        String query = "delete from board where board_no = ?";
        jdbcTemplate.update(query, boardNo);
    }
    public List<Board> list() throws Exception {
        List<Board> results = jdbcTemplate.query(
                "select board_no, title, content, writer, reg_date from board " +
                        "where board_no > 0 order by board_no desc, reg_date desc",
                new RowMapper<Board>() {
                    public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Board board = new Board();
                        board.setBoardNo(rs.getInt("board_no"));
                        board.setTitle(rs.getString("title"));
                        board.setContent(rs.getString("content"));
                        board.setWriter(rs.getString("writer"));
                        board.setRegDate(rs.getDate("reg_date"));
                        return board;
                    }
                }
        );
        return results;
    }
}
//    static Logger log = LoggerFactory.getLogger(BoardDAO.class);
//
//    // private SessionFactory sessionFactory;
//    private DataSource dataSource;
//
//    public BoardDAO(DataSource dataSource){
//        this.dataSource =dataSource;
//    }
//    public Board saveBoard(Board board){
//       // 실제 DB와 연결하는 구간 JDBC 드라이버가 활성화 되어있어야 한다.
//        Connection c = DataSourceUtils.getConnection(dataSource);
//
//        try{
//            // PreparedStatement를 통해서 다중인자의 비결정적 조건들을 프로그램 변수화하여 보낼 수 있다.
//           String insertSql = "insert into board('board_no','title', " +
//                   "'content', 'writer', 'reg_date') value(null,?,?,?,?)";
//            PreparedStatement ps = c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
//
//            //아래와같이 물음표자리의 값들을 채워 넣을 수 있따.
//            ps.setString(1,board.getTtile());
//            ps.setString(2,board.getContent());
//            ps.setString(3,board.getWriter());
//            ps.setTimestamp(4, new Timestamp(board.getRegDate().getTime()));
//
//            //executeUpdate() 를 통하면 물음표자리에 필요한 값들이 모두 세팅되고
//            // 실제 DB에 Query가 날라가게 된다.
//            //잘 처리가 되면 최소 0보다 큰값을 얻는다. 에러일 경우 0보다작은 음수값을 가진다.
//            int rowsAffected = ps.executeUpdate();
//            if(rowsAffected>0){
//                // 쿼리 결과를 읽을 수 있는 객체를 만든다.
//                ResultSet result = ps.getGeneratedKeys();
//
//                if(result.next()){
//                    int id = result.getInt(1);
//                    // 여기서필요하다면  하위에 ResponseEntity를 만들어서 처리해야한다.
//                    return new Board(id, board.getTtile(), board.getContent(),
//                            board.getWriter(), new Timestamp(board.getRegDate().getTime()));
//                }
//
//            }
//        } catch(SQLException ex){
//            log.error("Failed to save board", ex);
//            try{
//                c.close();
//            } catch (SQLException e){
//                log.error("Failed to close connection",e);
//            }
//        }
//
//    }
//}
