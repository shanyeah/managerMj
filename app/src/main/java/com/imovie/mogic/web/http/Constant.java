package com.imovie.mogic.web.http;

/**
 * Created by zhouxinshan on 2016/4/12.
 * <p/>
 * http constants
 * tips from web
 *
 * @see "https://en.wikipedia.org/wiki/List_of_HTTP_header_fields"
 */
public class Constant {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";
    /**
     * Content-Types that are acceptable for the response.
     * e.g: Accept: text/plain,
     *//*
     */
    public static final String PROPERTY_ACCEPT = "Accept";

    /**
     * Character sets that are acceptable.
     * e.g: Accept-Charset: utf-8
     */
    public static final String PROPERTY_ACCEPT_CHARSET = "Accept-Charset";

    /**
     * List of acceptable encodings.
     * e.g: Accept-Encoding: gzip, deflate
     */
    public static final String PROPERTY_ACCEPT_ENCODING = "Accept-Encoding";

    /**
     * List of acceptable human languages for response.
     * e.g: Accept-Language: en-US, zh-cn
     */
    public static final String PROPERTY_ACCEPT_LANGUAGE = "Accept-Language";

    /**
     * Acceptable version in time
     * e.g: Accept-Datetime: Thu, 31 May 2007 20:35:00 GMT
     */
    public static final String PROPERTY_ACCEPT_DATETIME = "Accept-Datetime";

    /**
     * Authentication credentials for HTTP authentication
     * e.g: Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
     */
    public static final String PROPERTY_AUTHORIZATION = "Authorization";

    /**
     * Used to specify directives that must be obeyed by all caching mechanisms along the request-response chain
     * e.g: Cache-Control: no-cache
     */
    public static final String PROPERTY_CACHE_CONTROL = "Cache-Control";

    /**
     * Control options for the current connection and list of hop-by-hop request fields
     * e.g: Connection: keep-alive
     * Connection: Upgrade
     */
    public static final String PROPERTY_CONNECTION = "Connection";

    /**
     * An HTTP cookie previously sent by the server with Set-Cookie (below)
     * e.g: Cookie: $Version=1; Skin=new;
     */
    public static final String PROPERTY_COOKIE = "Cookie";

    /**
     * The length of the request body in octets (8-bit bytes)
     * e.g: Content-Length: 348
     */
    public static final String PROPERTY_CONTENT_LENGTH = "Content-Length";

    /**
     * A Base64-encoded binary MD5 sum of the content of the request body
     * e.g: Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==
     */
    public static final String PROPERTY_CONTENT_MD5 = "Content-MD5";

    /**
     * The MIME type of the body of the request (used with POST and PUT requests)
     * e.g: Content-Type: application/x-www-form-urlencoded
     */
    public static final String PROPERTY_CONTENT_TYPE = "Content-Type";

    /**
     * The date and time that the message was sent (in "HTTP-date" format as defined by RFC 7231 Date/Time Formats)
     * e.g: Date: Tue, 15 Nov 1994 08:12:31 GMT
     */
    public static final String PROPERTY_DATE = "Date";

    /**
     * Indicates that particular server behaviors are required by the client
     * e.g: Expect: 100-continue
     */
    public static final String PROPERTY_EXPECT = "Expect";

    /**
     * The email address of the user making the request
     * e.g: From: user@example.com
     */
    public static final String PROPERTY_FROM = "From";

    /**
     * The domain name of the server (for virtual hosting), and the TCP port number on which the server is listening.
     * The port number may be omitted if the port is the standard port for the service requested.
     * Mandatory since HTTP/1.1.
     * <p/>
     * e.g: Host: en.wikipedia.org:80
     * Host: en.wikipedia.org
     */
    public static final String PROPERTY_HOST = "Host";

    /**
     * Only perform the action if the client supplied entity matches the same entity on the server.
     * This is mainly for methods like PUT to only update a resource if it has not been modified since the user last updated it.
     * <p/>
     * e.g: If-Match: "737060cd8c284d8af7ad3082f209582d"
     */
    public static final String PROPERTY_IF_MATCH = "If-Match";

