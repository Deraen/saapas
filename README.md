# saapas

*WIP*

Example project for Cljs/Om using Boot instead of Lein.
Inspired by [chestnut](https://github.com/plexus/chestnut).

For now, this is a example project instead of a lein template.

## Development

To start everything (nrepl server, cljx, cljs build, livereload, the app) run:
```
$ boot dev
boot.user=>
# App should now be running, you can use following commands to restart the app
boot.user=> (in-ns 'saapas.main)
saapas.main=> (stop) ; Stop app
saapas.main=> (start) ; Start app
saapas.main=> (reset) ; Stop, reload all namespaces, start
```

If you want a repl where you can execute commands, you can start a nrepl
client which connects to the nrepl server started by the dev task by running
```
$ boot dev-repl
```

## TODO

- [x] Cljx nrepl middleware
- [x] Less compilation and css livereload
  - Blocked: Requires less plugin for Boot
- [x] Production build (uberjar)
- [ ] Tests (midje)
  - Blocked: Requires midje plugin for Boot
