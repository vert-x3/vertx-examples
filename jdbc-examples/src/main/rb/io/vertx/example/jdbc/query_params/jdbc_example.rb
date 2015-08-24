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

  # create a test table
  connection.execute("create table test(id int primary key, name varchar(255))") { |create_err,create|

    # insert some test data
    connection.execute("insert into test values (1, 'Hello'), (2, 'World')") { |insert_err,insert|

      # query some data with arguments
      connection.query_with_params("select * from test where id = ?", [
        2
      ]) { |rs_err,rs|
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
