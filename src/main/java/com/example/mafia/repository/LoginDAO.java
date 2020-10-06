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
public class LoginDAO {
  private MongoClientURI uri = null;
  private MongoClient mongoClient = null;
  private MongoDatabase mongodb = null;
  private MongoCollection<Document> collection = null;
  private MongoCursor<Document> cursor = null;

  Logger log = LoggerFactory.getLogger(UserDAO.class);

  public LoginDAO() {
    try {
      uri = new MongoClientURI("mongodb://localhost:27017/?serverSelectionTimeoutMS=5000&connectTimeoutMS=10000&3t.uriVersion=3");
      mongoClient = new MongoClient(uri);
      mongodb = mongoClient.getDatabase("mafia");
      collection = mongodb.getCollection("UserInfo");
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  public Document login(String userid, String password) throws Exception {
    Document document = null;
    document = collection.find(Filters.eq("userid",userid)).first();
    return document;
  }
}
