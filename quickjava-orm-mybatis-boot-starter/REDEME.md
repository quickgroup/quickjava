

# 依赖排查
```shell

# gateway
mvn package -D"maven.test.skip=true"  -pl quickjava-orm-mybatis-boot-starter -am
mvn dependency:tree -pl quickjava-orm-mybatis-boot-starter >> cache/quickjava-orm-mybatis-boot-starter-tree.log

```
