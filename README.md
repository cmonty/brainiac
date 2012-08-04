[![Build Status](https://secure.travis-ci.org/cmonty/brainiac.png)](http://travis-ci.org/cmonty/brainiac)
# brainiac

Brainiac is a company dashboard, optimized for a display on large monitors - so really more of a billboard. It's aimed at software development organizations, but can definitely be used more generally. 

It can display information from a variety of sources, including: Jenkins, Nagios, Pager Duty, Github, Google Weather, Twitter, Chicago Transit Authority, Elovation, Jukebox2 and RSS feeds. 

Brainiac is a Leinigen app, written in Clojure. It is currently optimized for display on an HDTV (1920x1080 resolution), on recent Webkit and Firefox browsers.

## Usage

Install Clojure and Leinigen on your server (Brew makes this easy on Mac). Check out the source from Github. Copy config.yml.example to config.yml. Edit config.yml to choose the panels you would like to display, and to configure logins and locations for various data sources. Go the directory that contains the app and run `lein deps` to bring in any dependencies, and then `lein run` to start the server on localhost:8080.

## License

Copyright (C) 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
