/*
 * Copyright 2018 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package za.co.absa.cobrix.spark.cobol.examples

import org.apache.spark.sql.{SaveMode, SparkSession}

// This is an example Spark Job that uses COBOL data source.
// To run this locally change the scope of all Scala and Spark libraries from 'provided' to 'compile'

object CobolSparkExample {

  def main(args: Array[String]): Unit = {

    val sparkBuilder = SparkSession.builder().appName("Cobol source reader example 1")
    val spark = sparkBuilder
      .master("local")
      .getOrCreate()

    // This is an example read from a mainframe data file.
    // You can turn on/off the 'generate_record_id' and 'schema_retention_policy' options to see what difference it makes.
    val df = spark
      .read
      .format("cobol")
      .option("copybook", "data/test2_copybook.cob")
      //.option("generate_record_id", true)                   // Generates File_Id and Record_Id fields for line order dependent data
      //.option("schema_retention_policy", "collapse_root")   // Collapses the root group returning it's field on the top level of the schema
      .load("data/test2_data")

    df.printSchema
    println(df.count)
    df.show(100, truncate = false)

    df.toJSON.take(100).foreach(println)

    df.write.mode(SaveMode.Overwrite)
      .parquet("data/output/example")
  }

}
