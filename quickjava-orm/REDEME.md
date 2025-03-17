QuickJava-ORM

# 进度
- [x] 查询器CRUD
- [x] 查询器排序、分页、聚合、去重、行锁、查询
- [x] 查询器JOIN查询
- [x] 模型一对一关联查询、与载入
- [x] 一对一关联查询、与载入

# 模块
## ORMSqlExecutor
> 语句编译
> SQL执行

# 流程
## quickjava-web
QuerySet -> ORMSqlExecutor -> Drive/Mysql/Oracle -> Connection
## MyBatis SpringBoot
QuerySet -> MyBatisORMSqlExecutor
## MyBatis-Plus SpringBoot
QuerySet -> MyBatisPlusORMSqlExecutor（后续执行逻辑由改负责了）


# 关联查询
## 一对一
```java
public class Main {
    public static void main(String[] args) {
        new ModelLambdaQuery<UserModel>().where(UserModel::getId, 1)
                .join(UserModel::getPurses, PursesModel.class, UserModel::getId, PursesModel::getUserId)
                .join(UserModel::getPurses2, PursesModel.class, UserModel::getId, PursesModel::getUserId)
                .find();
    }
}
```
- 

