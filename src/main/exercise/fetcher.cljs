(ns exercise.fetcher
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defonce ^:private iTunes-url "https://itunes.apple.com/search?term=")

(defn search-iTunes
  [artist loading? search-results]
  (swap! loading? not)
  (go (let [response (<! (http/get (str iTunes-url artist)))]
        (js/console.log response)
        (swap! loading? not)
        (reset! search-results (-> response
                                   :body
                                   js/JSON.parse
                                   js->clj
                                   (get "results"))))))
