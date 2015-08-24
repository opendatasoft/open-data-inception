(defproject data-inception "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [grafter "0.5.1"]
                 [clj-http "2.0.0"]]

  :main data-inception.core

  :repl-options {:init (set! *print-length* 200)
                 :init-ns data-inception.core })
