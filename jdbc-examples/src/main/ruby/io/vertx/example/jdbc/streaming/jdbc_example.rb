require 'json'
require 'vertx-jdbc/jdbc_client'

client = VertxJdbc::JDBCClient.create_shared($vertx, {
  'url' => "jdbc:hsqldb:mem:test?shutdown=true",
  'driver_class' => "org.hsqldb.jdbcDriver",
  'max_pool_size' => 30,
  'user' => "SA",
  'password' => ""
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
    connection.execute("insert into test values (1, 'Hello'), (2, 'Goodbye'), (3, 'Cya Later')") { |insert_err,insert|
      # query some data
      connection.query_stream("select * from test") { |stream_err,stream|
        if (stream_err == nil)
          sqlRowStream = stream

          sqlRowStream.handler() { |row|
            # do something with the row...
            puts JSON.generate(row)
          }.end_handler() { |v|
            # no more data available, close the connection
            connection.close() { |done_err,done|
              if (done_err != nil)
                raise done_err
              end
            }
          }
        end
      }
    }
  }
}
