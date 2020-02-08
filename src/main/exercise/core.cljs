(ns exercise.core
  (:require [clojure.string :as string])
  (:require [exercise.fetcher :as fetcher])
  (:require [reagent.core :as r]))

;; STATE

(def state (r/atom {:search-results []
                    :loading        false}))

(def loading? (r/cursor state [:loading]))
(def search-results (r/cursor state [:search-results]))

;; COMPONENTS

(defn show-records
  [artist]
  [:button {:type     "button"
            :disabled (or @loading? (empty? artist))
            :on-click (fn []
                        (reset! search-results [])
                        (fetcher/search-iTunes artist
                                               loading?
                                               search-results))}
   "Show Records"])

(defn input-artist
  [artist]
  [:div
   [:label {:for "artist"} "Artist"]
   [:input {:id        "artist"
            :type      "text"
            :on-change #(reset! artist (-> % .-target .-value))}]])

(defn spinner
  []
  (let [dots (r/atom 1)]
    (fn render []
      (js/setTimeout #(if (= @dots 3)
                        (reset! dots 1)
                        (swap! dots inc))
                     400)
      [:p (str "Loading" (-> (repeat @dots ".") string/join))])))


(def primary-keys ["artistName" "trackName" "releaseDate" "primaryGenreName"])
(def secondary-keys ["trackPrice"])

(defn item-column
  [entry keys]
  (into [:div]
        (map #(vector :p %)
             (->> keys (select-keys entry) vals))))

(defn item
  [result-entry primary-keys secondary-keys]
  [:li {:key (get result-entry "trackId")}
   [item-column result-entry primary-keys]
   [item-column result-entry secondary-keys]])


(defn results
  [search-results]
  (into [:ul {:id "search-results"}]
        (map #(item % primary-keys secondary-keys)
             search-results)))

(defn app
  []
  (let [artist (r/atom "")]
    (fn render []
      (let [search-results @search-results]
        [:<>
         [input-artist artist]
         [show-records @artist]
         (when @loading? [spinner])
         (when (seq search-results) [results search-results])]))))

;; MOUNT

(defn root []
  (.getElementById js/document "app"))

(defn mount-root! [component]
  (r/render component (root)))

(defn start []
  (mount-root! [app]))

(start)

