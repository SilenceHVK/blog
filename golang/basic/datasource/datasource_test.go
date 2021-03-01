package datasource

import (
	"database/sql" // 用于定义操作数据库接口
	"testing"

	// 匿名导入包，只导入包而不使用包内的类型和数据
	_ "github.com/go-sql-driver/mysql" // mysql 驱动包，该包会自动初始化，并注册到 database/sql 上下文
)

func TestMySQL(t *testing.T) {
	/*
		连接数据库，传入参数：驱动名称，连接地址(用户名:密码@tcp(ip:port)/数据库名?charset=utf8)
		Open 返回的 sql.DB 对象是 Goroutine 并发安全的，sql.DB 对象提供了底层的数据库连接打开和关闭操作
		sql.DB 还提供管理数据库连接池，正在使用的连接被标记为繁忙，用完后回到连接池等待下次使用
		sql.DB 的设计目标就是作为长连接使用，不宜频繁开关，如果需要短连接，应该把 DB 作为参数进行传递
	*/
	db, err := sql.Open("mysql", "root:123456@tcp(node-160:3306)/practice?charset=utf8")
	if err != nil {
		panic(err)
	}

	// 执行 sql 语句，返回 Result 对象
	result, err := db.Exec("insert into user_info(name, password) values (?,?)", "张三", "123456")
	if err != nil {
		panic(err)
	}
	lastInsertId, _ := result.LastInsertId()
	t.Log("插入数据的ID：", lastInsertId)
	affected, _ := result.RowsAffected()
	t.Log("受影响函数：", affected)

	// 【推荐】预编译语句提供了手动拼接字符串 sql 语句，还能防止 sql 注入等问题
	stmt, err := db.Prepare("insert into user_info(name, password) values (?,?)")
	if err != nil {
		panic(err)
	}
	// 同 Exec 的返回类型
	stmt.Exec("王五", "123456")

	query := "select id, name, password from user_info"
	// 执行 多条查询操作，返回类型为 Rows
	rows, err := db.Query(query)

	/*
		每次 Query 后，都建议调用 rows.Close()
		因为 db.Query() 会从数据库连接池中获取一个连接，这个底层连接在结果集未关闭前会被标记为处于繁忙状态
		当遍历到最后一条记录时，会发生一个内部 EOF 错误，自动调用 rows.Close()
	*/
	defer rows.Close()
	if err != nil {
		panic(err)
	}

	// 循环遍历结果数据
	var id int
	var name, password string

	for rows.Next() {
		// scan 读取每一行数据
		err := rows.Scan(&id, &name, &password)
		if err != nil {
			panic(err)
		}
		t.Logf("ID: %d\tName: %s\tPassword:%s", id, name, password)
	}

	t.Log("")
	// 执行单条数据查询
	db.QueryRow(query+" where id = ?", 6).Scan(&id, &name, &password)
	t.Logf("ID: %d\tName: %s\tPassword:%s", id, name, password)
}
