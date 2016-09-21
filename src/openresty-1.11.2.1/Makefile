.PHONY: all install clean

all:
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/LuaJIT-2.1-20160517 && $(MAKE) TARGET_STRIP=@: CCDEBUG=-g CC=cc PREFIX=/etc/echo/luajit
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-cjson-2.1.0.4 && $(MAKE) DESTDIR=$(DESTDIR) LUA_INCLUDE_DIR=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/luajit-root/etc/echo/luajit/include/luajit-2.1 LUA_CMODULE_DIR=/etc/echo/lualib LUA_MODULE_DIR=/etc/echo/lualib CJSON_CFLAGS="-g -fpic" CJSON_LDFLAGS='-bundle -undefined dynamic_lookup' CC=cc
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-redis-parser-0.12 && $(MAKE) DESTDIR=$(DESTDIR) LUA_INCLUDE_DIR=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/luajit-root/etc/echo/luajit/include/luajit-2.1 LUA_LIB_DIR=/etc/echo/lualib LDFLAGS='-bundle -undefined dynamic_lookup' CC=cc
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-rds-parser-0.06 && $(MAKE) DESTDIR=$(DESTDIR) LUA_INCLUDE_DIR=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/luajit-root/etc/echo/luajit/include/luajit-2.1 LUA_LIB_DIR=/etc/echo/lualib LDFLAGS='-bundle -undefined dynamic_lookup' CC=cc
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/nginx-1.11.2 && $(MAKE)

install: all
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/LuaJIT-2.1-20160517 && $(MAKE) install TARGET_STRIP=@: CCDEBUG=-g CC=cc PREFIX=/etc/echo/luajit DESTDIR=$(DESTDIR)
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-cjson-2.1.0.4 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_INCLUDE_DIR=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/luajit-root/etc/echo/luajit/include/luajit-2.1 LUA_CMODULE_DIR=/etc/echo/lualib LUA_MODULE_DIR=/etc/echo/lualib CJSON_CFLAGS="-g -fpic" CJSON_LDFLAGS='-bundle -undefined dynamic_lookup' CC=cc
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-redis-parser-0.12 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_INCLUDE_DIR=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/luajit-root/etc/echo/luajit/include/luajit-2.1 LUA_LIB_DIR=/etc/echo/lualib LDFLAGS='-bundle -undefined dynamic_lookup' CC=cc
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-rds-parser-0.06 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_INCLUDE_DIR=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/luajit-root/etc/echo/luajit/include/luajit-2.1 LUA_LIB_DIR=/etc/echo/lualib LDFLAGS='-bundle -undefined dynamic_lookup' CC=cc
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-dns-0.17 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-memcached-0.14 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-redis-0.25 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-mysql-0.16 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-string-0.09 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-upload-0.09 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-websocket-0.05 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-lock-0.04 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-lrucache-0.04 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-core-0.1.8 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/lua-resty-upstream-healthcheck-0.04 && $(MAKE) install DESTDIR=$(DESTDIR) LUA_LIB_DIR=/etc/echo/lualib INSTALL=/Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/resty-cli-0.13 && /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/install bin/* $(DESTDIR)/etc/echo/bin/
	cp /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/resty.index $(DESTDIR)/etc/echo/
	cp -r /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/pod $(DESTDIR)/etc/echo/
	cd /Users/pteyer/coding/echo/src/openresty-1.11.2.1/build/nginx-1.11.2 && $(MAKE) install DESTDIR=$(DESTDIR)
	mkdir -p $(DESTDIR)/etc/echo/site/lualib
	ln -sf /etc/echo/nginx/sbin/nginx $(DESTDIR)/etc/echo/bin/openresty

clean:
	rm -rf build
