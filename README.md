# saapas

*WIP*

Example project for Cljs/Om using Boot instead of Lein.
Inspired by [chestnut](https://github.com/plexus/chestnut).

For now, this is a example project instead of a lein template.

## Development

To start everything (nrepl server, cljx, cljs build, livereload) run:
```
$ boot dev
```

If you want a repl where you can execute commands, you can start a nrepl
client which connects to the nrepl server started by the dev task by running
```
$ boot dev-repl
```

## TODO

- [ ] Cljx nrepl middleware
  - Blocked: Looks like it's not possible to add middleware when using cljs-repl
- [ ] Less compilation and css livereload
  - Blocked: Requires less plugin for Boot
- [ ] Production build (uberjar)
- [ ] Tests (midje)
  - Blocked: Requires midje plugin for Boot
