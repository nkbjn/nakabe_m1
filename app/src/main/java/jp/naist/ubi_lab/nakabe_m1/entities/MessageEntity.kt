package jp.naist.ubi_lab.nakabe_m1.entities

import java.io.Serializable
/**
 *Created by youdai on 2019/08/05 @ nakabe_m1-1
 **/

class MessageEntity(
    var score: Double,
    var message: String,
    var user: String
): Serializable{
    constructor(): this(0.0, "", "")

    constructor(testScore: Double, testUser: String): this(testScore, "", testUser)

    constructor(data: Map<String, String>): this(
        data.getValue("score").toDouble(),
        data.getValue("message").toString(),
        data.getValue("user").toString()
    )
}
