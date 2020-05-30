Doma 2 の Criteria API の紹介
============================

はじめに
-------

[Doma 2]は、Java 8以上で動作するデータベースアクセスフレームワークです。
これまではSQLファイルの実行結果をJavaオブジェクトにマッピングする機能を中心に提供していましたが、
バージョン2.35.0からJavaでSQLを組み立てるCriteria APIを提供するようになりました。

Doma 2の[Criteria API]には次のような特長があります。

- タイプセーフにSQLを組み立てられる
- RDBのリレーションシップをオブジェクトの関連にマッピングできる

このプロジェクトでは、実際にデータベースに接続するコードを示しながら上記の特長を説明します。

前提条件
-------

- JDK 8を使います
  - JDK 8より大きなバージョンを使う場合は適宜読み替えてください。
- Gradle 6.4.1を使います
  - 他のバージョンを使う場合は適宜読み替えてください。
- PostgreSQL 10.9を使います
  - 他のデータベースやバージョンを使う場合は適宜読み替えてください。

Step 0. データベーススキーマ作成とデータの投入
-----------------------------------------

[pgAdmin]などのツールを使って[schema.sql](schema.sql)のスクリプトを実行してください。

Step 1. IDEへのインポート
-----------------------

このプロジェクトをIDEにインポートする手順を示します。

まずは、`git clone`してください。

### IntelliJ IDEAの場合

できるだけ最新のIntelliJ IDEAを用意し、Gradleプロジェクトとしてインポートしてください。
新しいIntelliJ IDEAであれば追加の設定は不要です。

### Eclipseの場合

インポートをする前に以下のコマンドを実行し、Eclipseに必要な設定ファイルを生成してください。

```shell script
$ ./gradlew cleanEclipse eclipse
```

`eclipse`コマンドは[com.diffplug.eclipse.apt]プラグインによって拡張されています。
このプラグインは、ファクトリーパスなどアノテーションプロセッサーに関する設定ファイルをいい感じに生成します。
アノテーションに関する設定はEclipseのメニューからではなく、必ず上記のコマンドによって行ってください。

上記コマンド実行後、既存のプロジェクトとしてインポートをしてください。

Step 2. Javaコードの削除（オプション）
---------------------------------

このプロジェクトは、生成されたJavaコードをすでに含んでいます。
生成するStepを実際に試したい場合は、下記のコマンドを実行して生成されるコードを削除してください。

```shell script
$ ./gradlew deleteGeneratedCode
```

Step 3. Javaコードの生成
----------------------

PostgreSQL上のスキーマからJavaコードを生成します。
コードの生成には、Gradleプラグインである[doma-codegen-plugin]を使います。

[build.gradle](./build.gradle)で、次のようにdoma-codegen-pluginを有効化しています。

```groovy
apply plugin: 'org.seasar.doma.codegen'
```

このプラグインがPosgreSQLにアクセスできるようにbuildscriptブロックで
PostgreSQLのJDBCドライバーのclasspathを設定してください。

```groovy
buildscript {
    ext.postgresqlVersion = '42.2.12'
    ...
    dependencies {
        ...
        classpath "org.postgresql:postgresql:$postgresqlVersion"
    }
}
```

doma-codegen-pluginの設定はbuild.gradleの中の下記に相当します。

```groovy
domaCodeGen {
    dev {
        url = jdbcUrl
        user = jdbcUser
        password = jdbcPassword
        entity {
            packageName = entityPackage
            generationType = org.seasar.doma.gradle.codegen.desc.GenerationType.SEQUENCE
        }
    }
}
```

この記述では、JDBCや生成するエンティティクラスに関する情報を指定しています。
例えば、`packageName` はエンティティクラスのパッケージ名を表します。
また、`generationType` はエンティティのIDの生成方法を表し、
`org.seasar.doma.gradle.codegen.desc.GenerationType.SEQUENCE` はIDをシーケンスで生成することを意味しています。

次のGradleコマンドを実行すると、エンティティクラスが生成されます。

```shell script
$ ./gradlew domaCodeGenDevEntity
```

src/main/java/sample/entityの配下に下記のコードが生成されていたら成功です。

- AbstractAddress
- AbstractDepartment
- AbstractEmployee
- Address
- AddressListener
- Department
- DepartmentListener
- Employee
- EmployeeListener

Step 4. 関連オブジェクトを保持するプロパティの作成
------------------------------------------

Step 1で生成したエンティクラスにはDBのカラムに対応するプロパティが自動生成されますが、
関連オブジェクトに対応するプロパティは自動生成されません。
どのような関連を扱うかは、ユーザーに委ねられているためです。
しかし、関連オブジェクトに対応するプロパティを保持するためのクラスはエンティティクラスのスーパークラスとして自動生成されます。

ここでは、自動生成された`AbstractEmployee`と`AbstractDepartment`をそれぞれ次のように修正しましょう。

