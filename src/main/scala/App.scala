import com.arangodb.spark.ArangoSpark
import com.arangodb.{ArangoCollection, ArangoDB, ArangoDatabase}
import org.apache.spark.sql.{DataFrame, SparkSession}
import com.arangodb.spark.WriteOptions
import com.typesafe.config.ConfigFactory

object App {
    def main(args: Array[String]) {
        val config = ConfigFactory.load()

        val spark = SparkSession
            .builder
            .master("local[*]")
            .appName("Breast cancer")
            .config("arangodb.hosts", config.getString("arangodb.hosts"))
            .config("arangodb.user", config.getString("arangodb.user"))
            .config("arangodb.password", config.getString("arangodb.password"))
            .getOrCreate()

        // Connexion à arangodb, suppression, création de la base et de la collection
        val arangoDB = new ArangoDB.Builder()
            .user(config.getString("arangodb.user"))
            .password(config.getString("arangodb.password"))
            .build

        val database: ArangoDatabase = arangoDB.db(config.getString("arangodb.database"))

        if (database.exists()) {
            database.drop()
        }

        arangoDB.createDatabase(config.getString("arangodb.database"))
        database.createCollection(config.getString("arangodb.collection"))

        // Chargement des données au format csv dans un DataFrame
        val breastCancerData: DataFrame = spark.read
            .option("sep", ",")
            .option("header", true)
            .option("inferSchema", true)
            .csv(config.getString("app.dataPath"))
            .cache()

        // Stockage des données depuis le DataFrame dans la collection
        ArangoSpark.saveDF(
            breastCancerData,
            config.getString("arangodb.collection"),
            WriteOptions(config.getString("arangodb.database"))
        )


    }
}