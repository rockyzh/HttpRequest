# HTTP request plugin for Phonegap / Cordova #

This plugin allows you to send native HTTP requests great for API's. This plugin can force HTTPS requests

## Adding the Plugin to your project ##

Using this plugin requires [Android PhoneGap](http://github.com/phonegap/phonegap-android).
Based off of HTTP-Request library [http-request](https://github.com/kevinsawicki/http-request)

1. Include JS in your assets folder after cordova

    &lt;script type="text/javascript" charset="utf-8" src="httpRequest.js"&gt;&lt;/script&gt;

2. Add HttpRequestPlugin and HttpRequest java classes to your project

3. Add the plugin to your cordova config.xml

```xml
    <plugins>
        <plugin
            name="HttpRequest"
            value="com.phonegap.plugins.http.HttpRequestPlugin" />
     </plugins>
```

## Using the plugin ##

Plugin supports GET and POST methods
<br/>
There are two main options to set, trustAll which makes the request trust all SSL certs and Gzip to accept gzip requests
<br/>
If the response request code == 200 the win callback function is triggered otherwise it will fail

##Example##
Query facebook for posts with 'phonegap'

<pre>
    var httpOptions = {
            trustAll: true
        };
        var params = {
            q: 'phonegap',
            type: 'post'// note this is a GET request post refers to the facebook wall posts
        };

        var apiUrl = 'https://graph.facebook.com/search?';

        window.plugins.HttpRequest.execute(apiUrl,'get',params, httpOptions,
                function(response) {

                    var code = response.code;
                    var message = response.message;
                    var body = response.body;
                    
                    alert(JSON.stringify(body));
                    
                    return;
                },
                function(response) {
              
                    var code = response.code;
                    var message = response.message;
                    var body = response.body;

                    alert('Request : ' + message + ' code ' + code);
                });

        
</pre>

## RELEASE NOTES ##

### Feb 12, 2013 ###

* Updated the core library

### Jan 4, 2013 ###

* Initial release

## BUGS AND CONTRIBUTIONS ##


### The MIT License

Copyright (c) <2013> Brian Perin

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 
