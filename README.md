# Paratroops

## Build & Run

- build:

```bash
mvn package
```

- run:

```bash
java -cp target/paratroops-1.0-SNAPSHOT.jar com.paratroops.App
```

## Structure

- entity
    - Soldier: 士兵类

- message: 士兵之间进行认证、军衔比较时使用的消息类所在的包
  - AuthMessage: 身份认证时传递的消息类

- util
  - CipherKey: 密钥封装类
  - CipherUtils: 密码学算法封装（RSA公钥私钥生成、加密、解密）
  - TroopsUtils: 部队功能封装类（密钥分发、身份认证、军衔比较、开箱）

- App: 运行GUI的入口类
