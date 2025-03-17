

# 依赖排查
```shell

# gateway
mvn package -D"maven.test.skip=true"  -pl quickjava-orm-web-starter -am
mvn dependency:tree -pl quickjava-orm-web-starter >> cache/quickjava-orm-web-starter-tree.log

```
