# SocialCountAPI
Just retrieves social share counts (Twitter, Facebook, Pocket) and serve them via JSONP

`GET https://socialcountapi.appspot.com/count?callback=jsonp&url=https://facebook.com/`

```js
jsonp({"twitter":31135,"pocket":503041});
```

Please note it caches the counts 10 minutes.
