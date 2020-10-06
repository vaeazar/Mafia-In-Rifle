package com.example.mafia.repository;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDAO extends UserDAO {
  /*private MongoClientURI uri = null;
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
  }*/

  public Document login(String userid, String password) throws Exception {
    Document document = null;
    document = super.collection.find(Filters.eq("userid",userid)).first();
    return document;
  }
}
