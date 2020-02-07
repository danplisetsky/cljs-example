(ns utils
  (:require [shadow.cljs.devtools.api :as shadow-api]))

(defn switch-to-app-repl
  []
  (shadow-api/repl :app))


