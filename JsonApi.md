Edukapp provides a JSON API that can be used to create the user interface.

# Introduction #



# GET /api/featured #

Gets the list of featured widgets.

Example:

```
[
 {
 "id":1251,
 "name":"ShareDraw",
  "type":"W3C Widget",
  "featured":1,
  "uri":"http://wookie.apache.org/widgets/sharedraw",
  "tags":[],
  "activities":[]
 },
 {
  "id":1252,
  "name":"Butterfly",
  "type":"W3C Widget",
  "featured":1,
  "uri":"http://wookie.apache.org/widgets/butterfly",
  "tags":[
    {"id":1,"tagtext":"test tag text"},
    {"id":2,"tagtext":"test tag text"}
  ],
  "activities":[]
 }
]
```

# GET /api/search?q={search terms} #

Search for widgets matching the search terms.

Example:

```
GET /api/search?q=chat
```


# GET /api/widget/{widget id} #

Get the specified widget profile


# GET /api/widget/{widget uri} #

Get the specified widget profile


# GET /api/widget/{widget id}/similar #

Get widgets similar to the specified widget profile

Example:

```
GET /api/widgets/4/similar
```

Returns:

```
[
  {
  "id":1153,
  "name":"Natter",
  "type":"W3C Widget",
  "featured":0,
  "uri":"http://www.getwookie.org/widgets/natter",
  "tags":[],
  "activities":[]
  }
]
```


# GET /api/widget/{widget id}/comments #

Get comments and reviews for the specified widget profile.

Example:

```
GET /api/widget/4/comments
```

Returns:

```
[
  {
  "id":1,
  "text":"TEST",
  "rating":3,
  "time":"2012-02-14T10:20:01.000+0000",
  "user":"scott"
  }
]
```

# GET /api/tag/{tag id} #

Get the tag specified by the id (this can be the actual tag id, or just the tag name).

# GET /api/tag/{tag id}/widgets #

Get the widgets matching the tag specified by the id (this can be the actual tag id, or just the tag name)

# GET /api/tag/popular #

Returns the most popular tags used for widgets, in descending order, along with the number of times used.

Example:

```
[
    [
        {
            "id": 1,
            "tagtext": "fun"
        },
        29
    ],
    [
        {
            "id": 2,
            "tagtext": "games"
        },
        17
    ],
    [
        {
            "id": 651,
            "tagtext": "maths"
        },
        10
    ]
]
```

# "At risk" deprecated API methods #

The following methods are deprecated in favour or more RESTful patterns.


## GET /api/similar?uri={widget uri} ##

Search for similar widgets to the widget identified with the uri parameter.

_instead use /api/widget/{widget id}/similar_

## GET /api/review?uri={widget uri} ##

Get user reviews for the specified widget.