    /**
     * Allows a 304 Not Modified to be returned if content is unchanged
     * <p/>
     * e.g: If-Modified-Since: Sat, 29 Oct 1994 19:43:31 GMT
     */
    public static final String PROPERTY_IF_MODIFIED_SINCE = "If-Modified-Since";

    /**
     * Allows a 304 Not Modified to be returned if content is unchanged, see HTTP ETag
     * <p/>
     * e.g: If-None-Match: "737060cd8c284d8af7ad3082f209582d"
     */
    public static final String PROPERTY_IF_NONE_MATCH = "If-None-Match";

    /**
     * If the entity is unchanged, send me the part(s) that I am missing; otherwise, send me the entire new entity
     * e.g:If-Range: "737060cd8c284d8af7ad3082f209582d"
     */
    public static final String PROPERTY_IF_RANGE = "If-Range";

    /**
     * Only send the response if the entity has not been modified since a specific time.
     * <p/>
     * e.g: If-Unmodified-Since: Sat, 29 Oct 1994 19:43:31 GMT
     */
    public static final String PROPERTY_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    /**
     * Limit the number of times the message can be forwarded through proxies or gateways.
     * <p/>
     * e.g: Max-Forwards: 10
     */
    public static final String PROPERTY_MAX_FORWARDS = "Max-Forwards";

    /**
     * Initiates a request for cross-origin resource sharing (asks server for an 'Access-Control-Allow-Origin' response field) .
     * <p/>
     * e.g: Origin: http://www.example-social-network.com
     */
    public static final String PROPERTY_ORIGIN = "Origin";

    /**
     * Implementation-specific fields that may have various effects anywhere along the request-response chain.
     * <p/>
     * e.g: Pragma: no-cache
     */
    public static final String PROPERTY_PRAGMA = "Pragma";

    /**
     * Authorization credentials for connecting to a proxy.
     * <p/>
     * e.g: Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
     */
    public static final String PROPERTY_PROXY_AUTHORIZATION = "Proxy-Authorization";

    /**
     * Request only part of an entity. Bytes are numbered from 0. See Byte serving.
     * <p/>
     * e.g: Range: bytes=500-999
     */
    public static final String PROPERTY_RANGE = "Range";

    /**
     * This is the address of the previous web page from which a link to the currently requested page was followed.
     * (The word “referrer” has been misspelled in the RFC as well as in most implementations to the point that
     * it has become standard usage and is considered correct terminology)
     * <p/>
     * e.g: Referer: http://en.wikipedia.org/wiki/Main_Page
     */
    public static final String PROPERTY_REFERER = "Referer";

    /**
     * The transfer encodings the user agent is willing to accept:
     * the same values as for the response header field Transfer-Encoding can be used,
     * plus the "trailers" value (related to the "chunked" transfer method) to notify the server
     * it expects to receive additional fields in the trailer after the last, zero-sized, chunk.
     * <p/>
     * e.g: TE: trailers, deflate
     */
    public static final String PROPERTY_TE = "TE";

    /**
     * The user agent string of the user agent
     * <p/>
     * e.g: User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/21.0
     */
    public static final String PROPERTY_USER_AGENT = "User-Agent";

    /**
     * Ask the server to upgrade to another protocol.
     * <p/>
     * e.g: Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
     */
    public static final String PROPERTY_UPGRADE = "Upgrade";

    /**
     * Informs the server of proxies through which the request was sent.
     * <p/>
     * e.g: Via: 1.0 fred, 1.1 example.com (Apache/1.1)
     */
    public static final String PROPERTY_VIA = "Via";

    /**
     * A general warning about possible problems with the entity body.
     * <p/>
     * e.g: Warning: 199 Miscellaneous warning
     */
    public static final String PROPERTY_WARNING = "Warning";

    public static final class CONTENT_TYPE {

        /**
         * the default content-type
         */
        public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        /**
         * we can upload data with multi part using a boundary gap
         */
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        /**
         * mainstream used now, the request body using json
         */
        public static final String APPLICATION_JSON = "application/json;text/html;charset=UTF-8";
        /**application/json;application/json;text/html;
         * XML Remote Procedure Call, now is using json instead
         */
        public static final String TEXT_XML = "text/xml";

    }
}
