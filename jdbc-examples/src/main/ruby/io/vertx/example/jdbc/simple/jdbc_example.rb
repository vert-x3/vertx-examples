require 'json'
require 'vertx-jdbc/jdbc_client'

client = VertxJdbc::JDBCClient.create_shared($vertx, {
  'url' => "jdbc:hsqldb:mem:test?shutdown=true",
  'driver_class' => "org.hsqldb.jdbcDriver",
  'max_pool_size' => 30
})

client.get_connection() { |conn_err,conn|
  if (conn_err != nil)
    STDERR.puts conn_err.get_message()
    return
  end

  connection = conn
  connection.execute("create table test(id int primary key, name varchar(255))") { |res_err,res|
    if (res_err != nil)
      raise res_err
    end
    # insert some test data
    connection.execute("insert into test values(1, 'Hello')") { |insert_err,insert|
      # query some data
      connection.query("select * from test") { |rs_err,rs|
        rs['results'].each do |line|
          puts JSON.generate(line)
        end

        # and close the connection
        connection.close() { |done_err,done|
          if (done_err != nil)
            raise done_err
          end
        }
      }
    }
  }
}
