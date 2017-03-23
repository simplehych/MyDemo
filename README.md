Note：

    1、使用Retrofit，retrofit.create(service)方法内部使用反射invoke()的方法，效率问题？是将Service接口中全部的方法执行映射进行遍历，慢，体验差。

    2、路由模式进行模块化，所有模块暴露的方法在主工程内进行统一管理配置，例如写在文件下或一个类中

    3、将Retrofit的Api提取到BaseLibrary里，URL在主工程的某处统一配置，进一步封装，之前的网络地址在Build.config下，封装对用户使用力求简洁明了

    4、Mvp模式的理解，进行解耦

网上查看，retrofit的使用
接口，程序的扩展性、解耦、复用
泛型查看
