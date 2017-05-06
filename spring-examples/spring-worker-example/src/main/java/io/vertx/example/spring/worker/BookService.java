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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.*;

/**
 * A classical transactional service facade.
 *
 * @author Thomas Segismont
 */
@Service
@Transactional
public class BookService {

  @Autowired
  BookRepository bookRepository;

  public Book save(Book book) {
    return bookRepository.save(book);
  }

  public List<Book> getAll() {
    Iterable<Book> all = bookRepository.findAll();
    return StreamSupport.stream(all.spliterator(), false).collect(toList());
  }

}
