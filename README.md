# data-inception

A Clojure library designed to extract and clean data from different sources giving list of Open Data portals.

The basic idea is to extract portals names, URLs, Organisation, some details if they exist and the location if it exists.

## Result

You can check the final dataset in its latest version:
[Open Data Inception](http://public.opendatasoft.com/explore/dataset/open-data-sources/?tab=table)

## Usage

You'll need [Leiningen](http://leiningen.org/)

Clone the repository

    $ cd data-inception
    
    $ lein deps
    
    $ lein repl
    
    ods.pipeline> (okfn-pipeline "path-to-the-okfn-open-data-portals-data-file.csv")

## License

Copyright © 2015 OpenDataSoft

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
