package com.example.mafia.dao;

import com.example.mafia.domain.Room;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private static final String NAMESPACE = "com.example.mafia.dao.";

    @Autowired
    @Qualifier("mafiaSqlSessionTemplate")
    SqlSession sqlSession;

    public void insert(Room room) {
        sqlSession.insert(NAMESPACE + "insert", room);
    }

    public void roomVoteStart(String roomId) {
        sqlSession.update(NAMESPACE + "roomVoteStart", roomId);
    }

    public void roomVoteEnd(String roomId) {
        sqlSession.update(NAMESPACE + "roomVoteEnd", roomId);
    }

    public void roomMafiaVoteStart(String roomId) {
        sqlSession.update(NAMESPACE + "roomMafiaVoteStart", roomId);
    }

    public void roomMafiaVoteEnd(String roomId) {
        sqlSession.update(NAMESPACE + "roomMafiaVoteEnd", roomId);
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

    public void deleteAllRoom() {
        sqlSession.delete(NAMESPACE+"deleteAllRoom");
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
