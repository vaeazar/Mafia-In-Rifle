package com.example.mafia.dao;

import com.example.mafia.domain.Room;
import java.util.List;
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

    public void changeRoomStatus(Room room) {
        sqlSession.update(NAMESPACE + "changeRoomStatus", room);
    }

    public void decreaseRoomCount(String roomId) {
        sqlSession.update(NAMESPACE + "decreaseRoomCount", roomId);
    }

    public void increaseRoomCount(String roomId) {
        sqlSession.update(NAMESPACE + "increaseRoomCount", roomId);
    }

    public void deleteRoom(String roomId) {
        sqlSession.delete(NAMESPACE+"deleteRoom",roomId);
    }

    public Room selectRoomInfo(String roomId) {
        return sqlSession.selectOne(NAMESPACE+"selectRoomInfo",roomId);
    }

    public List<Room> selectRoomInfoList() {
        return sqlSession.selectList(NAMESPACE+"selectRoomInfoList");
    }

    public int selectRoomCount(String roomId) {
        return sqlSession.selectOne(NAMESPACE+"selectRoomCount",roomId);
    }

    public int selectRoomStatus(String roomId) {
        return sqlSession.selectOne(NAMESPACE+"selectRoomStatus", roomId);
    }
}
