(ns hospital-v2.logic
  (:require [hospital-v2.model :as h.model]))

(defn agora []
  (h.model/to-ms (java.util.Date.)))