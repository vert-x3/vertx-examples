require 'json'
require 'vertx-jdbc/jdbc_client'
def query(conn, sql, done)
  conn.query(sql) { |res_err,res|
    if (res_err != nil)
      raise res_err
    end

    done.handle(res)
  }
end
def start_tx(conn, done)
  conn.set_auto_commit(false) { |res_err,res|
    if (res_err != nil)
      raise res_err
    end

    done.handle(nil)
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
def end_tx(conn, done)
  conn.commit() { |res_err,res|
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
    # start a transaction
    start_tx(conn) { |beginTrans|
      # insert some test data
      execute(conn, "insert into test values(1, 'Hello')") { |insert|
        # commit data
        end_tx(conn) { |commitTrans|
          # query some data
          query(conn, "select count(*) from test") { |rs|
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
  }
}
