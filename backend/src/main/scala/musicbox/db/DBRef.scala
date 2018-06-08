package musicbox.db

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

case class DBRef(collection: String, id: BSONObjectID, db: Option[String] = None)

object DBRef {
  implicit object DBRefReader extends BSONDocumentReader[DBRef] {

    def read(bson: BSONDocument) =
      DBRef(
        bson.getAs[String]("$ref").get,
        bson.getAs[BSONObjectID]("$id").get,
        bson.getAs[String]("$db")
      )
  }

  implicit object DBRefWriter extends BSONDocumentWriter[DBRef] {

    def write(ref: DBRef) =
      BSONDocument("$ref" -> ref.collection, "$id" -> ref.id, "$db" -> ref.db)
  }
}
