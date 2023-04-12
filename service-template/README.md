    //If an author has >= 1 book, got error:
    //HR000057: java.util.concurrent.CompletionException: org.hibernate.LazyInitializationException:
    //HR000056: Collection cannot be initialized: com.quarkus.entity.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL


    // Got error java.util.concurrent.CompletionException:
    // org.hibernate.LazyInitializationException: HR000056:
    // Collection cannot be initialized: com.quarkus.entity.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL
    
    
    -> 
