

# 依赖排查
```shell

# gateway
mvn package -D"maven.test.skip=true"  -pl quickjava-orm-boot-starter -am
mvn dependency:tree -pl quickjava-orm-boot-starter >> cache/quickjava-orm-boot-starter-tree.log

```
