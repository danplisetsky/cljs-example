(ns exercise.core
  (:require [reagent.core :as r]))

(defn root []
  (.getElementById js/document "app"))

(defn mount-root! [component]
  (r/render component (root)))

(defn start []
  (mount-root! nil))
