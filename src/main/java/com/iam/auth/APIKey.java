package com.iam.auth;

import java.time.LocalDateTime;

public class APIKey {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private String key;
    private String user;
    private boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime accessedAt;

//    When a user signs up for access to your API, generate an API key:
//
//    var token = crypto.randomBytes(32).toString('hex');
//    Store this in your database, associated with your user.
//    Carefully share this with your user, making sure to keep it as hidden as possible. You might want to show it only once before regenerating it, for instance.
//    Have your users provide their API keys as a header, like
//
//    curl -H "Authorization: apikey MY_APP_API_KEY" https://myapp.example.com
//    To authenticate a user’s API request, look up their API key in the database.

    // dont store keys in db as raw text, use bCrpyt hashing

    //    make sure the 'delete' method is disallowed for all users
    //Rate limit your API keys
//    HTTP 403 Forbidden
}
