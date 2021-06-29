package integration

import com.mongodb.client.MongoCollection
import integration.fixture.BaseIntegrationSpec
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

class InMemoryMongoSpec extends BaseIntegrationSpec {

    @Autowired
    MongoTemplate mongoTemplate

    def "Should store data in mongo"() {
        given: "sample mongo collection"
        MongoCollection<Document> collection = mongoTemplate.getCollection("sampleTestCollection")

        when: "insert document to DB"
        collection.insertOne(new Document("key", "sampleValue"))

        then: "count return one"
        collection.countDocuments() == 1
    }

}
