require 'json'
require 'vertx-jdbc/jdbc_client'
def query(conn, sql, params, done)
  conn.query_with_params(sql, params) { |res_err,res|
    if (res_err != nil)
      raise res_err
    end

    done.handle(res)
  }
end
def execute(conn, sql, done)
  conn.execute(sql) { |res_err,res|
    if (res_err != nil)
      raise res_err
    end

    done.handle(nil)
  }
end

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

  # create a test table
  execute(conn, "create table test(id int primary key, name varchar(255))") { |create|
    # insert some test data
    execute(conn, "insert into test values (1, 'Hello'), (2, 'World')") { |insert|

      # query some data with arguments
      query(conn, "select * from test where id = ?", [
        2
      ]) { |rs|
        rs['results'].each do |line|
          puts JSON.generate(line)
        end

        # and close the connection
        conn.close() { |done_err,done|
          if (done_err != nil)
            raise done_err
          end
        }
      }
    }
  }
}
