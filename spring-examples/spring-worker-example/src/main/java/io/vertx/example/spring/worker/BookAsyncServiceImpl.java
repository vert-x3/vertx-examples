/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.example.spring.worker;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implements the {@link BookAsyncService}, delegating calls to the transactional {@link BookService}.
 *
 * @author Thomas Segismont
 */
@Component
public class BookAsyncServiceImpl implements BookAsyncService {

  @Autowired
  BookService bookService;

  @Override
  public void add(Book book, Handler<AsyncResult<Book>> resultHandler) {
    Book saved = bookService.save(book);
    resultHandler.handle(Future.succeededFuture(saved));
  }

  @Override
  public void getAll(Handler<AsyncResult<List<Book>>> resultHandler) {
    List<Book> all = bookService.getAll();
    resultHandler.handle(Future.succeededFuture(all));
  }
}
