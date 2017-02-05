# Saapas

<img src="saapas.png" align="right">

Opinionated example project for ClojureScript using Boot instead of Leiningen.
Was inspired by [chestnut] but has grown since to include other stuff to
demonstrate Boot.

This is not an Leiningen template as I don't believe in setting up complex
projects automatically. Instead you should study this project and copy
only stuff you need and understand.

### Prerequisites

You should first [install Boot][install]. Also you should be running the
latest version.

## Features

- Uses [component] and [reloaded.repl] to provide utilities to `start`,
  `stop` and `reset` (reload) the app.
- Simple [Compojure][compojure] backend
- Simple [Reagent][reagent] frontend
- [LESS][less] to write CSS
  - [less4clj] is able to import files from classpath
  - You can add dependency to e.g. bootstrap from [webjars] to
    your regular dependencies and then use `@import "bootstrap/less/bootstrap.less"`
    on your `.less` files.
- or alternatively, [Sass][sass] to write CSS
  - [sass4clj] is also able to import files from classpath
- `dev` task starts the whole development workflow
  - Check `boot dev --help` for options
  - [Browser repl][boot-cljs-repl] included
  - Watches for file changes
    - \*.less changes trigger less compilation
    - \*.cljs changes trigger [cljs][boot-cljs] compilation
  - [Live-reloading][boot-reload]
    - \*.js, \*.css, \*.html changes send notification to browser thought WebSocket and browser loads the new files
- `autotest` task to run Clj and Cljs tests whenever files are changed
  - Uses [boot-alt-test] to run Clojure tests as fast as possible and
  only running the changed tests are file changes
  - Uses [boot-cljs-test] to run ClojureScript tests with [Doo], allowing the tests
  to run in many of JS environments, like browsers, Node or PhantomJS.
- Provides `package` task that creates Uberjar that can be used to run the app on a server
  - Cljs will be compiled using `:advanced` optimization and uses minified foreign libraries, like React
  - Only [`backend.main`](./src/clj/backend/main.clj) is AOT compiled, so that it is
  possible to start the application with `java -jar saapas.jar` but so that
  rest of the code is compiled at application startup to avoid problems with AOT
  compilation.

## Usage

To start everything run:
```
$ boot dev
boot.user=>
# App should now be running, you can use following commands to restart the app
boot.user=> (stop) ; Stop app
boot.user=> (start) ; Start app
boot.user=> (reset) ; Stop, reload all namespaces, start
```

If you want a repl where you can execute commands, you can start a nrepl
client which connects to the nrepl server started by the dev task by running
```
$ boot repl -c
```

## License

<p xmlns:dct="http://purl.org/dc/terms/" xmlns:vcard="http://www.w3.org/2001/vcard-rdf/3.0#">
  <a rel="license"
     href="http://creativecommons.org/publicdomain/zero/1.0/">
    <img src="http://i.creativecommons.org/p/zero/1.0/88x31.png" style="border-style: none;" alt="CC0" />
  </a>
  <br />
  To the extent possible under law,
  <span resource="[_:publisher]" rel="dct:publisher">
    <span property="dct:title">Juho Teperi</span></span>
  has waived all copyright and related or neighboring rights to
  <span property="dct:title">Saapas</span>.
This work is published from:
<span property="vcard:Country" datatype="dct:ISO3166"
      content="FI" about="[_:publisher]">
  Suomi</span>.
</p>

[chestnut]: https://github.com/plexus/chestnut
[install]: https://github.com/boot-clj/boot#install
[component]: https://github.com/stuartsierra/component
[reloaded.repl]: https://github.com/weavejester/reloaded.repl
[compojure]: https://github.com/weavejester/compojure
[reagent]: https://github.com/reagent-project/reagent
[LESS]: http://lesscss.org/
[sass]: http://sass-lang.com/
[less4clj]: https://github.com/Deraen/less4clj
[sass4clj]: https://github.com/Deraen/sass4clj
[webjars]: http://www.webjars.org
[boot-cljs]: https://github.com/boot-clj/boot-cljs
[boot-cljs-repl]: https://github.com/adzerk/boot-cljs-repl
[boot-reload]: https://github.com/adzerk/boot-reload
[boot-alt-test]: https://github.com/metosin/boot-alt-test
[boot-cljs-test]: https://github.com/crisptrutski/boot-cljs-test
[Doo]: https://github.com/bensu/doo
