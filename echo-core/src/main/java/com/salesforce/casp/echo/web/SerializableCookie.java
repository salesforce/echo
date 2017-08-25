package com.salesforce.casp.echo.web;

import javax.servlet.http.Cookie;
import java.io.Serializable;

/**
 * A serializable cookie which wraps Cookie.
 * This gets around a NotSerializable error with Cookie when non memory stores are used.
 *
 * @version $Id: SerializableCookie.java 744 2008-08-16 20:10:49Z gregluck $
 * @author <a href="mailto:amurdoch@thoughtworks.com">Adam Murdoch</a>
 */
public class SerializableCookie implements Serializable {

    private static final long serialVersionUID = 8628587700329421486L;

    private String name;
    private String value;
    private String comment;
    private String domain;
    private int maxAge;
    private String path;
    private boolean secure;
    private int version;

    /** Creates a cookie. */
    public SerializableCookie(final Cookie cookie) {
        name = cookie.getName();
        value = cookie.getValue();
        comment = cookie.getComment();
        domain = cookie.getDomain();
        maxAge = cookie.getMaxAge();
        path = cookie.getPath();
        secure = cookie.getSecure();
        version = cookie.getVersion();
    }

    /** Builds a Cookie object from this object. */
    public Cookie toCookie() {
        final Cookie cookie = new Cookie(name, value);
        cookie.setComment(comment);
        //Otherwise null pointer exception
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(secure);
        cookie.setVersion(version);
        return cookie;
    }
}