```java
public abstract class AbstractEmployee {
  private Department department;
  private Address address;

  // getter・setterは省略
}
```

```java
public abstract class AbstractDepartment {
  private final List<Employee> employeeList = new ArrayList<>();

  public List<Employee> getEmployeeList() {
    return employeeList;
  }
}
```

Step 5. プロジェクトのビルド
-------------------------------

ここまでで一度Gradleでビルドを行ってみましょう。
ビルドにあたっては、[org.seasar.doma.compile]を補助的に使います。

```
apply plugin: 'org.seasar.doma.compile'
```

[org.seasar.doma.compile]はDoma2を使ったJavaプロジェクトに必要な設定を自動化するプラグインです。

```shell script
$ ./gradlew build
```

「BUILD SUCCESSFUL」と表示されればビルド成功です。

Step 6. Domaの設定クラスの作成
-------------------------------

Domaの[設定クラス]を`DbConfig`として用意します。

```java
public class DbConfig implements Config {
  private final PostgresDialect dialect;
  private final LocalTransactionDataSource dataSource;
  private final LocalTransactionManager transactionManager;

  public DbConfig(String url, String user, String password) {
    dialect = new PostgresDialect();
    dataSource = new LocalTransactionDataSource(url, user, password);
    transactionManager =
        new LocalTransactionManager(dataSource.getLocalTransaction(getJdbcLogger()));
  }
  // getter省略
}
```

`PostgresDialect`はPostgreSQLの方言を吸収するクラスです。

`LocalTransactionDataSource`や`LocalTransactionManager`はスレッドローカルを使った
データーソースとトランザクションを扱うクラスです。
Spring Frameworkなどトランザクション管理の仕組みを提供するフレームワークと組み合わせて使う場合は、そちらを使ってください。

Step 7. アプリケーションコードの作成
-------------------------------

ここからは、実際にデータベースのデータをJavaオブジェクトとして扱うコードを作成します。

Criteira APIの中でもエントリポイントとなるのが`Entityql`クラスです。
このクラスは、前Stepで作った`DbConfig`クラスのインスタンスをコンスタトラクタで渡して生成します。

```java
entityql = new Entityql(config);
```

このオブジェクトを使ったSQLの組み立てと
データベースのデータのJavaオブジェクトへのマッピングは次のように記述します。

```java
  private List<Employee> fetchEmployeesByDepartmentName(String departmentName) {
    // (1) メタモデルのオブジェクト生成
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Address_ a = new Address_();

    return entityql
        // (2) SQLの組み立て
        .from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .innerJoin(a, on -> on.eq(e.addressId, a.addressId))
        .where(c -> c.eq(d.departmentName, departmentName))
        .orderBy(c -> c.asc(e.employeeName))
        // (3) EmployeeとDepartmentの関連づけ
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        // (4) EmployeeとAddressの関連づけ
        .associate(e, a, Employee::setAddress)
        // (5) SQLの発行と検索結果の取得
        .fetch();
  }
```

### (1) メタモデルのオブジェクト生成
まず、アクセスしたいテーブルに対応するエンティティのメタモデルのオブジェクトを生成します。

```java
Employee_ e = new Employee_();
Department_ d = new Department_();
Address_ a = new Address_();
```

例えば、`Employee_`クラスは`Employee`クラスを基にアノテーションプロセッサーで生成されたクラスです。
このようなメタモデルを使うことでタイプセーフなクエリが組み立てられます。

### (2) SQLの組み立て
SQLと同じもしくは似た用語を使ってSQLを組み立てます。
その際、テーブルやカラムに対応する部分には上述のメタモデルを使用します。

```java
.from(e)
.innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
.innerJoin(a, on -> on.eq(e.addressId, a.addressId))
.where(c -> c.eq(d.departmentName, departmentName))
.orderBy(c -> c.asc(e.employeeName))
```

例えば、下記のコードはWHERE句を組み立てています。

```java
.where(c -> c.eq(d.departmentName, departmentName))
```

`c.eq`はSQLの`=`演算子を表しており、
第1引数の`d.departmentName`はString型のプロパティなので、
第2引数がString型でない場合にコンパイルエラーとなります。
これがタイプセーフにSQLを組み立てられるという特長です。

このJavaコードは実際には次のようなSQLに変換されます。

```sql
select 
    t0_.employee_id, t0_.employee_no, t0_.employee_name, t0_.manager_id, 
    t0_.hiredate, t0_.salary, t0_.department_id, t0_.address_id, t0_.version, 
    t1_.department_id, t1_.department_no, t1_.department_name, t1_.location, 
    t1_.version, t2_.address_id, t2_.street, t2_.version 
from 
    employee t0_ 
    inner join department t1_ on (t0_.department_id = t1_.department_id) 
    inner join address t2_ on (t0_.address_id = t2_.address_id) 
where 
    t1_.department_name = ? 
order by 
    t0_.employee_name asc
```

