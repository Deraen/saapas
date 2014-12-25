# Saapas

Example project for Cljs/Om using Boot instead of Lein.
Inspired by [chestnut](https://github.com/plexus/chestnut).

For now, this is a example project instead of a lein template.

## Features

- Simple [Compojure](https://github.com/weavejester/compojure) backend
- Simple [Om](https://github.com/swannodette/om) frontend
- [Cljx](https://github.com/lynaghk/cljx) to write code targetting both Clojure and ClojureScript
- [LESS](http://lesscss.org/) to write stylesheets (Disclaimer: boot-less still needs some love to work reliably)
  - [boot-less](https://github.com/Deraen/boot-less) is able to import files from classpath
  - You can add dependency to e.g. bootstrap from [webjars](https://webjars.org) to
    your regular dependencies and then add `@import "bootstrap/less/bootstrap.less"`
    to your `.less` files.
- `dev` task starts the whole development workflow
  - Repl should automatically include cljx and cljs middlewares
  - [Browser repl](https://github.com/adzerk/boot-cljs-repl) included
  - No need to change `index.html`, resulting *unified* JS can be loaded like `:advanced` compiled.
  - Watches for file changes
    - \*.cljx changes trigger [cljx](https://github.com/Deraen/boot-cljx) compilation
    - \*.less changes trigger less compilation
    - \*.cljx changes trigger [cljs](https://github.com/adzerk/boot-cljs) compilation
  - [Live-reloading](https://github.com/adzerk/boot-reload)
    - \*.js, \*.css, \*.html changes send notification to browser throught websocket and browser loads the new files
- `package` task creates uberjar
  - Cljs will be compiled using `:advanced` optimization
  - Uses minified react.js

## Usage

To start everything run:
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
$ boot repl -c
```

## TODO

- [x] Cljx nrepl middleware
- [x] Less compilation and css livereload
- [x] Production build (uberjar)
- [ ] Tests
  - [boot-test](https://github.com/adzerk/boot-test)
