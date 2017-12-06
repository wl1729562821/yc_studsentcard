package cc.manbu.schoolinfocommunication.listener

/**
 *@Author:余慈
 *@Date:2017/10/26 0026
 *@Description: http请求返回参数类
 **/
class HttpRespnse<T> {

    var code:Int=0
    var data:T?=null

    constructor()

    constructor(code:Int,data:T){
        this.code=code
        this.data=data
    }

}