ここでは2回のJOINを行っていますが、Criteria APIではJOINの回数に制限はありません。
また、同じテーブルを結合する自己結合についても制限はありません。

### (3) EmployeeとDepartmentの関連づけ

`associate`メソッドでエンティティオブジェクトの関連づけが行えます。
1番目と2番目の引数には関連づけ対象のエンティティに対応するメタモデルを渡し、
3番目の引数には関連付けを行うラムダ式を渡します。

```java
.associate(
    e,
    d,
    (employee, department) -> {
      employee.setDepartment(department);
      department.getEmployeeList().add(employee);
    })
```

ラムダ式のパラーメータ`employee`と`department`のそれぞれの型は
エンティティクラスの`Employee`と`Department`です。
このラムダ式ではStep 4で追加したメソッドを使ってエンティティの関連づけを行っています。

この例では`employee`と`department`を双方向に関連づけていますが、
単方向にするのか、または全く別の第3者のオブジェクトに格納するのか、
などはアプリケーションの自由です。

`employee`と`department`が`null`になることはありません。
`left outer join`を実施する場合、関連づけ対象の他方のデータが存在しないことが
あり得ますが、その場合ラムダ式は実行されません。

### (4) EmployeeとAddressの関連づけ

```java
.associate(e, a, Employee::setAddress)
```

この例では`Employee`エンティティから`Address`エンティティへの単方向の関連づけのみを行っています。
Step 4で追加したメソッドを使っています。

### (5) SQLの発行と検索結果の取得

実際にSQLを発行するのは`fetch`メソッドを実行するときです。
`fetch`メソッドはSQLを発行し、結果を`java.util.List`として返します。

1件だけを取得する場合は、`fetch`メソッドの代わりに`fetchOne`メソッドが使えます。

Step 8. アプリケーションコードの実行
-------------------------------

このサンプルでは、Gradleで実行した時に`App`クラスの`main`メソッドに
引数を渡すように作っています。
実行する際は、必ず下記のようにGradleのコマンドから実行してください。

```shell script
$ ./gradlew run
```

IDEで動かす場合は、コードを修正するか、IDEの作法に則ってください。

アプリケーションを実行すると、Step 7で説明したクエリを実行してエンティティを取得し、
エンティティのデータの一部をコンソールに出力します。
また、その後にエンティティを修正してバッチ更新を行います。

pgAdminで以下のSQLを実行すると、アプリケーションで
検索対象となるレコード3件のsalary列やversion列の値が更新されていることがわかります。

```sql
select * from employee
```

おわりに
-------

実際にデータベースに接続して動かす例を示しながら、Doma 2のCriteria APIの以下の特長を説明しました。

- タイプセーフにSQLを組み立てられる
- RDBのリレーションシップをオブジェクトの関連にマッピングできる

参考情報
-------

Criteria APIの情報が一番まとまっているのはドキュメントもしくはソースコードです。
- [Criteria API] - ドキュメント
- [org.seasar.doma.jdbc.criteriaパッケージ] - ソースコード 

Criteria APIを使ったサンプルはこのプロジェクト以外にもあります。
- [spring-boot-jpetstore] - [Spring Boot]を使ったWebアプリケーション
- [simple-examples] - [JUnit 5]を使ったサンプル集

以下の方法で質問もらえればできる範囲で回答したいと思っています。
- [teratail]に投稿
- [スタックオーバーフロー]に投稿
- Twitterに #doma2 のハッシュタグ付きで投稿
- このプロジェクトのissueに登録

感想や改善案なども歓迎です。

[Doma 2]: https://github.com/domaframework/doma
[Criteria API]: https://doma.readthedocs.io/en/latest/criteria-api/
[pgAdmin]: https://www.pgadmin.org/
[doma-codegen-plugin]: https://github.com/domaframework/doma-codegen-plugin
[com.diffplug.eclipse.apt]: https://plugins.gradle.org/plugin/com.diffplug.eclipse.apt
[org.seasar.doma.compile]: https://github.com/domaframework/doma-compile-plugin
[設定クラス]: https://doma.readthedocs.io/en/latest/config/
[spring-boot-jpetstore]: https://github.com/domaframework/spring-boot-jpetstore
[simple-examples]: https://github.com/domaframework/simple-examples
[Spring Boot]: https://github.com/spring-projects/spring-boot
[JUnit 5]: https://github.com/junit-team/junit5/
[teratail]: https://teratail.com/
[スタックオーバーフロー]: https://ja.stackoverflow.com/
[org.seasar.doma.jdbc.criteriaパッケージ]: https://github.com/domaframework/doma/tree/master/doma-core/src/main/java/org/seasar/doma/jdbc/criteria
