/*
 * This file is generated by jOOQ.
 */
package org.simpleflatmapper.jooq.test.books;


import org.jooq.ForeignKey;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import javax.annotation.Generated;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>PUBLIC</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AuthorRecord> PK_T_AUTHOR = UniqueKeys0.PK_T_AUTHOR;
    public static final UniqueKey<BookRecord> PK_T_BOOK = UniqueKeys0.PK_T_BOOK;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<BookRecord, AuthorRecord> FK_T_BOOK_AUTHOR_ID = ForeignKeys0.FK_T_BOOK_AUTHOR_ID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<AuthorRecord> PK_T_AUTHOR = Internal.createUniqueKey(Author.AUTHOR, "PK_T_AUTHOR", Author.AUTHOR.ID);
        public static final UniqueKey<BookRecord> PK_T_BOOK = Internal.createUniqueKey(Book.BOOK, "PK_T_BOOK", Book.BOOK.ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<BookRecord, AuthorRecord> FK_T_BOOK_AUTHOR_ID = Internal.createForeignKey(Keys.PK_T_AUTHOR, Book.BOOK, "FK_T_BOOK_AUTHOR_ID", Book.BOOK.AUTHOR_ID);
    }
}
