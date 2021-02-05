package com.example.mafia.dao;

import com.example.mafia.domain.Room;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private static final String NAMESPACE = "com.example.mafia.dao.";

    @Autowired
    SqlSession sqlSession;

    public void insert(Room room) {
        sqlSession.insert(NAMESPACE + "insert", room);
    }

    public int selectRoomCount(String roomId) {
        return sqlSession.selectOne(NAMESPACE+"selectRoomCount",roomId);
    }

    public int selectRoomStatus(String roomId) {
        return sqlSession.selectOne(NAMESPACE+"selectRoomStatus", roomId);
    }
}
