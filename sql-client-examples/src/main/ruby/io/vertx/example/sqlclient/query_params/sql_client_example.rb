require 'vertx-pg-client/pg_pool'
require 'vertx-sql-client/tuple'

pool = VertxPgClient::PgPool.pool($vertx, {
  'port' => 5432,
  'host' => "the-host",
  'database' => "the-db",
  'user' => "user",
  'password' => "secret"
}, {
  'maxSize' => 4
})

# Uncomment for MySQL
#    Pool pool = MySQLPool.pool(vertx, new MySQLConnectOptions()
#      .setPort(5432)
#      .setHost("the-host")
#      .setDatabase("the-db")
#      .setUser("user")
#      .setPassword("secret"), new PoolOptions().setMaxSize(4));

pool.get_connection() { |res1_err,res1|
  if (res1_err != nil)
    STDERR.puts res1_err.get_message()
    return
  end
  connection = res1

  # create a test table
  connection.query("create table test(id int primary key, name varchar(255))") { |res2_err,res2|
    if (res2_err != nil)
      connection.close()
      STDERR.puts "Cannot create the table"
      res2_err.print_stack_trace()
      return
    end

    # insert some test data
    connection.query("insert into test values (1, 'Hello'), (2, 'World')") { |res3_err,res3|

      # query some data with arguments
      connection.prepared_query("select * from test where id = ?", VertxSqlClient::Tuple.of(2)) { |rs_err,rs|
        if (rs_err != nil)
          STDERR.puts "Cannot retrieve the data from the database"
          rs_err.print_stack_trace()
          return
        end

        rs.each do |line|
          puts "#{line}"
        end

        # and close the connection
        connection.close()
      }
    }
  }
}
