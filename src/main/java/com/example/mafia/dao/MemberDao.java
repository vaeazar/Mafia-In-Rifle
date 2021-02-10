package com.example.mafia.dao;

import com.example.mafia.domain.Member;
import com.example.mafia.domain.Room;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private static final String NAMESPACE = "com.example.mafia.dao.";

    @Autowired
    SqlSession sqlSession;

    public void insert(Member member) {
        sqlSession.insert(NAMESPACE + "memberInsert", member);
    }

    public void changeMemberStatus(String memberId) {
        sqlSession.update(NAMESPACE + "changeMemberStatus", memberId);
    }

    public void setMemberAdmin(String memberId) {
        sqlSession.update(NAMESPACE + "setMemberAdmin", memberId);
    }

    public void deleteMember(String memberId) {
        sqlSession.delete(NAMESPACE+"deleteMember",memberId);
    }

    public Member selectMemberInfo(String memberId) {
        return sqlSession.selectOne(NAMESPACE+"selectMemberInfo", memberId);
    }

    public Member getNextAdmin(String memberRoomId) {
        return sqlSession.selectOne(NAMESPACE+"getNextAdmin", memberRoomId);
    }

    public String selectMemberName(String memberId) {
        return sqlSession.selectOne(NAMESPACE+"selectMemberName", memberId);
    }

    public String selectMemberStatus(String memberId) {
        return sqlSession.selectOne(NAMESPACE+"selectMemberStatus", memberId);
    }

    public String selectOverlapName(Member member) {
        return sqlSession.selectOne(NAMESPACE + "selectOverlapName", member);
    }

    public List<String> selectMemberNames(String memberRoomId) {
        return sqlSession.selectList(NAMESPACE + "selectMemberNames", memberRoomId);
    }

    public List<String> selectMemberIds(String memberRoomId) {
        return sqlSession.selectList(NAMESPACE + "selectMemberIds", memberRoomId);
    }

    public List<Member> selectMemberInfoList(String memberRoomId) {
        return sqlSession.selectList(NAMESPACE + "selectMemberInfoList", memberRoomId);
    }
}
