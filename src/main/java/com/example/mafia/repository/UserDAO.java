package com.example.mafia.repository;

import com.example.mafia.vo.UserVO;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
  private MongoClientURI uri = null;
  private MongoClient mongoClient = null;
  private MongoDatabase mongodb = null;
  protected MongoCollection<Document> collection = null;
  private MongoCursor<Document> cursor = null;

  Logger log = LoggerFactory.getLogger(UserDAO.class);

  public UserDAO() {
    try {
      uri = new MongoClientURI("mongodb://localhost:27017/?serverSelectionTimeoutMS=5000&connectTimeoutMS=10000&3t.uriVersion=3");
      mongoClient = new MongoClient(uri);
      mongodb = mongoClient.getDatabase("mafia");
      collection = mongodb.getCollection("UserInfo");
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  public void insert(UserVO vo) throws Exception {
    Document document = setDocument(vo);
    collection.insertOne(document);
  }

  public void update(UserVO vo) throws Exception {
    Document document = setDocument(vo);
    collection.updateOne(Filters.eq("userid",vo.getUserid()),new Document("$set",document));
  }

  public void delete(String userid) throws Exception {
    collection.deleteOne(Filters.eq("userid",userid));
  }

  public MongoCursor<Document> listAll() throws Exception {
    return collection.find().cursor();
  }

  public Document selectOne(String userid) throws Exception {
    return collection.find(Filters.eq("userid",userid)).first();
  }

  public long countByUserid(String userid) throws Exception {
    return collection.countDocuments(Filters.eq("userid",userid));
  }

  private Document setDocument(UserVO vo) {
    Document document = new Document();
    document.put("userid",vo.getUserid());
    document.put("username",vo.getUsername());
    document.put("password",vo.getPassword());
    document.put("nickname",vo.getNickname());
    document.put("citizenWin",0);
    document.put("citizenLose",0);
    document.put("mafiaWin",0);
    document.put("mafiaLose",0);
    return document;
  }

}